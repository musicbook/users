package com.fri.musicbook;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
import org.eclipse.microprofile.faulttolerance.*;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Bulkhead
@GroupKey("UsersBean")
public class UsersBean {
    public static User getUserByEmail(String email){
        return UsersDB.getUserByEmail(email);
    }

    public static List<User> getUsers(){
        return UsersDB.getUsers();
    }

    public static boolean addUser(User user,Optional<URL> posts_url){
        if(user == null) return false;
        if(     (!(user.getEmail() == null)        && !user.getEmail().isEmpty()     ) &&
                (!(user.getName() == null)         && !user.getName().isEmpty()      ) &&
                (!(user.getSurname() == null)      && !user.getSurname().isEmpty()   ) &&
                (!(user.getTypeOfUser() == null)   && !user.getTypeOfUser().isEmpty()) &&
                (user.getTypeOfUser().equals("Band") || user.getTypeOfUser().equals("Listener")) &&
                UsersDB.getUserByEmail(user.getEmail())==null
                ){
            List<User> users=UsersDB.getUsers();
            int new_id=users.get(users.size()-1).getId()+1;
            if(user.getTypeOfUser().equals("Band")) {
                if (!addNewUserPost(new_id, posts_url)) return false;
            }
            user.setId(new_id); // povečamo id za 1
            UsersDB.addUser(user);
            return true;
        }
        return false;
    }


    public static boolean deleteUserByEmail(String email,Optional<URL> posts_url){
        User user=UsersDB.getUserByEmail(email);
        if(user!=null) {
            if (user.getTypeOfUser().equals("Band")){
                return (removeBandPost(user.getId(),posts_url) && UsersDB.deleteUser(user.getId()));
            }
            return UsersDB.deleteUser(user.getId());

        }
        return false;

    }

    @CircuitBreaker
    @Timeout
    @Retry
    @Fallback(fallbackMethod = "postMSFallback")
    private static boolean removeBandPost(int bandId_int,Optional<URL> posts_url){
        Client httpClient = ClientBuilder.newClient();
        String bandId = Integer.toString(bandId_int);

        if(posts_url.isPresent()){
            String url = posts_url.get().toString();
            try {
                Response res = httpClient
                        .target(url + "/v1/posts/bandId/"+bandId)
                        .request().delete();
                if(res.getStatus()==Response.Status.OK.getStatusCode()) return true;
                System.out.println(res.getStatus());
            } catch (WebApplicationException | ProcessingException e) {
                return false;
            }
        }

        return false;
    }

    private static boolean postMSFallback(){
        return false;
    }


    @CircuitBreaker
    @Timeout
    @Retry
    @Fallback(fallbackMethod = "postMSFallback")
    private static boolean addNewUserPost(int bandId_int,Optional<URL> posts_url){
        Client httpClient = ClientBuilder.newClient();
        String bandId = Integer.toString(bandId_int);

        if(posts_url.isPresent()){
            String url = posts_url.get().toString();
            try {
                Response res = httpClient
                        .target(url + "/v1/posts")
                        .request().post(Entity.json("{ \"bandId\" : \""+bandId+"\"}"));
                if(res.getStatus()==201) return true;
                System.out.println(res.getStatus());
            } catch (WebApplicationException | ProcessingException e) {
                return false;
            }
        }

        return false;
    }


    public static List<User> getBands(){
        List<User> allUsers=UsersDB.getUsers();

        List<User> bands = allUsers.stream().filter(u -> u.getTypeOfUser().equals("Band")).collect(Collectors.toList());

        return bands;
    }

    public static List<User> getListeners(){
        List<User> allUsers=UsersDB.getUsers();
        List<User> listeners = allUsers.stream().filter(u -> u.getTypeOfUser().equals("Listener")).collect(Collectors.toList());

        return listeners;
    }
}
