package cn.cookiestudio.easy4chess_server.network.listener;

import cn.cookiestudio.easy4chess_server.network.packet.Packet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListenerManager {
    private final Map<Integer,ArrayList<ListenerMethod>> methodMap = new HashMap<>();
    public ListenerManager(){
        methodMap.put(PriorityType.LOWEST,new ArrayList<>());
        methodMap.put(PriorityType.LOWER,new ArrayList<>());
        methodMap.put(PriorityType.LOW,new ArrayList<>());
        methodMap.put(PriorityType.MEDIUMLOW,new ArrayList<>());
        methodMap.put(PriorityType.MEDIUM,new ArrayList<>());
        methodMap.put(PriorityType.MEDIUMHIGH,new ArrayList<>());
        methodMap.put(PriorityType.HIGH,new ArrayList<>());
        methodMap.put(PriorityType.HIGHER,new ArrayList<>());
        methodMap.put(PriorityType.HIGHEST,new ArrayList<>());
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

    public void callAllListener(Packet packet){
        for (int i = PriorityType.LOWEST; i<= PriorityType.HIGHEST; i++){
            for (ListenerMethod lm : getMethodMap().get(i)){
                if (!packet.isCancelled())
                    if (lm.isMatchPacket(packet))
                        try {
                            lm.getMethod().invoke(lm.getInstance(),packet);
                        } catch (Exception e) {
                            new PacketException(e).printStackTrace();
                        }
                else
                    if (lm.isMatchPacket(packet) && lm.isIgnoreCanceled())
                        try {
                            lm.getMethod().invoke(lm.getInstance(),packet);
                        } catch (Exception e) {
                            new PacketException(e).printStackTrace();
                        }
            }
        }
    }
    private class ListenerMethod{
        public int priority;
        public boolean isIgnoreCanceled;
        public Method method;
        public Listener instance;

        public int getPriority() {
            return priority;
        }

        public boolean isIgnoreCanceled() {
            return isIgnoreCanceled;
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
            this.isIgnoreCanceled = annotation.IgnoreCanceled();
            this.priority = annotation.priority();
            this.instance = instance;
        }
        public boolean isMatchPacket(Packet packet){
            return this.method.getParameterTypes()[0] == packet.getClass();
        }
    }
}
