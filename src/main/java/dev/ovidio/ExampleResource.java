package dev.ovidio;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {
        var entity = new MyEntity();
        entity.field = "Hello World";
        MyEntity.persist(entity);
        var all = MyEntity.findAll();
        return "Hello from Quarkus REST: "+ all.count();
    }
}
