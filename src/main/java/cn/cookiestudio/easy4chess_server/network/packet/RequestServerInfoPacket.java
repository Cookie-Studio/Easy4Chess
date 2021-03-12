package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RequestServerInfoPacket extends Packet{

    private String address;

    private int port;

    public RequestServerInfoPacket(InetSocketAddress socketAddress){
        this.pid = 2;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }

    public RequestServerInfoPacket() {
        this.pid = 2;
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
