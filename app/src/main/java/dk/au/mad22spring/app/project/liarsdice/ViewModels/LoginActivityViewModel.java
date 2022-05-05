package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad22spring.app.project.liarsdice.LiarsDiceApplication;
import dk.au.mad22spring.app.project.liarsdice.R;

public class LoginActivityViewModel extends ViewModel {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient signInClient;

    public LoginActivityViewModel(Activity loginActivity) {
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LiarsDiceApplication.getAppContext().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(loginActivity, gso);
    }

    public GoogleSignInClient getSignInClient() {
        return signInClient;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
