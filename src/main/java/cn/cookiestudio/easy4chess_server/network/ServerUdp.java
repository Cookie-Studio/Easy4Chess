package cn.cookiestudio.easy4chess_server.network;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.network.packet.Packet;
import cn.cookiestudio.easy4chess_server.network.packet.PidInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
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
                    String str = new String(udpPacket.getData());
                    Server.getInstance().getLogger().info("Receive a packet: " + str);
                    JsonReader jsonReader = new JsonReader(new StringReader(str));
                    jsonReader.setLenient(true);
                    JsonObject jsonObject = new JsonParser().parse(jsonReader).getAsJsonObject();
                    if (!jsonObject.has("pid"))
                        continue;
                    if (!PidInfo.getPidMap().containsKey(jsonObject.get("pid").getAsInt()))
                        continue;
                    jsonReader = new JsonReader(new StringReader(str));//只能有效调用一次。
                    jsonReader.setLenient(true);
                    packet = Server.getGSON().fromJson(jsonReader,PidInfo.getPidMap().get(jsonObject.get("pid").getAsInt()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Server.getInstance().getListenerManager().callAllListener(packet);
            }
        }
    }

    public synchronized DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public Thread getThread() {
        return thread;
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
        this.thread.interrupt();
        this.udpSocket.close();
    }
}
