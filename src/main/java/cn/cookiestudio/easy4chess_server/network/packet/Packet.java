package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.network.listener.Cancellable;
import cn.cookiestudio.easy4chess_server.network.listener.PacketException;

public abstract class Packet{

    protected int pid;
    protected boolean isCancelled = false;

    public int getPid(){
        return this.pid;
    }

    public boolean isCancelled() {
        if (!(this instanceof Cancellable)) {
            throw new PacketException("Packet is not Cancellable");
        }
        return isCancelled;
    }

    public void setCancelled() {
        setCancelled(true);
    }

    public void setCancelled(boolean value) {
        if (!(this instanceof Cancellable)) {
            throw new PacketException("Packet is not Cancellable");
        }
        isCancelled = value;
    }
}
