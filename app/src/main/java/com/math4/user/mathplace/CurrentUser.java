package com.math4.user.mathplace;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentUser {
    FirebaseUser user;
    String name;
    String surname;
    String password;
    String email;
    String id;
    String imageURL;
    static String lastTheme="Четность";
    static Map<String,Map<String,Object>> AllTask=new HashMap<>();
    static HashMap<String , ArrayList> task=new HashMap<>();
    public CurrentUser(){}
    public CurrentUser(FirebaseUser user, String name, String surname, String email, String password, String id){
        this.user=user;
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.password=password;
        this.id=id;
    }
    public void setMap(String name,int all_task){
        ArrayList<Boolean> array=new ArrayList<>();
        for(int i=0;i<all_task;i++){
            array.add(false);
        }
        task.put(name,array);
    }
    public static void setLastTheme(String name){
        lastTheme=name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getName(){
        return this.name;
    }
    public String getSurname(){
        return this.surname;
    }
    public String getId(){
        return this.id;
    }
    public FirebaseUser getUser(){
        return this.user;
    }
    public String getEmail(String email){
        return this.email;
    }
    public String getImageURL(){
        return this.imageURL;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setId(String id){
        this.password=password;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setSurname(String surname){
        this.surname=surname;
    }
    public void setUser(FirebaseUser user){
        this.user=user;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public HashMap<String,ArrayList> getTask(){
        return task;
    }
}
