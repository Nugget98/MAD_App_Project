package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.User;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "FirestoreUtil: ";

    private TextView txtUsername;
    private TextView txtWins;
    private TextView txtLoses;
    private TextView txtTotalGames;

    private Button btnSave;
    private Button btnCancel;

    private FirestoreUtil firestoreUtil;
    private GoogleAuthenticationUtil googleAuthenticationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        checkUser();
    }

    private void init() {
        txtWins = findViewById(R.id.txtDisplayWins);
        txtLoses = findViewById(R.id.txtDisplayLoses);
        txtTotalGames = findViewById(R.id.txtDisplayTotalGames);
        txtUsername = findViewById(R.id.editTextUsername);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(view -> save());
        btnCancel.setOnClickListener(view -> cancel());

        firestoreUtil = new FirestoreUtil();
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
    }

    private void checkUser() {
        firestoreUtil.doesUserExist(googleAuthenticationUtil.getSignedInUserUID());
        firestoreUtil.getUser().observe(this, user -> {
            if(user != null){
                Log.d("Profile", "User exist: true");
                txtUsername.setText(user.Displayname);
                txtWins.setText(user.Wins);
                txtLoses.setText(user.Loses);
                txtTotalGames.setText(user.TotalGames);
            } else {
                Log.d("Profile", "User exist: false");
                firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(),googleAuthenticationUtil.getSignedInUserName(),0,0);
            }
        });
    }

    private void save() {
        firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(), txtUsername.getText().toString(), Integer.parseInt(txtWins.getText().toString()), Integer.parseInt(txtLoses.getText().toString()));
        finish();
    }

    private void cancel() {
        finish();
    }
}