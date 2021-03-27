package cn.cookiestudio.easy4chess_server.network.listener;

import cn.cookiestudio.easy4chess_server.network.packet.Packet;
import cn.cookiestudio.easy4chess_server.utils.PriorityType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private final Map<Integer,ArrayList<ListenerMethod>> methodMap = new HashMap<>();
    public ListenerManager(){
        methodMap.put(PriorityType.LOWEST.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.LOWER.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.LOW.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.MEDIUMLOW.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.MEDIUM.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.MEDIUMHIGH.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.HIGH.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.HIGHER.ordinal(),new ArrayList<>());
        methodMap.put(PriorityType.HIGHEST.ordinal(),new ArrayList<>());
    }

    public Map<Integer, ArrayList<ListenerMethod>> getMethodMap() {
        return methodMap;
    }

    public void registerListener(Listener listener){
        for(Method method : listener.getClass().getMethods()){
            if (!method.isAnnotationPresent(PacketHandler.class)){
                continue;
            }
            getMethodMap().get(method.getAnnotation(PacketHandler.class).priority()).add(new ListenerMethod(method,listener));
        }
    }

    public <T extends Packet> T callPacket(T packet){
        for (int i = PriorityType.LOWEST.ordinal(); i<= PriorityType.HIGHEST.ordinal(); i++){
            for (ListenerMethod lm : getMethodMap().get(i)){
                if (!packet.isCancelled())
                    if (lm.packetMatched(packet))
                        try {
                            lm.getMethod().invoke(lm.getInstance(),packet);
                        } catch (Exception e) {
                            new PacketException(e).printStackTrace();
                        }
                else
                    if (lm.packetMatched(packet) && lm.ignoreCanceled())
                        try {
                            lm.getMethod().invoke(lm.getInstance(),packet);
                        } catch (Exception e) {
                            new PacketException(e).printStackTrace();
                        }
            }
        }
        return packet;
    }
    private class ListenerMethod{
        public int priority;
        public boolean ifIgnoreCanceled;
        public Method method;
        public Listener instance;

        public int getPriority() {
            return priority;
        }

        public boolean ignoreCanceled() {
            return ifIgnoreCanceled;
        }

        public Method getMethod() {
            return method;
        }

        public Listener getInstance() {
            return instance;
        }

        public ListenerMethod(Method method, Listener instance){
            PacketHandler annotation = method.getAnnotation(PacketHandler.class);
            this.method = method;
            this.ifIgnoreCanceled = annotation.IgnoreCanceled();
            this.priority = annotation.priority();
            this.instance = instance;
        }
        public boolean packetMatched(Packet packet){
            return this.method.getParameterTypes()[0].isAssignableFrom(packet.getClass());
        }}
}
