package dk.au.mad22spring.app.project.liarsdice.Utilities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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

import dk.au.mad22spring.app.project.liarsdice.Models.StaticUser;
import dk.au.mad22spring.app.project.liarsdice.Models.User;

public class FirestoreUtil {
    private FirebaseFirestore db = null;
    private static FirestoreUtil instance = null;
    private static final String TAG = "FirestoreUtil: ";

    private final MutableLiveData<User> user = new MutableLiveData<User>();

    private FirestoreUtil() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirestoreUtil getFirestore() {
        if (instance == null) {
            instance = new FirestoreUtil();
        }
        return instance;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void updateUser(User inputUser) {
        Map<String, Object> user = new HashMap<>();
        user.put("UUID", inputUser.UUID);
        user.put("Displayname", inputUser.Displayname);
        user.put("Loses", inputUser.Loses);
        user.put("TotalGames", inputUser.TotalGames);
        db.collection("users")
                .document(inputUser.UUID)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + inputUser.UUID);
                        StaticUser.staticUser = inputUser;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document: ", e);
                    }
                });
    }

    public void updateStats(User _user) {
        Map<String, Object> user = new HashMap<>();
        user.put("UUID", _user.UUID);
        user.put("Displayname", _user.Displayname);
        user.put("Loses", _user.Loses);
        user.put("TotalGames", _user.TotalGames);
        Log.d("CRASH", "DocumentSnapshot added with ID: " + _user.UUID);

        db.collection("users")
                .document(_user.UUID)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + _user.UUID);
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
                        _user.UUID = data.get("UUID").toString();
                        _user.Displayname = data.get("Displayname").toString();
                        _user.Loses = data.get("Loses").toString();
                        _user.TotalGames = data.get("TotalGames").toString();
                        _user.Wins = String.valueOf(Integer.parseInt(_user.TotalGames) - Integer.parseInt(_user.Loses));

                        user.setValue(_user);
                        StaticUser.staticUser = _user;
                        Log.d("CRASH", "Static user added with ID: " + _user.UUID);

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
