package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RegisterInfoPacket extends Packet{

    private String userName;

    private String password;

    private String address;

    private int port;

    public RegisterInfoPacket(String userName, String password, InetSocketAddress socketAddress){
        this.pid = 5;
        this.userName = userName;
        this.password = password;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }

    public InetSocketAddress getAddress() {
        try {
            return new InetSocketAddress(InetAddress.getByName(this.address),this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
