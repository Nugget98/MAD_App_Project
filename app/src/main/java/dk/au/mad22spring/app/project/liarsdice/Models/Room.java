package dk.au.mad22spring.app.project.liarsdice.Models;


import java.util.ArrayList;

public class Room {

    public static final int StartNumberOfDice = 6;

    public enum GameState {
        WaitingForPlayers,
        ShakeTheDice,
        Started
    }

    private int RoomNumber;
    private int Players = 0;
    private int Dice = 0;
    private int PlayersLeftInGame = 0;
    private GameState CurrentGameState = GameState.WaitingForPlayers;
    private ArrayList<String> playersInRoom = new ArrayList<>();

    public void setPlayersLeftInGame(int playersLeftInGame) {
        PlayersLeftInGame = playersLeftInGame;
    }

    public int getPlayersLeftInGame() {
        return PlayersLeftInGame;
    }

    public ArrayList<String> getPlayersInRoom() {
        return playersInRoom;
    }

    public void addOneToPlayersInRoom(String playerName) {
        playersInRoom.add(playerName);
    }

    public void removePlayer(String playerName) {
        playersInRoom.remove(playerName);
    }

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

    public GameState getCurrentGameState() {
        return CurrentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        CurrentGameState = currentGameState;
    }
}
