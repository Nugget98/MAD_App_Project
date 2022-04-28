package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dk.au.mad22spring.app.project.liarsdice.R;

public class RoomActivity extends AppCompatActivity {

    private TextView roomNumberText, playersText, diceRemainingText, shakeHelpText;
    private Button leaveRoomButton, rollDiceButton, loseRoundButton;
    private ImageView dice1Image, dice2Image, dice3Image, dice4Image, dice5Image, dice6Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initialise();

        leaveRoomButton.setOnClickListener(view -> finish());
    }

    private void initialise() {
        findViewById(R.id.roomNumberText);
        findViewById(R.id.playersText);
        findViewById(R.id.diceRemainingText);
        findViewById(R.id.shakeHelpText);
        findViewById(R.id.leaveRoomButton);
        findViewById(R.id.rollDiceButton);
        findViewById(R.id.loseRoundButton);
        findViewById(R.id.dice1Image);
        findViewById(R.id.dice2Image);
        findViewById(R.id.dice3Image);
        findViewById(R.id.dice4Image);
        findViewById(R.id.dice5Image);
        findViewById(R.id.dice6Image);
    }
}