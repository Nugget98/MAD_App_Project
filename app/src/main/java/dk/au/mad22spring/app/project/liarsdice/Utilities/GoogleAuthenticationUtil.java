package dk.au.mad22spring.app.project.liarsdice.Utilities;

import com.google.firebase.auth.FirebaseAuth;

public class GoogleAuthenticationUtil {

    private FirebaseAuth firebaseAuth;

    private static GoogleAuthenticationUtil instance = null;

    public static GoogleAuthenticationUtil getInstance() {
        if (instance == null) {
            instance = new GoogleAuthenticationUtil();
        }
        return instance;
    }

    private GoogleAuthenticationUtil() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getSignedInUserName() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    public String getSignedInUserUID() {
        return firebaseAuth.getUid();
    }

    public void signOut() {
        firebaseAuth.signOut();
    }
}
