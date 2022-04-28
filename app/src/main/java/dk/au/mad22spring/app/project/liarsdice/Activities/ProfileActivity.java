package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                                Log.d(TAG, "DocumentSnapshot data: " + data.get("Displayname"));
                                user.setDisplayname(data.get("Displayname").toString());
                                updateUI(user);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


    }

    private void updateUI(User user) {
        Log.d("Profile: ", "user displayname - " + user.getDisplayname());
    }
}