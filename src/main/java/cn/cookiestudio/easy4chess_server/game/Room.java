package cn.cookiestudio.easy4chess_server.game;

import cn.cookiestudio.easy4chess_server.player.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {

    private Player[] players;
    private RoomState state = RoomState.WAITING;

    public Room(Player[] players){
        if(players.length > 1)
            throw new RuntimeException("players size is out of bound!");
        this.players = players;
    }




    enum RoomState{
        WAITING,
        PLAYING,
        OVER
    }
}
