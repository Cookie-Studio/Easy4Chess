package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RegisterInfoPacket extends Packet{

    @JsonSerialize
    @JsonDeserialize
    private String userName;

    @JsonSerialize
    @JsonDeserialize
    private String password;

    @JsonSerialize
    @JsonDeserialize
    private String address;

    @JsonSerialize
    @JsonDeserialize
    private int port;

    public RegisterInfoPacket(String userName, String password, InetSocketAddress socketAddress){
        this.pid = 5;
        this.userName = userName;
        this.password = password;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }

    public RegisterInfoPacket(){
        this.pid = 5;
    }

    public InetSocketAddress getAddress() {
        try {
            return new InetSocketAddress(InetAddress.getByName(this.address),this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
