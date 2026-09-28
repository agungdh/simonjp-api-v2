package id.my.agungdh.resource;

import id.my.agungdh.dto.ValidationTestRequest;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Path("/validation-test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "validation-test", description = "Endpoint for testing validation")
@RunOnVirtualThread
public class ValidationTestResource {

    @POST
    @Operation(summary = "Test validation (always returns error)")
    public Response test(@Valid ValidationTestRequest request) {
        return Response.ok(Map.of("message", "Valid!", "data", request)).build();
    }
}
