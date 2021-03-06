package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class LoginStatePacket extends Packet{

    @JsonSerialize
    private LoginStateEnum state;

    public LoginStatePacket(LoginStateEnum state){
        this.pid = 1;
        this.state = state;
    }

    public LoginStatePacket() {
        this.pid = 1;
    }

    public LoginStateEnum getState(){
        return this.state;
    }
    public enum LoginStateEnum{
        SUCCESS,
        NO_INFO,
        WRONG_PASSWORD,
    }
}
