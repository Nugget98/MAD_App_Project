package dk.au.mad22spring.app.project.liarsdice.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUtil {
    private FirebaseFirestore db;
    private static final String TAG = "FirestoreUtil: ";

    private final MutableLiveData<User> user = new MutableLiveData<User>();

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

    public MutableLiveData<User> getUser() {
        return user;
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

    public void doesUserExist(String _uuid){
        User _user = new User();
        docRefResponse(_uuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();

                        _user.setDisplayname(data.get("Displayname").toString());
                        _user.Wins = data.get("Wins").toString();
                        _user.Loses = data.get("Loses").toString();
                        _user.TotalGames = String.valueOf(Integer.parseInt(_user.Wins) - Integer.parseInt(_user.Loses));

                        user.setValue(_user);
                    } else {
                        Log.d(TAG, "No such document");
                        user.setValue(null);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public DocumentReference docRefResponse(String _uuid) {
        return db.collection("users").document(_uuid);
    }
}
