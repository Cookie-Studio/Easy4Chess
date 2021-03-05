package cn.cookiestudio.easy4chess_server.network.listener;

public class PacketException extends RuntimeException{

    public PacketException(Exception e) {
        super(e);
    }

    public PacketException(String message) {
        super(message);
    }
}
