package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;

public class HomeActivity extends AppCompatActivity {

    public static final String KEY_ROOM_NUMBER = "roomNumber";
    private Button joinRoomButton, createRoomButton;
    private TextView roomNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialise();
    }

    private void initialise() {
        joinRoomButton = findViewById(R.id.joinRoomButton);
        createRoomButton = findViewById(R.id.createRoomButton);
        roomNumberText = findViewById(R.id.enterRoomNumber);

        joinRoomButton.setOnClickListener(view -> joinRoom());
        createRoomButton.setOnClickListener(view -> createRoom());
    }

    private void joinRoom() {
        Intent intent = new Intent(this, RoomActivity.class);

        int roomNumber = Integer.parseInt(roomNumberText.getText().toString());
        intent.putExtra(KEY_ROOM_NUMBER,roomNumber);
        startActivity(intent);
    }

    private void createRoom() {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
    }


}