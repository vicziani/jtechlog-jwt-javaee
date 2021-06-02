package hello;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        System.out.println("JerseyConfig");
        packages("hello");
        register(RolesAllowedDynamicFeature.class);
//        register(JwtFilter.class);
    }
}
