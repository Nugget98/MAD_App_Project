package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.Models.User;

public class ProfileActivityViewModel extends ViewModel {
    private GoogleAuthenticationUtil googleAuthenticationUtil;
    private FirestoreUtil firestoreUtil;
    private String displayName = "";

    private User user;

    private final MutableLiveData<User> liveData = new MutableLiveData<User>();

    public ProfileActivityViewModel(){
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
        firestoreUtil = FirestoreUtil.getFirestore();
    }

    public void getUser() {
        firestoreUtil.doesUserExist(googleAuthenticationUtil.getSignedInUserUID());
        firestoreUtil.getUser().observeForever(_user -> {
            if(_user != null){
                Log.d("Profile", "User exist: true");
                user = _user;
                liveData.setValue(user);
            }
        });
    }

    public MutableLiveData<User> getLiveData() {
        return liveData;
    }

    public String getDisplayName(){
        return user.Displayname;
    }

    public void setDisplayName(String _displayName){

        user.Displayname = _displayName;
    }

}
