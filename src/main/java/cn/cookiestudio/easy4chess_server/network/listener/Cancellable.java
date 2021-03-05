package cn.cookiestudio.easy4chess_server.network.listener;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled();

    void setCancelled(boolean isCancel);
}
