package dk.au.mad22spring.app.project.liarsdice.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.Models.Room;
import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.RoomActivityFactory;
import dk.au.mad22spring.app.project.liarsdice.ViewModels.RoomActivityViewModel;

public class RoomActivity extends AppCompatActivity {

    private static final String TAG = "RoomActivity";

    private TextView roomNumberText, playersText, diceRemainingText, shakeHelpText;
    private Button leaveRoomButton, rollDiceButton, loseRoundButton, startGameButton;
    private ImageView dice1Image, dice2Image, dice3Image, dice4Image, dice5Image, dice6Image;

    private RoomActivityViewModel viewModel;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        initialise();

        Intent intentFromListActivity = getIntent();
        int roomNumber = intentFromListActivity.getIntExtra(HomeActivity.KEY_ROOM_NUMBER,0);

        if(roomNumber == 0) {
            viewModel = new ViewModelProvider(this).get(RoomActivityViewModel.class);
            startGameButton.setVisibility(View.VISIBLE);
        }
        else {
            viewModel = new ViewModelProvider(this, new RoomActivityFactory(roomNumber)).get(RoomActivityViewModel.class);
            startGameButton.setVisibility(View.INVISIBLE);
        }

        //Inspired from https://stackoverflow.com/a/2184151
        viewModel.getRoom().observe(this, room -> {
            roomNumberText.setText(getString(R.string.RoomNumber) + " " + String.valueOf(room.getRoomNumber()));
            diceRemainingText.setText(getString(R.string.DiceLeft) + " " + String.valueOf(room.getDice()));
            playersText.setText(getString(R.string.PlayersText) + " " + String.valueOf(room.getPlayers()));

            switch (room.getCurrentGameState()) {
                case ShakeTheDice:
                    if(room.getDice() == viewModel.getNumberOfDice()) {
                        Log.d(TAG,"You lost the game");
                        //SAVE LOST GAME IN DATABASE
                        viewModel.startGame();
                    } else {
                        rollDiceButton.setEnabled(true);
                        loseRoundButton.setEnabled(false);
                        if(!viewModel.getLostRound()) {
                            viewModel.loseOneDice();
                            Log.d(TAG,String.valueOf(viewModel.getNumberOfDice()));
                        }
                        viewModel.setLostRound(false);
                    }
                    break;
                case Started:
                    viewModel.resetGame();
                    rollDiceButton.setEnabled(true);
                    //add one game to the player in the database
                    break;
                case WaitingForPlayers:
                    rollDiceButton.setEnabled(false);
                    loseRoundButton.setEnabled(false);
                    break;
            }
        });

        leaveRoomButton.setOnClickListener(view -> leaveRoom());
        loseRoundButton.setOnClickListener(view -> lostRound());
        rollDiceButton.setOnClickListener(view -> rollDice());
        startGameButton.setOnClickListener(view -> startGame());



        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void initialise() {
        roomNumberText = findViewById(R.id.roomNumberText);
        playersText = findViewById(R.id.playersText);
        diceRemainingText = findViewById(R.id.diceRemainingText);
        shakeHelpText = findViewById(R.id.shakeHelpText);
        leaveRoomButton = findViewById(R.id.leaveRoomButton);
        rollDiceButton = findViewById(R.id.rollDiceButton);
        loseRoundButton = findViewById(R.id.loseRoundButton);
        startGameButton = findViewById(R.id.startGameButton);
        dice1Image = findViewById(R.id.dice1Image);
        dice2Image = findViewById(R.id.dice2Image);
        dice3Image = findViewById(R.id.dice3Image);
        dice4Image = findViewById(R.id.dice4Image);
        dice5Image = findViewById(R.id.dice5Image);
        dice6Image = findViewById(R.id.dice6Image);
    }

    private void startGame() {
        viewModel.startGame();
        startGameButton.setVisibility(View.INVISIBLE);
    }

    private void leaveRoom() {
        viewModel.leaveRoom(viewModel.getNumberOfDice());
        finish();
    }

    private void lostRound() {
        loseRoundButton.setEnabled(false);
        viewModel.playerLostRound();
    }

    private void rollDice() {
        loseRoundButton.setEnabled(true);
        rollDiceButton.setEnabled(false);

        switch (viewModel.getNumberOfDice()) {
            case 0:
                dice1Image.setImageResource(0);
                dice2Image.setImageResource(0);
                dice3Image.setImageResource(0);
                dice4Image.setImageResource(0);
                dice5Image.setImageResource(0);
                dice6Image.setImageResource(0);

                loseRoundButton.setEnabled(false);
                break;
            case 1:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(0);
                dice3Image.setImageResource(0);
                dice4Image.setImageResource(0);
                dice5Image.setImageResource(0);
                dice6Image.setImageResource(0);
                break;
            case 2:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(viewModel.getRandomDice());
                dice3Image.setImageResource(0);
                dice4Image.setImageResource(0);
                dice5Image.setImageResource(0);
                dice6Image.setImageResource(0);
                break;
            case 3:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(viewModel.getRandomDice());
                dice3Image.setImageResource(viewModel.getRandomDice());
                dice4Image.setImageResource(0);
                dice5Image.setImageResource(0);
                dice6Image.setImageResource(0);
                break;
            case 4:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(viewModel.getRandomDice());
                dice3Image.setImageResource(viewModel.getRandomDice());
                dice4Image.setImageResource(viewModel.getRandomDice());
                dice5Image.setImageResource(0);
                dice6Image.setImageResource(0);
                break;
            case 5:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(viewModel.getRandomDice());
                dice3Image.setImageResource(viewModel.getRandomDice());
                dice4Image.setImageResource(viewModel.getRandomDice());
                dice5Image.setImageResource(viewModel.getRandomDice());
                dice6Image.setImageResource(0);
                break;
            case 6:
                dice1Image.setImageResource(viewModel.getRandomDice());
                dice2Image.setImageResource(viewModel.getRandomDice());
                dice3Image.setImageResource(viewModel.getRandomDice());
                dice4Image.setImageResource(viewModel.getRandomDice());
                dice5Image.setImageResource(viewModel.getRandomDice());
                dice6Image.setImageResource(viewModel.getRandomDice());
                break;
        }
    }
}