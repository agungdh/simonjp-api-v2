package id.my.agungdh.resource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Validation Failed");

        Map<String, String> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        this::extractFieldName,
                        ConstraintViolation::getMessage,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        body.put("errors", errors);

        return Response.status(422)
                .entity(body)
                .build();
    }

    private String extractFieldName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        int dot = path.lastIndexOf('.');
        return dot >= 0 ? path.substring(dot + 1) : path;
    }
}
