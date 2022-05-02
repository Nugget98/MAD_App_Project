package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.Models.Room;
import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.RoomActivityFactory;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.RoomActivityViewModel;

public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";

    private TextView roomNumberText, playersText, diceRemainingText, shakeHelpText;
    private Button leaveRoomButton, rollDiceButton, loseRoundButton;
    private ImageView dice1Image, dice2Image, dice3Image, dice4Image, dice5Image, dice6Image;

    private RoomActivityViewModel viewModel;

    private int numberOfDice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        numberOfDice = RealtimeDatabaseUtil.StartNumberOfDice;

        Intent intentFromListActivity = getIntent();
        int roomNumber = intentFromListActivity.getIntExtra(HomeActivity.KEY_ROOM_NUMBER,0);

        if(roomNumber == 0) {
            viewModel = new ViewModelProvider(this).get(RoomActivityViewModel.class);
        }
        else {
            viewModel = new ViewModelProvider(this, new RoomActivityFactory(roomNumber)).get(RoomActivityViewModel.class);
        }

        initialise();

        viewModel.getRoom().observe(this, new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                roomNumberText.setText(String.valueOf(room.getRoomNumber()));
                diceRemainingText.setText(String.valueOf(room.getDice()));
                playersText.setText(String.valueOf(room.getPlayers()));
                if(room.getCurrentGameState() == Room.GameState.ShakeTheDice) {
                    rollDiceButton.setEnabled(true);
                }
                else {
                    rollDiceButton.setEnabled(false);
                }
            }
        });

        leaveRoomButton.setOnClickListener(view -> leaveRoom());
        loseRoundButton.setOnClickListener(view -> lostRound());
        rollDiceButton.setOnClickListener(view -> rollDice());

    }

    private void initialise() {
        roomNumberText = findViewById(R.id.roomNumberText);
        playersText = findViewById(R.id.playersText);
        diceRemainingText = findViewById(R.id.diceRemainingText);
        shakeHelpText = findViewById(R.id.shakeHelpText);
        leaveRoomButton = findViewById(R.id.leaveRoomButton);
        rollDiceButton = findViewById(R.id.rollDiceButton);
        loseRoundButton = findViewById(R.id.loseRoundButton);
        dice1Image = findViewById(R.id.dice1Image);
        dice2Image = findViewById(R.id.dice2Image);
        dice3Image = findViewById(R.id.dice3Image);
        dice4Image = findViewById(R.id.dice4Image);
        dice5Image = findViewById(R.id.dice5Image);
        dice6Image = findViewById(R.id.dice6Image);
    }

    private void leaveRoom() {
        viewModel.leaveRoom(numberOfDice);
        finish();
    }

    private void lostRound() {
        numberOfDice--;
        viewModel.playerLostRound();
    }

    private void rollDice() {
        rollDiceButton.setEnabled(false);

        dice1Image.setImageResource(viewModel.getRandomDice());
    }


}