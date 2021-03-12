package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RegisterInfoPacket extends Packet{

    private String userName;

    private String password;

    private InetSocketAddress address;

    public RegisterInfoPacket(String userName, String password, InetSocketAddress address){
        this.pid = 5;
        this.userName = userName;
        this.password = password;
        this.address = address;
    }

    public RegisterInfoPacket(){
        this.pid = 5;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
