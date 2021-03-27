package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetSocketAddress;

public class RegisterInfoPacket extends Packet{

    private String userName;

    private String password;

    public RegisterInfoPacket(String userName, String password, InetSocketAddress socketAddress){
        this.pid = 5;
        this.userName = userName;
        this.password = password;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }



    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
