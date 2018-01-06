package com.fri.musicbook.health;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.Optional;


@Health
@ApplicationScoped
public class AmIOnEtcd implements HealthCheck {
    @Inject
    @DiscoverService("users")
    Optional<String> users_url;

    @Override
    public HealthCheckResponse call(){
        if(users_url.isPresent()){
            return HealthCheckResponse.named(AmIOnEtcd.class.getSimpleName()).up().build();
        }
        return HealthCheckResponse.named(AmIOnEtcd.class.getSimpleName()).down().build();
    }
}
