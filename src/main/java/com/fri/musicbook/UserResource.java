package com.fri.musicbook;


import com.sun.org.apache.regexp.internal.RE;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("users/")
public class UserResource {
    @GET
    public Response getAllUsers(){
        List<User> users=UsersBean.getUsers();
        return Response.ok(users).build();
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
        if(UsersBean.addUser(user)){
            return Response.status(Response.Status.CREATED).entity(user).build();
        }
        return Response.status( Response.Status.CONFLICT).build();
    }

    @DELETE
    @Path("email/{email}")
    public Response deleteUserByEmail(@PathParam("email") String email){
        if(UsersBean.deleteUserByEmail(email)){
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }
}
