package dk.au.mad22spring.app.project.liarsdice.Utilities;

import com.google.firebase.auth.FirebaseAuth;

public class GoogleAuthenticationUtil {

    private FirebaseAuth firebaseAuth;

    public GoogleAuthenticationUtil() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public String getSignedInUserName() {
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    public String getSignedInUserUID() {
        return firebaseAuth.getUid();
    }
}
