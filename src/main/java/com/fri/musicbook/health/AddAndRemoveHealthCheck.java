package com.fri.musicbook.health;

import com.fri.musicbook.User;
import com.fri.musicbook.config.ConfigProperties;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Health
@ApplicationScoped
public class AddAndRemoveHealthCheck implements HealthCheck {

    Client httpClient = ClientBuilder.newClient();

    private static final Logger LOG = Logger.getLogger(AddAndRemoveHealthCheck.class.getSimpleName());

    @Inject
    private ConfigProperties configProperties;

    @Override
    public HealthCheckResponse call(){
        if(CreateNewUser() && RemoveCreatedUser()){
            return HealthCheckResponse.named(AddAndRemoveHealthCheck.class.getSimpleName()).up().build();
        }
        return HealthCheckResponse.named(AddAndRemoveHealthCheck.class.getSimpleName()).down().build();

    }

    private boolean CreateNewUser(){
        User newUser = new User("HC_test", "HC_test", "HC_test@musicbook.com", "Listener");

            try {
            Response res = httpClient
                    .target(configProperties.getBaseServer()+"/v1/users")
                    .request().post(Entity.json(newUser));
            if(res.getStatus()== Response.Status.CREATED.getStatusCode()) return true;
            LOG.warning("Get status message code: " + res.getStatus() +" Data: " +res.getEntity());
        }catch (Exception e){
            LOG.severe(e.getMessage());
        }
        return false;
    }

    private boolean RemoveCreatedUser(){
        try {
            Response res = httpClient
                    .target(configProperties.getBaseServer()+"/v1/users/email/HC_test@musicbook.com")
                    .request().delete();
            if(res.getStatus()==Response.Status.OK.getStatusCode()) return true;
            LOG.warning("Get status message code: " + res.getStatus() +" Data: " +res.getEntity());
        }catch (Exception e){
            LOG.severe(e.getMessage());
        }
        return false;

    }
}
