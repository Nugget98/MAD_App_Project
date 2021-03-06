package dk.au.mad22spring.app.project.liarsdice.Utilities;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.LiarsDiceApplication;
import dk.au.mad22spring.app.project.liarsdice.Models.Room;
import dk.au.mad22spring.app.project.liarsdice.Models.StaticUser;

public class RealtimeDatabaseUtil {

    private static final String TAG = "RealtimeDatabaseUtil";

    private FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance("https://liar-s-dice-da444-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference roomRef;
    private Boolean newGame;
    private Boolean deletedRoom = false;
    private final MutableLiveData<Room> room = new MutableLiveData<>();

    public RealtimeDatabaseUtil() {
        int roomNumber = generateRandomRoomNumber();
        roomRef = realtimeDatabase.getReference(String.valueOf(roomNumber));

        Room newRoom = new Room();
        newRoom.setRoomNumber(roomNumber);
        newRoom.setDice(6);
        newRoom.setPlayers(1);

        newRoom.addOneToPlayersInRoom(StaticUser.staticUser.Displayname);

        roomRef.setValue(newRoom);

        newGame = true;
        addValueEventListener();
    }

    public RealtimeDatabaseUtil(int roomNumber) {
        roomRef = realtimeDatabase.getReference(String.valueOf(roomNumber));
        newGame = false;
        addValueEventListener();
    }

    private int generateRandomRoomNumber() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    private void addOnePlayerToRoom() {
        int numberOfPlayers = room.getValue().getPlayers();
        room.getValue().setPlayers(++numberOfPlayers);

        int dice = room.getValue().getDice();
        room.getValue().setDice(dice += Room.StartNumberOfDice);

        room.getValue().addOneToPlayersInRoom(StaticUser.staticUser.Displayname);
    }

    public void onePlayerFinish() {
        int players = room.getValue().getPlayersLeftInGame() - 1;
        room.getValue().setPlayersLeftInGame(players);
        room.getValue().setCurrentGameState(Room.GameState.Finish);
        roomRef.setValue(room.getValue());
    }

    public void setGameState(Room.GameState gameState) {
        room.getValue().setCurrentGameState(gameState);
        roomRef.setValue(room.getValue());
    }

    public void resetNumberOfDiceInGame() {
        room.getValue().setCurrentGameState(Room.GameState.Started);
        room.getValue().setDice(room.getValue().getPlayers() * Room.StartNumberOfDice);
        room.getValue().setPlayersLeftInGame(room.getValue().getPlayers());
        room.getValue().setCurrentGameState(Room.GameState.Started);
        roomRef.setValue(room.getValue());
    }

    public void playerLostRound() {
        room.getValue().setCurrentGameState(Room.GameState.ShakeTheDice);
        int numberOfPlayersLeft = room.getValue().getPlayersLeftInGame();
        int dice = room.getValue().getDice() - numberOfPlayersLeft + 1;
        room.getValue().setDice(dice);

        roomRef.setValue(room.getValue());
    }

    public void leaveRoom(int numberOfDice) {
        int numberOfPlayers = room.getValue().getPlayers();
        room.getValue().setPlayers(--numberOfPlayers);

        int dice = room.getValue().getDice() - numberOfDice;
        room.getValue().setDice(dice);

        room.getValue().removePlayer(StaticUser.staticUser.Displayname);

        roomRef.setValue(room.getValue());
    }

    public MutableLiveData<Room> getRoom() {
        return room;
    }

    private void addValueEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Room newRoom = dataSnapshot.getValue(Room.class);

                if(newRoom == null) {
                    if(!deletedRoom) {
                        Toast.makeText(LiarsDiceApplication.getAppContext(), "No Room Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    deletedRoom = false;
                    if(newRoom.getPlayers() == 0) {
                        deletedRoom = true;
                        dataSnapshot.getRef().removeValue();
                    }
                    else {
                        room.setValue(newRoom);
                        if(!newGame) {
                            addOnePlayerToRoom();
                            roomRef.setValue(room.getValue());
                            newGame = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
