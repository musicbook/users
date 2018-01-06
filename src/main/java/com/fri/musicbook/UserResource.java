package com.fri.musicbook;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;
import com.kumuluz.ee.discovery.utils.DiscoveryUtil;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/")
@Log
@Metered(name = "UserResources")
public class UserResource {
    @Inject
    @DiscoverService("posts")
    private Optional<URL> posts_url;

    @GET
    public Response getAllUsers(){
        List<User> users=UsersBean.getUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("Bands")
    public Response getBands(){
        List<User> bands=UsersBean.getBands();
        if(bands==null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(bands).build();
    }

    @GET
    @Path("Listeners")
    public Response getListeners(){
        List<User> listeners=UsersBean.getListeners();
        if(listeners==null) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(listeners).build();
    }



    @GET
    @Path("email/{email}")
    public Response getUserByEmail(@PathParam("email") String email){
        User user=UsersBean.getUserByEmail(email);
        if (user != null) return Response.ok(user).build();

        return Response.status(Response.Status.NOT_FOUND).build(); //entity("User not found with email: "+email)
    }

    @POST
    public Response addUser(User user){
        if(UsersBean.addUser(user,posts_url)){

            return Response.status(Response.Status.CREATED).entity(user).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @DELETE
    @Path("email/{email}")
    public Response deleteUserByEmail(@PathParam("email") String email){
        if(UsersBean.deleteUserByEmail(email,posts_url)){
            return Response.ok().build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }
}
