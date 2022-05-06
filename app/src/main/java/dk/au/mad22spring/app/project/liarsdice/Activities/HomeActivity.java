package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Services.ForegroundService;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.GoogleAuthenticationUtil;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.HomeActivityViewModel;

public class HomeActivity extends AppCompatActivity {

    public static final String KEY_ROOM_NUMBER = "roomNumber";
    private Button joinRoomButton, createRoomButton, signOutButton;
    private TextView roomNumberText;

    private HomeActivityViewModel viewModel;

    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        initialise();

        startForegroundService();

        GoogleAuthenticationUtil googleAuthenticationUtil = GoogleAuthenticationUtil.getInstance();
        FirestoreUtil firestoreUtil = new FirestoreUtil();
        firestoreUtil.doesUserExist(googleAuthenticationUtil.getSignedInUserUID());
        firestoreUtil.getUser().observe(this, user -> {
            if(user != null){
                Log.d("HOME", "User exist: true");
            } else {
                Log.d("HOME", "User exist: false");
                firestoreUtil.updateUser(googleAuthenticationUtil.getSignedInUserUID(),googleAuthenticationUtil.getSignedInUserName(),0,0);
            }
        });

    }

    private void initialise() {
        joinRoomButton = findViewById(R.id.joinRoomButton);
        createRoomButton = findViewById(R.id.createRoomButton);
        signOutButton = findViewById(R.id.signOutButton);
        roomNumberText = findViewById(R.id.enterRoomNumber);

        joinRoomButton.setOnClickListener(view -> joinRoom());
        createRoomButton.setOnClickListener(view -> createRoom());
        signOutButton.setOnClickListener(view -> signOut());
    }

    private void joinRoom() {
        Intent intent = new Intent(this, RoomActivity.class);

        String roomNumber = roomNumberText.getText().toString();
        if(roomNumber.length() < 5) {
            Toast.makeText(this, "Please provide a valid room number", Toast.LENGTH_SHORT).show();
            roomNumberText.setText("");
        }
        else {
            int roomNumberInt = Integer.parseInt(roomNumber);
            intent.putExtra(KEY_ROOM_NUMBER,roomNumberInt);

            roomNumberText.setText("");
            startActivity(intent);
        }
    }

    private void createRoom() {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        viewModel.signOut();
        finish();
    }

    private void startForegroundService() {
        Intent foregroundServiceIntent = new Intent(this, ForegroundService.class);
        startService(foregroundServiceIntent);

        btnProfile = findViewById(R.id.seeProfileButton);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile();
            }
        });
    }

    private void profile() {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);  //Using start activity because we are not getting a result back
    }
}