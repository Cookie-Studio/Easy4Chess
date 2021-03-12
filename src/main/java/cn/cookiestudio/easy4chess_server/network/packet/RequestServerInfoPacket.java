package cn.cookiestudio.easy4chess_server.network.packet;

import java.net.InetSocketAddress;

public class RequestServerInfoPacket extends Packet{

    private InetSocketAddress address;

    public RequestServerInfoPacket(InetSocketAddress socketAddress){
        this.pid = 2;
        this.address = socketAddress;
    }

    public RequestServerInfoPacket(){
        this.pid = 2;
    }

    public InetSocketAddress getAddress() {
        return this.address;
    }
}
