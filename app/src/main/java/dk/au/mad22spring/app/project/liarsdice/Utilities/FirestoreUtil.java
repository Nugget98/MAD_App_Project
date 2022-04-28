package dk.au.mad22spring.app.project.liarsdice.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtil {
    private FirebaseFirestore db;
    private static final String TAG = "FirestoreUtil: ";

    public FirestoreUtil() {
        if (db == null) {
            db = getFirestore();
        }
    }

    public FirebaseFirestore getFirestore() {
        if (db == null) {
            db = FirebaseFirestore.getInstance();
        }
        return db;
    }

    public void updateUser(String _uuid, String _displayName, Number _wins, Number _loses) {
        Map<String, Object> user = new HashMap<>();
        user.put("UUID", _uuid);
        user.put("Displayname", _displayName);
        user.put("Wins", _wins);
        user.put("Loses", _loses);
        db.collection("users")
                .document(_uuid)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + _uuid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document: ", e);
                    }
                });
    }

    public User readUser(String _uuid) {
        User user = new User();
        DocumentReference docRef = db.collection("users").document(_uuid);
         docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "DocumentSnapshot data: " + data.get("Displayname"));
                                user.setDisplayname(data.get("Displayname").toString());
                                Log.d(TAG, "User.getDisplayname: " + user.getDisplayname());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        return user;
    }

    public DocumentReference docRefResponse(String _uuid) {
        return db.collection("users").document(_uuid);
    }
}
