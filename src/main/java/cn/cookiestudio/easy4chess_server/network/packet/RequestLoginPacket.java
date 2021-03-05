package cn.cookiestudio.easy4chess_server.network.packet;

public class RequestLoginPacket extends Packet{

    protected int pid = 0;
    private String userName;
    private String password;

    public RequestLoginPacket(){

    }
}
