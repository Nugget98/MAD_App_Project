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

    private RealtimeDatabaseUtil realtimeDatabaseUtil;

    public RoomActivityViewModel(){
        realtimeDatabaseUtil = RealtimeDatabaseUtil.getInstance();
    }

    public RoomActivityViewModel(int roomNumber) {
        realtimeDatabaseUtil = RealtimeDatabaseUtil.getInstance(roomNumber);
    }

    public MutableLiveData<Room> getRoom () {
        return realtimeDatabaseUtil.getRoom();
    }

    public void leaveRoom(int numberOfDice) {
        realtimeDatabaseUtil.leaveRoom(numberOfDice);
    }

    public void playerLostRound() {
        realtimeDatabaseUtil.playerLostRound();
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
