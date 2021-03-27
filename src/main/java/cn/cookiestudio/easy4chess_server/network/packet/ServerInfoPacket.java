package cn.cookiestudio.easy4chess_server.network.packet;

import cn.cookiestudio.easy4chess_server.Server;

public class ServerInfoPacket extends Packet{

    private int onlineCount;

    private String motd;

    public ServerInfoPacket(Server server){
        this.pid = 3;
        this.motd = server.getServerSets().getString("motd");
        this.onlineCount = server.getPlayers().size();
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public String getMotd() {
        return motd;
    }
}
