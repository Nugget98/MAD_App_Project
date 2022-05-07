package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import dk.au.mad22spring.app.project.liarsdice.LiarsDiceApplication;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.Models.User;

public class ProfileActivityViewModel extends ViewModel {
    private GoogleAuthenticationUtil googleAuthenticationUtil;
    private FirestoreUtil firestoreUtil;
    private String displayName = "";

    private User user;

    private final MutableLiveData<User> liveData = new MutableLiveData<User>();

    public ProfileActivityViewModel(){
        googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
        firestoreUtil = FirestoreUtil.getFirestore();
    }

    public void getUser() {
        firestoreUtil.doesUserExist(googleAuthenticationUtil.getSignedInUserUID());
        firestoreUtil.getUser().observeForever(_user -> {
            if(_user != null){
                Log.d("Profile", "User exist: true");
                user = _user;
                liveData.setValue(user);
            }
        });
    }

    public MutableLiveData<User> getLiveData() {
        return liveData;
    }

    public String getDisplayName(){
        return user.Displayname;
    }

    public void setDisplayName(String _displayName){

        user.Displayname = _displayName;
    }

    public void getRandomName(TextView txtDisplayName) {
        Log.d("API", "getRandomName called");

        RequestQueue queue = Volley.newRequestQueue(LiarsDiceApplication.getAppContext());
        String url = "https://api.namefake.com/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("{","");
                        response = response.replace("}", "");
                        String[] splitedResponse = response.split(",");
                        String newName = splitedResponse[0].replace("\"", "").replace("name:", "");
                        txtDisplayName.setText(newName);

                        Log.d("API", "" + newName);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "Error");
            }
        });

        queue.add(stringRequest);
    }

}
