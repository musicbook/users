package com.fri.musicbook.config;


import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("kumuluzee")
public class ConfigProperties {

    @ConfigValue("server.base-url")
    private String baseServer;

    public String getBaseServer() {
        return baseServer;
    }

    public void setBaseServer(String baseServer) {
        this.baseServer = baseServer;
    }
}
