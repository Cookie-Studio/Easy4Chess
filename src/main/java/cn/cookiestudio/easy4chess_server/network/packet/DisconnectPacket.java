package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.player.Player;

public class DisconnectPacket extends Packet {

    private Player player;

    public DisconnectPacket(Player player) {
        this.pid = 4;
        this.player = player;
    }

    public Player getUser() {
        return player;
    }
}
