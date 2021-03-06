package cn.cookiestudio.easy4chess_server;

import cn.cookiestudio.easy4chess_server.network.ServerUdpReceive;
import cn.cookiestudio.easy4chess_server.network.listener.DefaultListener;
import cn.cookiestudio.easy4chess_server.network.listener.ListenerManager;
import cn.cookiestudio.easy4chess_server.scheduler.Scheduler;
import cn.cookiestudio.easy4chess_server.user.User;
import cn.cookiestudio.easy4chess_server.user.UserDataConfig;
import cn.cookiestudio.easy4chess_server.utils.Config;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Server {

    private static ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static YAMLMapper YAML_MAPPER = new YAMLMapper();

     static{
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_MAPPER.setVisibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NONE);
        JSON_MAPPER.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        JSON_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private static Server instance = null;
    private int serverTick = 20;
    private Path serverPath = Paths.get(System.getProperty("user.dir"));
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private Scheduler scheduler;
    private ListenerManager listenerManager = new ListenerManager();
    private ServerUdpReceive serverUdpReceive;
    private Config serverSets;
    private UserDataConfig userData;
    private InetSocketAddress serverAddress;
    private HashMap<String, User> users = new HashMap<>();
    private DatagramSocket packetSender;
    public static void main(String[] args) {
        new Server();
    }

    public Server(){
        this.logger.info("Server starting...");
        instance = this;
        this.loadServerYml();
        this.userData = new UserDataConfig();
        try {
            this.serverAddress = new InetSocketAddress(InetAddress.getByName((String)this.serverSets.get("ip")), (int)this.serverSets.get("port"));
            this.serverUdpReceive = new ServerUdpReceive(this.serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getLogger().info("Successfully loaded server info");
        this.serverUdpReceive.start();
        this.getLogger().info("Successfully started udp service");
        try {
            this.packetSender = new DatagramSocket(this.serverAddress.getPort() + 1,this.serverAddress.getAddress());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.scheduler = new Scheduler();
        this.scheduler.start();
        this.getLogger().info("Successfully started scheduler");
        this.registerDefaultListener();
        this.getLogger().info("Successfully registered default listener");
    }

    public ListenerManager getListenerManager() {
        return listenerManager;
    }

    public Path getServerPath() {
        return serverPath;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void addUser(User user){
        this.users.put(user.getUserName(),user);
    }

    public boolean removeUser(User user){
        if (this.users.containsKey(user.getUserName())) return false;
        this.users.remove(user.getUserName());
        return true;
    }

    public DatagramSocket getPacketSender() {
        return packetSender;
    }

    public ServerUdpReceive getServerUdpReceive() {
        return serverUdpReceive;
    }

    public UserDataConfig getUserData() {
        return userData;
    }

    public Config getServerSets() {
        return serverSets;
    }

    public InetSocketAddress getServerAddress() {
        return serverAddress;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    public static YAMLMapper getYamlMapper() {
        return YAML_MAPPER;
    }

    public static Server getInstance(){
        return instance;
    }

    public int getServerTick() {
        return serverTick;
    }

    public Logger getLogger(){
        return this.logger;
    }

    public void stop(int status){
        if (status == 0){
            logger.info("Server is closed");
            stop$1();
            System.exit(0);
        }else{
            logger.fatal("Server is crashed");
            stop$1();
            System.exit(1);
        }
    }

    private void stop$1(){
        this.getPacketSender().close();
        this.getServerUdpReceive().close();
    }

    private void loadServerYml(){
        Path ymlPath = Paths.get(this.serverPath.toString(), "server.yml");
        if (!Files.exists(ymlPath)){
            logger.error("Can't find server.yml,creating new file....");
            try {
                Files.copy(Server.class.getClassLoader().getResourceAsStream("server.yml"),ymlPath);
            } catch (IOException e) {
                e.printStackTrace();
                logger.fatal("Can't create file,server will crash...");
                this.stop(1);
            }
        }
        serverSets = new Config(ymlPath);
    }

    private void registerDefaultListener(){
        this.getListenerManager().registerListener(new DefaultListener());
    }
}
