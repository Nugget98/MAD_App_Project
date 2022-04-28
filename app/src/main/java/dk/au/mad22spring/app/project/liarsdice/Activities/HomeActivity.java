package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirestoreUtil firestoreUtil = new FirestoreUtil();
        GoogleAuthenticationUtil googleAuthenticationUtil = new GoogleAuthenticationUtil();
        firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(), "Penis", 5, 0);
        firestoreUtil.readUser(googleAuthenticationUtil.getSignedInUserUID());
    }
}