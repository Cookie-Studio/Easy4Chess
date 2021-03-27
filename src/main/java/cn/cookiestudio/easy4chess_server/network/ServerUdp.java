package cn.cookiestudio.easy4chess_server.network;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.network.packet.Packet;
import cn.cookiestudio.easy4chess_server.network.packet.PidInfo;
import cn.cookiestudio.easy4chess_server.scheduler.tasks.PeriodTask;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerUdp {
    private DatagramSocket udpSocket;

    public ServerUdp(InetSocketAddress address) throws SocketException {
        this.udpSocket = new DatagramSocket(address);
        Server.getInstance().getScheduler().schedulerTask(new PeriodTask(new Runnable(),0));
    }

    private class Runnable implements java.lang.Runnable{
        @Override
        public void run() {
            byte[] buffer = new byte[512];
            DatagramPacket udpPacket = new DatagramPacket(buffer,0,512);
            Packet packet = null;
            try {
                ServerUdp.this.udpSocket.receive(udpPacket);
                String str = new String(udpPacket.getData());
                Server.getInstance().getLogger().info("Receive a packet: " + str);
                JsonNode node =Server.getJacksonJsonMapper().readTree(str);
                if (!node.has("pid"))
                    return;
                if (!PidInfo.getPidMap().containsKey(node.get("pid").asInt()))
                    return;
                JsonReader jsonReader = new JsonReader(new StringReader(str));//只能有效调用一次。
                jsonReader.setLenient(true);
                packet = Server.getGSON().fromJson(jsonReader,PidInfo.getPidMap().get(node.get("pid").asInt()));
                packet.setInetSocketAddress(udpPacket.getAddress(),udpPacket.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Server.getInstance().getListenerManager().callPacket(packet);
        }
    }

    public synchronized DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public void sendData(byte[] data, InetSocketAddress address){
        DatagramPacket packet = new DatagramPacket(data,0,data.length,address);
        try {
            this.udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server.getInstance().getLogger().info("Send a data packet,content: " + new String(data));
    }

    public void close(){
        this.udpSocket.close();
    }
}
