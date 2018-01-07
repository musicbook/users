package com.fri.musicbook;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
@RequestScoped
public class UsersDB {
    @PersistenceContext(unitName = "users-jpa")
    private EntityManager em;

    public List<User> getUsers() {
        Query query=em.createNamedQuery("users.getAll",User.class);
        List<User> test = query.getResultList();
        if(test==null) System.out.println("IM EMPTY");
        return test;
    }

    public User addUser(User user){

        try {
            beginTx();
            em.persist(user);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return null;
        }
        return user;
    }

    public User getUser(int userId){
        User user = em.find(User.class,userId);
        return user;
    }

    public boolean deleteUser(String userId){
        User bandPost=em.find(User.class,userId);
        if(bandPost!=null) {
            try {
                beginTx();
                em.remove(bandPost);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
                return false;
            }
        }else return false;

        return true;
    }
/*
    public List<User> getUserByNameAndSurname(String name, String surname){
        List<User> found_users = new ArrayList<>();
        for(User user : users){
            if(user.getName().equals(name) && user.getSurname().equals(surname)){
                found_users.add(user);
            }
        }
        return found_users;
    }
*/
    public User getUserByEmail(String email){
        Query query=em.createNamedQuery("users.getUserByEmail",User.class).setParameter("email",email);
        List<User> test = query.getResultList();
        if(test!=null && test.size()==1) return test.get(0);
        return null;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }


}
