package hello;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("hello")
//@PermitAll
public class HelloResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    @Path("user")
    public MessageResponse sayHello(@Context SecurityContext context) {
        System.out.println("Username: " + context.getUserPrincipal().getName());
        System.out.println("Has user role? " + context.isUserInRole("user"));
        return new MessageResponse("Hello JAX-WS!");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("admin")
    public MessageResponse sayHelloToAdmin(@Context SecurityContext context) {
        return new MessageResponse("Hello admin!");
    }


}
