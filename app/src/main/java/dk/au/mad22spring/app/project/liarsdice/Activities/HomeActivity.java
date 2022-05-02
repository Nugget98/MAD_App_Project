package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.HomeActivityViewModel;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.RoomActivityViewModel;

public class HomeActivity extends AppCompatActivity {

    public static final String KEY_ROOM_NUMBER = "roomNumber";
    private Button joinRoomButton, createRoomButton, signOutButton;
    private TextView roomNumberText;

    private HomeActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = new ViewModelProvider(this).get(HomeActivityViewModel.class);

        initialise();
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
        viewModel.singOut();
        finish();
    }


}