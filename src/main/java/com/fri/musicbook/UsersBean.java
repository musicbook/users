package com.fri.musicbook;


import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
import org.eclipse.microprofile.faulttolerance.*;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class UsersBean {
    @Inject
    private UsersDB usersDB;

    public User getUserByEmail(String email){
        return usersDB.getUserByEmail(email);
    }

    public List<User> getUsers(){
        return usersDB.getUsers();
    }

    public boolean addUser(User user,Optional<URL> posts_url){
        if(user == null) return false;
        if(     (!(user.getEmail() == null)        && !user.getEmail().isEmpty()     ) &&
                (!(user.getName() == null)         && !user.getName().isEmpty()      ) &&
                (!(user.getSurname() == null)      && !user.getSurname().isEmpty()   ) &&
                (!(user.getTypeOfUser() == null)   && !user.getTypeOfUser().isEmpty()) &&
                (user.getTypeOfUser().equals("Band") || user.getTypeOfUser().equals("Listener")) &&
                usersDB.getUserByEmail(user.getEmail())==null
                ){
            List<User> users=usersDB.getUsers();
            User added_user = usersDB.addUser(user);
            if(added_user!=null && added_user.getTypeOfUser().equals("Band")){
                if (!addNewUserPost(added_user.getId(),posts_url)){
                    usersDB.deleteUser(added_user.getId());
                    return false;
                }
            }

            if(added_user==null) return false;
            return true;
        }
        return false;

    }


    public boolean deleteUserByEmail(String email,Optional<URL> posts_url){
        User user=usersDB.getUserByEmail(email);
        if(user!=null) {
            if (user.getTypeOfUser().equals("Band")){
                return (removeBandPost(user.getId(),posts_url) && usersDB.deleteUser(user.getId()));
            }
            return usersDB.deleteUser(user.getId());

        }
        return false;

    }

    @CircuitBreaker
    @Timeout
    @Retry
    @Fallback(fallbackMethod = "postMSFallback")
    private boolean removeBandPost(String bandId,Optional<URL> posts_url){
        Client httpClient = ClientBuilder.newClient();

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

    private boolean postMSFallback(){
        return false;
    }


    @CircuitBreaker
    @Timeout
    @Retry
    @Fallback(fallbackMethod = "postMSFallback")
    private boolean addNewUserPost(String bandId,Optional<URL> posts_url){
        Client httpClient = ClientBuilder.newClient();


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


    public List<User> getBands(){
        List<User> allUsers=usersDB.getUsers();

        List<User> bands = allUsers.stream().filter(u -> u.getTypeOfUser().equals("Band")).collect(Collectors.toList());

        return bands;
    }

    public List<User> getListeners(){
        List<User> allUsers=usersDB.getUsers();
        List<User> listeners = allUsers.stream().filter(u -> u.getTypeOfUser().equals("Listener")).collect(Collectors.toList());

        return listeners;
    }
}
