package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.LiarsDiceApplication;
import dk.au.mad22spring.app.project.liarsdice.Models.Room;
import dk.au.mad22spring.app.project.liarsdice.Models.StaticUser;
import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.FirestoreUtil;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;

public class RoomActivityViewModel extends ViewModel {

    private static final String TAG = "RoomActivityViewModel";

    private static final int min = 1;
    private static final int max = 6;

    private int numberOfDice = Room.StartNumberOfDice;
    private boolean lostRound;
    private boolean rollDiceButtonEnabled;
    private boolean loseRoundButtonEnabled;
    private int startButtonVisible;

    private Room currentRoom;

    private androidx.lifecycle.Observer<Room> observer;

    private RealtimeDatabaseUtil realtimeDatabaseUtil;

    private final MutableLiveData<Room> newRoom = new MutableLiveData<>();

    private ArrayList<Integer> diceRolled = new ArrayList<>();

    private int checkRoomNumber = 0;

    public RoomActivityViewModel() {
        realtimeDatabaseUtil = new RealtimeDatabaseUtil();
        startButtonVisible = View.VISIBLE;
        handleGameState();
    }

    public RoomActivityViewModel(int roomNumber) {
        checkRoomNumber = roomNumber;
        realtimeDatabaseUtil = new RealtimeDatabaseUtil(roomNumber);
        startButtonVisible = View.INVISIBLE;
        handleGameState();
    }

    private void handleGameState() {
        //Inspired from https://stackoverflow.com/questions/48396092/should-i-include-lifecycleowner-in-viewmodel
        observer = new androidx.lifecycle.Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                currentRoom = room;

                switch (room.getCurrentGameState()) {
                    case ShakeTheDice:
                        if (room.getDice() == numberOfDice) {
                            Toast.makeText(LiarsDiceApplication.getAppContext(), "You lost the game", Toast.LENGTH_SHORT).show();
                            FirestoreUtil firestoreUtil = FirestoreUtil.getFirestore();
                            StaticUser.staticUser.Loses = String.valueOf(Integer.parseInt(StaticUser.staticUser.Loses) + 1);
                            firestoreUtil.updateStats(StaticUser.staticUser);
                            startNextGame();
                        } else {
                            rollDiceButtonEnabled = true;
                            loseRoundButtonEnabled = false;
                            if (!lostRound) {
                                loseOneDice();
                                Log.d(TAG, String.valueOf(numberOfDice));
                            }
                            lostRound = false;
                        }
                        break;
                    case Finish:
                        loseRoundButtonEnabled = false;
                        rollDiceButtonEnabled = true;
                        break;
                    case Started:
                        Toast.makeText(LiarsDiceApplication.getAppContext(), "Game started", Toast.LENGTH_SHORT).show();
                        Log.d("STARTLOG", "started called");
                        StaticUser.staticUser.TotalGames = String.valueOf(Integer.parseInt(StaticUser.staticUser.TotalGames) + 1);
                        rollDiceButtonEnabled = true;
                        //add one game to the player in the database
                        break;
                    case WaitingForPlayers:
                        loseRoundButtonEnabled = false;
                        rollDiceButtonEnabled = false;
                        if(checkRoomNumber == 0) {
                            startButtonVisible = View.VISIBLE;
                        }
                        break;
                }

                newRoom.setValue(currentRoom);
            }
        };

        realtimeDatabaseUtil.getRoom().observeForever(observer);
    }

    public MutableLiveData<Room> getNewRoom() {
        return newRoom;
    }

    //Inspired from https://stackoverflow.com/questions/26139115/not-able-to-dynamically-set-the-setvisibility-parameter
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    public @interface Visibility {
    }

    public @Visibility
    int getStartButtonVisible() {
        return startButtonVisible;
    }

    public boolean getRollDiceButtonEnabled() {
        return rollDiceButtonEnabled;
    }

    public void setStartButtonVisible(int startButtonVisible) {
        this.startButtonVisible = startButtonVisible;
    }

    public boolean getLoseRoundButtonEnabled() {
        return loseRoundButtonEnabled;
    }

    public void setRollDiceButtonEnabled(boolean rollDiceButtonEnabled) {
        this.rollDiceButtonEnabled = rollDiceButtonEnabled;
    }

    public void setLoseRoundButtonEnabled(boolean loseRoundButtonEnabled) {
        this.loseRoundButtonEnabled = loseRoundButtonEnabled;
    }

    private void loseOneDice() {
        numberOfDice--;
        if(numberOfDice == 0) {
            realtimeDatabaseUtil.onePlayerFinish();
        }
    }

    public void startGame() {
        lostRound = false;
        numberOfDice = Room.StartNumberOfDice;
        realtimeDatabaseUtil.resetNumberOfDiceInGame();
    }

    public void startNextGame() {
        realtimeDatabaseUtil.setGameState(Room.GameState.WaitingForPlayers);
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    public void leaveRoom() {
        realtimeDatabaseUtil.leaveRoom(numberOfDice);
    }

    public void playerLostRound() {
        lostRound = true;
        realtimeDatabaseUtil.playerLostRound();
    }

    public ArrayList<Integer> getDiceRolled() {
        return diceRolled;
    }

    public void roleDice() {
        diceRolled.clear();
        for (int i = 0; i < 6; i++) {
            diceRolled.add(getRandomDice());
        }
    }

    private int getRandomDice() {
        int random = new Random().nextInt((max - min) + 1) + min;

        switch (random) {
            case 1:
                return R.drawable.dice1;
            case 2:
                return R.drawable.dice2;
            case 3:
                return R.drawable.dice3;
            case 4:
                return R.drawable.dice4;
            case 5:
                return R.drawable.dice5;
            case 6:
                return R.drawable.dice6;
            default:
                return 0;
        }
    }

    @Override
    public void onCleared() {
        realtimeDatabaseUtil.getRoom().removeObserver(observer);
        super.onCleared();
    }

}
