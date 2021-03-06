package cn.cookiestudio.easy4chess_server.user;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.utils.Config;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class UserDataConfig {
    private Config config;

    public UserDataConfig(){
        loadUserData();
    }

    public void writeUserData(User user){
        HashMap map = new HashMap();
        map.put("win-count",user.getWinCount());
        map.put("lose-count",user.getLoseCount());
        map.put("password",user.getPassword());
        this.config.set(user.getUserName(),map);
        this.config.save();
    }

    public boolean containUser(String user){
        return this.config.exists(user);
    }

    public boolean verifyPassword(String user, String password){
        return this.config.getString(user + ".password").equals(password);
    }

    private void loadUserData(){
        Path ymlPath = Paths.get(Server.getInstance().getServerPath().toString(), "userdata.yml");
        if (!Files.exists(ymlPath)){
            Server.getInstance().getLogger().error("Can't find userdata.yml,creating new file....");
            try {
                Files.copy(Server.class.getClassLoader().getResourceAsStream("userdata.yml"),ymlPath);
            } catch (IOException e) {
                e.printStackTrace();
                Server.getInstance().getLogger().fatal("Can't create file,server will crash...");
                Server.getInstance().stop(1);
            }
        }
        config = new Config(ymlPath);
    }
}
