package com.fri.musicbook;

import java.util.ArrayList;
import java.util.List;

public class UsersDB {
    private static List<User> users= new ArrayList<User>(){{
        add(new User(0,"test","test",new ArrayList<>(),new ArrayList<>(),"Listener","test@musicbook.com"));
        add(new User(1,"Jan","Šubelj",new ArrayList<>(),new ArrayList<>(),"Band","janS@musicbook.com"));
        add(new User(2,"Jan","Blatnik",new ArrayList<>(),new ArrayList<>(),"Band","janB@musicbook.com"));
        add(new User(3,"Alojzi","Škof",new ArrayList<>(),new ArrayList<>(),"Listener","alojtiS@lj.com"));
        add(new User(4,"Andrej","Peterle",new ArrayList<>(),new ArrayList<>(),"Listener","andrejP@lj.com"));
    }};

    public static List<User> getUsers() {
        return users;
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static User getUser(int userId){
        for(User user : users){
            if(user.getId()==userId){
                return user;
            }
        }
        return null;
    }

    public static boolean deleteUser(int userId){
        for(User user : users){
            if(user.getId()==userId){
                users.remove(user);
                return true;
            }
        }

        return false;
    }

    public static List<User> getUserByNameAndSurname(String name, String surname){
        List<User> found_users = new ArrayList<>();
        for(User user : users){
            if(user.getName().equals(name) && user.getSurname().equals(surname)){
                found_users.add(user);
            }
        }
        return found_users;
    }

    public static User getUserByEmail(String email){
        for(User user : users){
            if(user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }


}
