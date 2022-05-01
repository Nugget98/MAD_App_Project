package dk.au.mad22spring.app.project.liarsdice.Utilities;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import dk.au.mad22spring.app.project.liarsdice.Models.Room;

public class RealtimeDatabaseUtil {

    public static final int NumberOfDice = 6;
    private static final String TAG = "RealtimeDatabaseUtil";

    private FirebaseDatabase realtimeDatabase = FirebaseDatabase.getInstance("https://liar-s-dice-da444-default-rtdb.europe-west1.firebasedatabase.app");
    private DatabaseReference roomRef;
    private Room room = new Room();
    private Boolean newGame;

    private static RealtimeDatabaseUtil instance = null;

    public static RealtimeDatabaseUtil getInstance()
    {
        if (instance == null) {
            instance = new RealtimeDatabaseUtil();
        }
        return instance;
    }

    public static RealtimeDatabaseUtil getInstance(int roomNumber)
    {
        if (instance == null) {
            instance = new RealtimeDatabaseUtil(roomNumber);
        }
        return instance;
    }

    private RealtimeDatabaseUtil() {
        int roomNumber = generateRandomRoomNumber();
        roomRef = realtimeDatabase.getReference(String.valueOf(roomNumber));

        room.setRoomNumber(roomNumber);
        room.setDice(6);
        room.setPlayers(1);

        roomRef.setValue(room);

        newGame = true;
        addValueEventListener();
    }

    private RealtimeDatabaseUtil(int roomNumber) {
        roomRef = realtimeDatabase.getReference(String.valueOf(roomNumber));
        newGame = false;
        addValueEventListener();
    }

    public DatabaseReference getRoomRef() {
        return roomRef;
    }

    private int generateRandomRoomNumber() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    private void addOnePlayerToRoom() {
        int numberOfPlayers = room.getPlayers();
        room.setPlayers(++numberOfPlayers);

        int dice = room.getDice();
        room.setDice(dice += NumberOfDice);
    }

    private void playerLostRound() {
        room.setCurrentGameState(Room.GameState.ShakeTheDice);
    }

    private void leaveRoom() {
        int numberOfPlayers = room.getPlayers();
        room.setPlayers(--numberOfPlayers);

        int dice = room.getDice();
        room.setDice(dice -= NumberOfDice);

        roomRef.setValue(room);
    }

    private void addValueEventListener() {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                room = dataSnapshot.getValue(Room.class);
                if(room == null) {
                    Log.d(TAG, "No Room Found");
                }
                else {
                    if(!newGame) {
                        addOnePlayerToRoom();
                        roomRef.setValue(room);
                        newGame = true;
                    }
                    Log.d(TAG, "Value is: " + room.getRoomNumber());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
