package cn.cookiestudio.easy4chess_server.scheduler;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.scheduler.tasks.ServerTask;
import cn.cookiestudio.easy4chess_server.utils.PriorityType;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private Thread mainThread;
    private ExecutorService asyncTaskPool = Executors.newCachedThreadPool();
    private HashMap<Integer, CopyOnWriteArrayList<ServerTask>> mainThreadTasks = new HashMap();
    private CopyOnWriteArrayList<ServerTask> asyncTasks = new CopyOnWriteArrayList<>();
    private double mainThreadTPS = 20.000;//main thread tps, isn't async thread's!
    private int idealSleepMillisecond = (1000 / Server.getInstance().getServerTPS());

    {
        mainThreadTasks.put(PriorityType.LOWEST.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.LOWER.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.LOW.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUMLOW.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUM.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUMHIGH.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGH.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGHER.ordinal(),new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGHEST.ordinal(),new CopyOnWriteArrayList<>());
    }

    public HashMap<Integer, CopyOnWriteArrayList<ServerTask>> getMainThreadTasks() {
        return mainThreadTasks;
    }

    public CopyOnWriteArrayList<ServerTask> getAsyncTasks() {
        return asyncTasks;
    }

    public int getIdealSleepMillisecond() {
        return idealSleepMillisecond;
    }

    public void schedulerTask(ServerTask task){
        if (this.mainThread.isInterrupted())
            return;
        this.schedulerTask(task,PriorityType.MEDIUM);
    }

    /**
     * @param task
     * @param priority
     * scheduler a main thread's task
     * only main thread task can has priority
     */
    public void schedulerTask(ServerTask task,PriorityType priority){
        if (this.mainThread.isInterrupted())
            return;
        this.mainThreadTasks.get(priority).add(task);
    }

    public void schedulerAsyncTask(ServerTask task){
        if (this.asyncTaskPool.isShutdown())
            return;
        this.asyncTasks.add(task);
    }

    public double getMainThreadTPS() {
        return this.mainThreadTPS;
    }

    public String getShortMainThreadTPS(){
        return String.format("%.3f", this.mainThreadTPS);
    }

    public void start(){
        this.mainThread = new Thread(new MainThreadRunnable());
        this.mainThread.start();
    }

    private class MainThreadRunnable implements Runnable{
        @Override
        public void run() {
            while(!mainThread.isInterrupted()){
                long startTime = System.currentTimeMillis();

                /**
                 * the async tasks are been submited to thread pool in main thread
                 * used parallel stream to increase the submit speed
                 */

                Scheduler.this.asyncTasks.stream().parallel().forEach((asyncTask) ->
                    Scheduler.this.asyncTaskPool.execute(() -> {
                    asyncTask.tryInvokeTask();
                    if (asyncTask.isCancel())
                        Scheduler.this.asyncTasks.remove(asyncTask);
                    }));

                for (int i = PriorityType.LOWEST.ordinal();i <= PriorityType.HIGHEST.ordinal();i++) {
                    for (ServerTask task : Scheduler.this.mainThreadTasks.get(i)){
                        task.tryInvokeTask();
                        if (task.isCancel())Scheduler.this.mainThreadTasks.remove(task);
                    }
                }

                long endTime = System.currentTimeMillis();
                long spendTime = endTime - startTime;
                long sleepNeededTime = Scheduler.this.idealSleepMillisecond - spendTime;

                if (sleepNeededTime >= 0){
                    Scheduler.this.sleep(sleepNeededTime);
                    Scheduler.this.mainThreadTPS = 20.000;
                }else if (sleepNeededTime < 0){
                    Scheduler.this.mainThreadTPS = 20.000 * ((double)Scheduler.this.idealSleepMillisecond / (double)spendTime);
                }
            }

            while(mainThread.isInterrupted() && mainThreadTasks.size() != 0){//after interrupted
                for (ServerTask asyncTask : Scheduler.this.asyncTasks)
                    Scheduler.this.asyncTaskPool.execute(() -> {
                        asyncTask.setCancel();
                        Scheduler.this.asyncTasks.remove(asyncTask);
                    });
                Scheduler.this.asyncTaskPool.shutdown();
                for (int i = PriorityType.LOWEST.ordinal();i <= PriorityType.HIGHEST.ordinal();i++) {
                    for (ServerTask task : Scheduler.this.mainThreadTasks.get(i)){
                        task.setCancel();
                        Scheduler.this.mainThreadTasks.remove(task);
                    }
                }
            }
        }
    }

    public void close(){
        this.mainThread.interrupt();
    }

    private void sleep(long millisecond){
        try {
            TimeUnit.MILLISECONDS.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
