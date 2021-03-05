package cn.cookiestudio.easy4chess_server.network.packet;

import java.util.HashMap;

public final class PidInfo {
    private static HashMap<Integer,Class<? extends Packet>> pid = new HashMap<>();
    static{
        pid.put(0,RequestLoginPacket.class);
    }

    public static HashMap<Integer,Class<? extends Packet>> getPidMap(){
        return pid;
    }
}
