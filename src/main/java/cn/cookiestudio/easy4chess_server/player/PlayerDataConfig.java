package cn.cookiestudio.easy4chess_server.player;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.network.packet.RegisterInfoStatePacket;
import cn.cookiestudio.easy4chess_server.utils.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PlayerDataConfig {
    private Config config;

    public PlayerDataConfig(){
        loadPlayerData();
    }

    public boolean containPlayer(String user){
        return this.config.exists(user);
    }

    public boolean verifyPassword(String user, String password){
        return this.config.getString(user + ".password").equals(password);
    }

    public RegisterInfoStatePacket.RegisterInfoStateEnum writeUserData(String user, String password){
        if (this.config.exists(user))
            return RegisterInfoStatePacket.RegisterInfoStateEnum.ALREADY_EXIST;
        HashMap map = new HashMap();
        map.put("win-count",0);
        map.put("lose-count",0);
        map.put("password",password);
        map.put("level",1);
        this.config.set(user,map);
        this.config.save();
        return RegisterInfoStatePacket.RegisterInfoStateEnum.SUCCESS;
    }

    public void writeKey(String key,Object value){
        this.config.set(key,value);
        this.config.save();
    }

    public Object getKey(String key){
        return this.config.get(key);
    }

    public Config getConfig() {
        return config;
    }

    private void loadPlayerData() {
        Path ymlPath = Paths.get(Server.getInstance().getServerPath().toString(), "userdata.yml");
        if (!Files.exists(ymlPath)) {
            Server.getInstance().getLogger().error("Can't find userdata.yml,creating new file....");
            try {
                Files.copy(Server.class.getClassLoader().getResourceAsStream("userdata.yml"), ymlPath);
            } catch (IOException e) {
                e.printStackTrace();
                Server.getInstance().getLogger().fatal("Can't create file,server will crash...");
                Server.getInstance().stop(1);
            }
        }
        config = new Config(ymlPath);
    }
}
