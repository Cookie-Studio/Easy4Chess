package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.Server;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ServerInfoPacket extends Packet{

    @JsonSerialize
    private int onlineCount;
    @JsonSerialize
    private String motd;

    public ServerInfoPacket(Server server){
        this.pid = 3;
        this.motd = server.getServerSets().getString("motd");
        this.onlineCount = server.getUsers().size();
    }

    public ServerInfoPacket(){
        this.pid = 3;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public String getMotd() {
        return motd;
    }
}
