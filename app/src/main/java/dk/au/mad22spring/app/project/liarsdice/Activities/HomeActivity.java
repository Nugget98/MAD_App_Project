package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;

public class HomeActivity extends AppCompatActivity {

    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnProfile = findViewById(R.id.seeProfileButton);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });
    }

    private void profile() {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);  //Using start activity because we are not getting a result back
    }
}