package cn.cookiestudio.easy4chess_server.network.listener;

import cn.cookiestudio.easy4chess_server.network.packet.RequestLoginPacket;

public class DefaultListener implements Listener{
    @PacketHandler
    public void onPlayerRequestLogin(RequestLoginPacket packet){
        
    }
}
