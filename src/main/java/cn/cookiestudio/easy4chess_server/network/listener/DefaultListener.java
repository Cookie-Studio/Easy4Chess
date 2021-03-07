package cn.cookiestudio.easy4chess_server.network.listener;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.network.packet.*;
import cn.cookiestudio.easy4chess_server.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.DatagramPacket;

public class DefaultListener implements Listener{
    @PacketHandler
    public void onRequestLogin(RequestLoginPacket packet) throws IOException {
        Server.getInstance().getLogger().info("Received a RequestLoginPacket");
        User user = new User(packet.getUserName(),packet.getPassword(),packet.getAddress());
        //check if exist
        if (!Server.getInstance().getUserData().containUser(packet.getUserName())) {
            byte[] b = Server.getJsonMapper().writeValueAsBytes(new LoginStatePacket(LoginStatePacket.LoginStateEnum.NO_INFO));
            Server.getInstance().getServerUdp().getUdpSocket().send(new DatagramPacket(b,0,b.length,user.getAddress()));
        }
        //check password
        if (!Server.getInstance().getUserData().verifyPassword(user.getUserName(),user.getPassword())){
            byte[] b = Server.getJsonMapper().writeValueAsBytes(new LoginStatePacket(LoginStatePacket.LoginStateEnum.WRONG_PASSWORD));
            Server.getInstance().getServerUdp().getUdpSocket().send(new DatagramPacket(b,0,b.length,user.getAddress()));
        }

        Server.getInstance().addUser(user);
        //send success packet
        byte[] b = Server.getJsonMapper().writeValueAsBytes(new LoginStatePacket(LoginStatePacket.LoginStateEnum.SUCCESS));
        Server.getInstance().getServerUdp().getUdpSocket().send(new DatagramPacket(b,0,b.length,user.getAddress()));
    }

    @PacketHandler
    public void onRequestServerInfo(RequestServerInfoPacket packet) throws IOException {
        Server.getInstance().getLogger().info("Received a RequestServerInfoPacket");
        ServerInfoPacket packet1 = new ServerInfoPacket(Server.getInstance());
        Server.getInstance().getServerUdp().sendData(Server.getJsonMapper().writeValueAsBytes(packet1),packet.getAddress());
    }

    @PacketHandler
    public void onDisconnect(DisconnectPacket packet){
        Server.getInstance().getLogger().info("Received a DisconnectPacket");
        //for future code...

        //remove user
        Server.getInstance().removeUser(packet.getUser());
    }

    @PacketHandler
    public void onRegisterInfo(RegisterInfoPacket packet) throws JsonProcessingException {
        Server.getInstance().getLogger().info("Received a RegisterInfoPacket");
        RegisterInfoStatePacket result = new RegisterInfoStatePacket(Server.getInstance().getUserData().registerUserInfo(packet.getUserName(),packet.getPassword()));
        Server.getInstance().getServerUdp().sendData(Server.getJsonMapper().writeValueAsBytes(result),packet.getAddress());
    }
}
