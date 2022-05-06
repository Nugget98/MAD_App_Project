package dk.au.mad22spring.app.project.liarsdice.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

//Class inspired of answer on Stack Overflow post:
//https://stackoverflow.com/questions/46283981/android-viewmodel-additional-arguments
public class RoomActivityFactory implements ViewModelProvider.Factory {
    private int roomNumber;

    public RoomActivityFactory(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RoomActivityViewModel(roomNumber);
    }
}
