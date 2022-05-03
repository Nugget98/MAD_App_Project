package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.Models.Room;
import dk.au.mad22spring.app.project.liarsdice.R;
import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;

public class RoomActivityViewModel extends ViewModel {

    private static final int min = 1;
    private static final int max = 6;

    private int numberOfDice;
    private boolean lostRound;

    private RealtimeDatabaseUtil realtimeDatabaseUtil;

    public RoomActivityViewModel(){
        realtimeDatabaseUtil = new RealtimeDatabaseUtil();
    }

    public RoomActivityViewModel(int roomNumber) {
        realtimeDatabaseUtil = new RealtimeDatabaseUtil(roomNumber);
    }

    public void setLostRound(boolean lostRound) {
        this.lostRound = lostRound;
    }

    public boolean getLostRound() {
        return lostRound;
    }

    public void loseOneDice() {
        numberOfDice--;
    }

    public void resetGame() {
        lostRound = false;
        numberOfDice = Room.StartNumberOfDice;
    }

    public int getNumberOfDice() {
        return numberOfDice;
    }

    public MutableLiveData<Room> getRoom () {
        return realtimeDatabaseUtil.getRoom();
    }

    public void leaveRoom(int numberOfDice) {
        realtimeDatabaseUtil.leaveRoom(numberOfDice);
    }

    public void playerLostRound() {
        lostRound = true;
        realtimeDatabaseUtil.playerLostRound();
    }

    public void startGame() {
        realtimeDatabaseUtil.setGameState(Room.GameState.Started);
    }

    public int getRandomDice() {
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

}
