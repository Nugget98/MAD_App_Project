package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;

public class HomeActivityViewModel extends ViewModel {

    private GoogleAuthenticationUtil googleAuthenticationUtil;

    public HomeActivityViewModel() {
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
    }

    public void singOut() {
        googleAuthenticationUtil.signOut();
    }
}
