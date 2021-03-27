package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetSocketAddress;

public class RequestLoginPacket extends Packet{

    private String userName;

    private String password;

    public RequestLoginPacket(String userName,String passWord,InetSocketAddress socketAddress){
        this.pid = 0;
        this.userName = userName;
        this.password = passWord;
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
