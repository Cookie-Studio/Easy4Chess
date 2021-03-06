package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.network.listener.Cancellable;
import cn.cookiestudio.easy4chess_server.network.listener.PacketException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public abstract class Packet{

    @JsonSerialize
    @JsonDeserialize
    protected int pid;

    protected boolean isCancelled = false;

    public int getPid(){
        return this.pid;
    }

    public boolean isCancelled() {
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
