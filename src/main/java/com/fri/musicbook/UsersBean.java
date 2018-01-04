package com.fri.musicbook;


import java.util.List;

public class UsersBean {
    public static User getUserByEmail(String email){
        return UsersDB.getUserByEmail(email);
    }

    public static List<User> getUsers(){
        return UsersDB.getUsers();
    }

    public static boolean addUser(User user){
        if(     (!(user.getEmail() == null)        && !user.getEmail().isEmpty()     ) &&
                (!(user.getName() == null)         && !user.getName().isEmpty()      ) &&
                (!(user.getSurname() == null)      && !user.getSurname().isEmpty()   ) &&
                (!(user.getTypeOfUser() == null)   && !user.getTypeOfUser().isEmpty()) &&
                (user.getTypeOfUser().equals("Band") || user.getTypeOfUser().equals("Listener")) &&
                UsersDB.getUserByEmail(user.getEmail())==null
                ){
            List<User> users=UsersDB.getUsers();
            user.setId(users.get(users.size()-1).getId()+1); // povečamo id za 1
            UsersDB.addUser(user);
            return true;
        }
        return false;
    }

    public static boolean deleteUserByEmail(String email){
        User user=UsersDB.getUserByEmail(email);
        if(user!=null) return UsersDB.deleteUser(user.getId());
        return false;

    }
}
