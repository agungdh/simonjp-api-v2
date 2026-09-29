package id.my.agungdh.filter;

import id.my.agungdh.entity.User;
import id.my.agungdh.service.AuthService;
import id.my.agungdh.util.CurrentUser;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final String SESSION_COOKIE_NAME = "SESSION_TOKEN";
    private static final Set<String> PUBLIC_PATHS = Set.of("/auth/login");
    private static final String PUBLIC_PATH_PREFIX = "/q/";

    @Inject
    AuthService authService;

    @Inject
    CurrentUser currentUser;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();

        if (isPublic(path)) {
            return;
        }

        Cookie sessionCookie = requestContext.getCookies().get(SESSION_COOKIE_NAME);
        if (sessionCookie == null || sessionCookie.getValue().isBlank()) {
            requestContext.abortWith(Response.status(401)
                    .entity("{\"error\":\"Unauthorized\",\"message\":\"Session tidak ditemukan\"}")
                    .type("application/json")
                    .build());
            return;
        }

        Optional<User> user = authService.validateSession(sessionCookie.getValue());
        if (user.isEmpty()) {
            requestContext.abortWith(Response.status(401)
                    .entity("{\"error\":\"Unauthorized\",\"message\":\"Session tidak valid atau sudah expired\"}")
                    .type("application/json")
                    .build());
            return;
        }

        User u = user.get();
        currentUser.setId(u.id);
        currentUser.setUuid(u.uuid);
        currentUser.setUsername(u.username);
    }

    private boolean isPublic(String path) {
        if (PUBLIC_PATHS.contains(path)) {
            return true;
        }
        if (path.startsWith(PUBLIC_PATH_PREFIX)) {
            return true;
        }
        return false;
    }
}
