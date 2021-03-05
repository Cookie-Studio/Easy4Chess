package cn.cookiestudio.easy4chess_server.network;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.network.packet.Packet;
import cn.cookiestudio.easy4chess_server.network.packet.PidInfo;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerUdp {
    private Thread thread;
    private DatagramSocket udpSocket;

    public ServerUdp(InetSocketAddress address) throws SocketException {
        this.udpSocket = new DatagramSocket(address);
        this.thread = new Thread(new Runnable());
    }

    public void start(){
        this.thread.start();
    }

    private class Runnable implements java.lang.Runnable{
        @Override
        public void run() {
            while(!thread.isInterrupted()){
                byte[] buffer = new byte[512];
                DatagramPacket udpPacket = new DatagramPacket(buffer,0,512);
                Packet packet = null;
                try {
                    ServerUdp.this.udpSocket.receive(udpPacket);
                    JsonNode jsonNode = Server.getJsonMapper().readTree(udpPacket.getData());
                    if (!jsonNode.has("pid"))
                        continue;
                    if (!PidInfo.getPidMap().containsKey(jsonNode.get("pid")))
                        continue;
                    packet = Server.getJsonMapper().readValue(udpPacket.getData(), PidInfo.getPidMap().get(jsonNode.get("pid")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Server.getInstance().getListenerManager().callAllListener(packet);
            }
        }
    }
}
