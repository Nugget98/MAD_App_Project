package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    private TextView txtWins;
    private TextView txtLoses;
    private TextView txtTotalGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtWins = findViewById(R.id.txtDisplayWins);
        txtLoses = findViewById(R.id.txtDisplayLoses);
        txtTotalGames = findViewById(R.id.txtDisplayTotalGames);

        FirestoreUtil firestoreUtil = new FirestoreUtil();
        GoogleAuthenticationUtil googleAuthenticationUtil = new GoogleAuthenticationUtil();
        User user = new User();
        firestoreUtil.docRefResponse(googleAuthenticationUtil.getSignedInUserUID()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "DocumentSnapshot data: " + data.get("Wins"));
                                user.setDisplayname(data.get("Displayname").toString());
                                user.Wins = data.get("Wins").toString();
                                user.Loses = data.get("Loses").toString();
                                user.TotalGames = String.valueOf(Integer.parseInt(user.Wins) - Integer.parseInt(user.Loses));
                                Log.d(TAG, "User wins: " + user.Wins);
                                updateUI(user);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        //user.TotalGames = String.valueOf(Integer.parseInt(user.Wins) - Integer.parseInt( user.Loses));

        txtWins.setText("" + user.Wins);
        txtLoses.setText(user.Loses);
        //txtTotalGames.setText(user.TotalGames);


    }

    private void updateUI(User user) {
        Log.d("Profile: ", "user displayname - " + user.getDisplayname());
    }
}