package hello;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.jupiter.api.*;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTest {

    public static final String BASE_URI = "http://localhost:8080/";

    static HttpServer server;

    WebTarget target;

    @BeforeAll
    static void startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("hello").register(RolesAllowedDynamicFeature.class);
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    @BeforeEach
    void setUp() {
        Client c = ClientBuilder.newClient();
        //c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());
        target = c.target(BASE_URI);
    }

    @AfterAll
    static void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void testWithAuth() {
        String value = target.path("auth").request().post(Entity.entity(new AuthenticationCommand("user", "user"), MediaType.APPLICATION_JSON)).getCookies().get("token").getValue();
        System.out.printf("Token: %s", value);
        Response response = target.path("hello/user").request()
                .header("Authorization", "Bearer " + value)
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Hello JAX-WS!", response.readEntity(MessageResponse.class).getMessage());
    }

    @Test
    public void testNoAuth() {
        int status = target.path("hello/user").request().get().getStatus();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), status);
    }

    @Test
    public void testNoRole() {
        String value = target.path("auth").request().post(Entity.entity(new AuthenticationCommand("user", "user"), MediaType.APPLICATION_JSON)).getCookies().get("token").getValue();
        int status = target.path("hello/admin").request()
                .header("Authorization", "Bearer " + value)
                .get().getStatus();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), status);
    }

}
