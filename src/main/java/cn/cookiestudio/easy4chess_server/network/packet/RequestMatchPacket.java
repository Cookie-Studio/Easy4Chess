package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.player.Player;

public class RequestMatchPacket extends Packet{

    private Player requester = null;

    public RequestMatchPacket(Player player){
        this.pid = 7;
        this.requester = player;
    }
}
