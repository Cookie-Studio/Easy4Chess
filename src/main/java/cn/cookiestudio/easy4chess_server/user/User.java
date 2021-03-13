package cn.cookiestudio.easy4chess_server.user;

import cn.cookiestudio.easy4chess_server.Server;

import java.net.InetSocketAddress;

public class User {

    private String userName;
    private String password;
    private int winCount;
    private int loseCount;
    private InetSocketAddress address;
    private int level;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getWinCount() {
        return winCount;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public User(String userName, String password, InetSocketAddress address){
        this.userName = userName;
        this.password = password;
        this.address = address;
        UserDataConfig userData = Server.getInstance().getUserData();
        if (Server.getInstance().getUserData().containUser(userName)){
            this.level = (int) userData.getKey(this.userName + ".level");
            this.loseCount = (int) userData.getKey(this.userName + ".lose-count");
            this.winCount = (int) userData.getKey(this.userName + ".win-count");
        }
    }

    public void sendData(byte[] data){
        Server.getInstance().getServerUdp().sendData(data, this.address);
    }

    public void setLevel(int level) {
        this.level = level;
        Server.getInstance().getUserData().writeKey(this.userName + ".level",level);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLoseCount(int count) {
        this.loseCount = loseCount;
        Server.getInstance().getUserData().writeKey(this.userName + ".lose-count",count);
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
        Server.getInstance().getUserData().writeKey(this.userName + ".win-count",winCount);
    }

    public void setUserName(String userName){
        UserDataConfig userDataConfig = Server.getInstance().getUserData();
        userDataConfig.getConfig().set(userName,userDataConfig.getKey(this.userName));
        userDataConfig.getConfig().remove(this.userName);
        userDataConfig.getConfig().save();
        this.userName = userName;
    }
}
