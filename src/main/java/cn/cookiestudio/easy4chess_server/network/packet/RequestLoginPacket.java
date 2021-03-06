package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RequestLoginPacket extends Packet{

    @JsonDeserialize
    private String userName;
    @JsonDeserialize
    private String password;
    @JsonDeserialize
    private String address;
    @JsonDeserialize
    private int port;

    public RequestLoginPacket(String userName,String passWord,InetSocketAddress socketAddress){
        this.pid = 0;
        this.userName = userName;
        this.password = passWord;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }

    public RequestLoginPacket(){
        this.pid = 0;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
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
