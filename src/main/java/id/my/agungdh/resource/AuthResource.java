package id.my.agungdh.resource;

import id.my.agungdh.dto.AuthRequest;
import id.my.agungdh.service.AuthService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "auth", description = "Authentication operations")
@RunOnVirtualThread
public class AuthResource {

    private static final String SESSION_COOKIE_NAME = "SESSION_TOKEN";
    private static final int SESSION_MAX_AGE_SECONDS = 7 * 24 * 60 * 60;

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    @Operation(summary = "Login with username and password")
    public Response login(@Valid AuthRequest request, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        String ipAddress = headers.getHeaderString("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isBlank()) {
            ipAddress = headers.getHeaderString("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isBlank()) {
            ipAddress = uriInfo.getRequestUri().getHost();
        }
        String userAgent = headers.getHeaderString("User-Agent");

        String token = authService.login(request.username(), request.password(), ipAddress, userAgent);

        NewCookie cookie = new NewCookie.Builder(SESSION_COOKIE_NAME)
                .value(token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(SESSION_MAX_AGE_SECONDS)
                .sameSite(NewCookie.SameSite.LAX)
                .build();

        return Response.ok().cookie(cookie).build();
    }
}
