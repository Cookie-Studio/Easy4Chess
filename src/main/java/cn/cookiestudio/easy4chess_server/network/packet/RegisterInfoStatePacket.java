package cn.cookiestudio.easy4chess_server.network.packet;


public class RegisterInfoStatePacket extends Packet {

    private RegisterInfoStateEnum state;

    public RegisterInfoStatePacket(RegisterInfoStateEnum state){
        this.pid = 6;
        this.state = state;
    }

    public RegisterInfoStatePacket() {
        this.pid = 6;
    }

    public RegisterInfoStateEnum getState() {
        return state;
    }

    public enum RegisterInfoStateEnum{
        SUCCESS,
        ALREADY_EXIST,
    }
}
