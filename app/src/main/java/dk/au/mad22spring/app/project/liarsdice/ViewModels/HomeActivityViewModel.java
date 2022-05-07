package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.Models.User;

public class HomeActivityViewModel extends ViewModel {

    private GoogleAuthenticationUtil googleAuthenticationUtil;
    private FirestoreUtil firestoreUtil;
    private androidx.lifecycle.Observer<User> observer;

    public HomeActivityViewModel() {
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
        firestoreUtil = FirestoreUtil.getFirestore();
    }

    public void signOut() {
        googleAuthenticationUtil.signOut();
    }

    public void checkUser() {
        observer = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    Log.d("HOME", "User exist: true");
                } else {
                    Log.d("HOME", "User exist: false");
                    firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(),googleAuthenticationUtil.getSignedInUserName(),0,0);
                }
            }
        };

        firestoreUtil.doesUserExist(googleAuthenticationUtil.getSignedInUserUID());
        firestoreUtil.getUser().observeForever(observer);
    }
}
