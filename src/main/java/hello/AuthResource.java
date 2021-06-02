package hello;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("auth")
@PermitAll
public class AuthResource {

    private static final String COOKIE_NAME = "token";

    private static final Map<String, String> USERS = Map.of("user", "user", "admin", "admin");

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response auth(AuthenticationCommand authenticationCommand) {
        System.out.println("Auth: " + authenticationCommand.getUsername() + "/" + authenticationCommand.getPassword());

        String token = new TokenHandler().generateToken(authenticationCommand.getUsername(), USERS.get(authenticationCommand.getUsername()));
        NewCookie newCookie = new NewCookie(COOKIE_NAME, token, "/", "http://localhost:8080", "JWT", 30 * 60, true, true);

        return Response.ok(new MessageResponse("Successful"))
                .cookie(newCookie)
                .build();
    }

}
