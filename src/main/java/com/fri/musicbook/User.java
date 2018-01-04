package com.fri.musicbook;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String surname;
    private String email;
    private List<String> subsGenre = new ArrayList<>();;
    private List<String> subsBands = new ArrayList<>();;
    private String typeOfUser; // Band or Listener

    public User() {
    }

    public User(int id, String name, String surname, List<String> subsGenre, List<String> subsBands, String typeOfUser,String email) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
