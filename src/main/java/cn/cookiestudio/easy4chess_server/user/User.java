package cn.cookiestudio.easy4chess_server.user;

public class User {

    private String userName;
    private String password;
    private int winCount;
    private int loseCount;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
