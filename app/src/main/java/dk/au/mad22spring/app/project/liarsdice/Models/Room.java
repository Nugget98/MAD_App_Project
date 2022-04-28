package dk.au.mad22spring.app.project.liarsdice.Models;

public class Room {

    private int RoomNumber;
    private int Players = 0;
    private int Dice = 0;

    public int getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        RoomNumber = roomNumber;
    }

    public int getPlayers() {
        return Players;
    }

    public void setPlayers(int players) {
        Players = players;
    }

    public int getDice() {
        return Dice;
    }

    public void setDice(int dice) {
        Dice = dice;
    }
}
