package com.approdevelopers.ranchites.Repository;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserRepository {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseUser currentUser;


    private static FirebaseUserRepository fireBaseUserRepoInstance = null;

    private FirebaseUserRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    public static FirebaseUserRepository getInstance(){
        if (fireBaseUserRepoInstance==null){
            fireBaseUserRepoInstance = new FirebaseUserRepository();
        }
        return fireBaseUserRepoInstance;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }



}
