package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.app.project.liarsdice.Utilities.RealtimeDatabaseUtil;

public class RoomActivityViewModel extends ViewModel {

    private RealtimeDatabaseUtil realtimeDatabaseUtil;

    public RoomActivityViewModel(){
        realtimeDatabaseUtil = RealtimeDatabaseUtil.getInstance();
    }

    public RoomActivityViewModel(int roomNUmber) {
        realtimeDatabaseUtil = RealtimeDatabaseUtil.getInstance(roomNUmber);
    }
}
