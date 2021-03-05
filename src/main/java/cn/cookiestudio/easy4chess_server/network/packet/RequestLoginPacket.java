package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RequestLoginPacket extends Packet{

    @JsonDeserialize
    private String userName;
    @JsonDeserialize
    private String password;

    public RequestLoginPacket(){
        this.pid = 0;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
