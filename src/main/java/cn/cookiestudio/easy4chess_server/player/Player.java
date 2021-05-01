package cn.cookiestudio.easy4chess_server.player;

import cn.cookiestudio.easy4chess_server.Server;
import java.net.InetSocketAddress;

public class Player {

    private String userName;
    private String password;
    private int winningCount;
    private int losingCount;
    private InetSocketAddress address;
    private int level;
    private boolean isPlaying = false;

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public String getPlayerName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getWinningCount() {
        return winningCount;
    }

    public int getLosingCount() {
        return losingCount;
    }

    public int getTotalPlayingCount(){
        return winningCount + losingCount;
    }

    public double getWinRate() {
        return (winningCount / (winningCount + losingCount)) * 100;
    }

    public Player(String playerName, String password, InetSocketAddress address){
        this.userName = playerName;
        this.password = password;
        this.address = address;
        PlayerDataConfig userData = Server.getInstance().getUserData();
        if (Server.getInstance().getUserData().containPlayer(playerName)){
            this.level = (int) userData.get(this.userName + ".level");
            this.losingCount = (int) userData.get(this.userName + ".lose-count");
            this.winningCount = (int) userData.get(this.userName + ".win-count");
        }
    }

    public void sendData(byte[] data){
        Server.getInstance().getServerUdp().sendData(data, this.address);
    }

    public void setLevel(int level) {
        this.level = level;
        Server.getInstance().getUserData().write(this.userName + ".level",level);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLosingCount(int count) {
        this.losingCount = losingCount;
        Server.getInstance().getUserData().write(this.userName + ".lose-count",count);
    }

    public void setWinningCount(int winningCount) {
        this.winningCount = winningCount;
        Server.getInstance().getUserData().write(this.userName + ".win-count", winningCount);
    }

    public void setPlayerName(String userName){
        PlayerDataConfig playerDataConfig = Server.getInstance().getUserData();
        playerDataConfig.getConfig().set(userName, playerDataConfig.get(this.userName));
        playerDataConfig.getConfig().remove(this.userName);
        playerDataConfig.getConfig().save();
        this.userName = userName;
    }
}
