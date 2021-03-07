package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class RegisterInfoStatePacket extends Packet {

    @JsonSerialize
    @JsonDeserialize
    private RegisterInfoStateEnum state;

    public RegisterInfoStatePacket(){
        this.pid = 6;
    }

    public RegisterInfoStatePacket(RegisterInfoStateEnum state){
        this.pid = 6;
        this.state = state;
    }

    public RegisterInfoStateEnum getState() {
        return state;
    }

    public enum RegisterInfoStateEnum{
        SUCCESS,
        ALREADY_EXIST,
    }
}
