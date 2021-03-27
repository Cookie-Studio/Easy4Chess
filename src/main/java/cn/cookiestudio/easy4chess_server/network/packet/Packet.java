package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.network.listener.Cancellable;
import cn.cookiestudio.easy4chess_server.network.listener.PacketException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public abstract class Packet{

    protected int pid;
    protected String address;
    protected int port;
    protected transient boolean cancelled = false;

    public InetSocketAddress getInetSocketAddress() {
        try {
            return new InetSocketAddress(InetAddress.getByName(this.address),this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setInetSocketAddress(InetSocketAddress address){
        this.address = address.getAddress().getHostAddress();
        this.port = address.getPort();
    }
    public void setInetSocketAddress(InetAddress address,int port){
        this.address = address.getHostAddress();
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPid(){
        return this.pid;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled() {
        setCancelled(true);
    }

    public void setCancelled(boolean value) {
        if (!(this instanceof Cancellable)) {
            throw new PacketException("Packet is not Cancellable");
        }
        cancelled = value;
    }
}
