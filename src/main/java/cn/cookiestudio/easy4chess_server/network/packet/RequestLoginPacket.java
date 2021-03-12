package cn.cookiestudio.easy4chess_server.network.packet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RequestLoginPacket extends Packet{

    private String userName;

    private String password;

    private String address;

    private int port;

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

    public InetSocketAddress getAddress() {
        try {
            return new InetSocketAddress(InetAddress.getByName(this.address),this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
