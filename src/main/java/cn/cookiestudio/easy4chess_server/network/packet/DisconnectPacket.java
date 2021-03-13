package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.user.User;

public class DisconnectPacket extends Packet {

    private User user;

    public DisconnectPacket(User user) {
        this.pid = 4;
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
