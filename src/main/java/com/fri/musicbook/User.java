package com.fri.musicbook;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity(name = "users")
@NamedQueries(value =
        {
                @NamedQuery(name = "users.getAll", query = "SELECT o FROM users o"),
                @NamedQuery(name = "users.getUser", query = "SELECT o FROM users o WHERE o.id = :id"),
                @NamedQuery(name = "users.getUserByEmail", query = "SELECT o FROM users o WHERE o.email = :email"),
                @NamedQuery(name = "users.getListeners", query = "SELECT o FROM users o WHERE o.typeOfUser = 'Listener'"),
                @NamedQuery(name = "users.getBands", query = "SELECT o FROM users o WHERE o.typeOfUser = 'Band'")
        })
@UuidGenerator(name = "idGenerator")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "idGenerator")
    private String id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @ElementCollection
    @Column(name = "subsGenre")
    private List<String> subsGenre = new ArrayList<>();

    @ElementCollection
    @Column(name = "subsBands")
    private List<String> subsBands = new ArrayList<>();

    @Column(name = "typeOfUser")
    private String typeOfUser; // Band or Listener

    public User() {
    }

    public User(String id, String name, String surname, List<String> subsGenre, List<String> subsBands, String typeOfUser,String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.subsGenre = subsGenre;
        this.subsBands = subsBands;
        this.typeOfUser = typeOfUser;
        this.email=email;
    }

    public User(String name, String surname, String email, String typeOfUser) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.typeOfUser = typeOfUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<String> getSubsGenre() {
        return subsGenre;
    }

    public void setSubsGenre(List<String> subsGenre) {
        this.subsGenre = subsGenre;
    }

    public List<String> getSubsBands() {
        return subsBands;
    }

    public void setSubsBands(List<String> subsBands) {
        this.subsBands = subsBands;
    }


    public String getTypeOfUser() {
        return typeOfUser;
    }

    public boolean setTypeOfUser(String typeOfUser) {
        if(typeOfUser.equals("Band") || typeOfUser.equals("Listener")){
            this.typeOfUser = typeOfUser;
            return true;
        }
        return false;
    }









}
