package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetSocketAddress;

public class RequestServerInfoPacket extends Packet{

    public RequestServerInfoPacket(InetSocketAddress socketAddress){
        this.pid = 2;
        this.address = socketAddress.getAddress().getHostName();
        this.port = socketAddress.getPort();
    }
}
