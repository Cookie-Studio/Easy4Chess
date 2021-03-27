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
    private HashMap<PriorityType, CopyOnWriteArrayList<ServerTask>> mainThreadTasks = new HashMap<>();
    private CopyOnWriteArrayList<ServerTask> asyncTasks = new CopyOnWriteArrayList<>();
    private double mainThreadTPS = 20.000;//main thread tps, isn't async thread's!
    private int idealSleepMillisecond = (1000 / Server.getInstance().getServerTPS());

    public Scheduler() {
        mainThreadTasks.put(PriorityType.LOWEST,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.LOWER,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.LOW,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUMLOW,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUM,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.MEDIUMHIGH,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGH,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGHER,new CopyOnWriteArrayList<>());
        mainThreadTasks.put(PriorityType.HIGHEST,new CopyOnWriteArrayList<>());

        this.mainThread = new Thread(new MainThreadRunnable());
    }

    public HashMap<PriorityType, CopyOnWriteArrayList<ServerTask>> getMainThreadTasks() {
        return mainThreadTasks;
    }

    public CopyOnWriteArrayList<ServerTask> getAsyncTasks() {
        return asyncTasks;
    }

    public int getIdealSleepMillisecond() {
        return idealSleepMillisecond;
    }

    /**
     * scheduler a main thread's task
     * only main thread task can has priority
     */
    public synchronized void schedulerTask(ServerTask task){
        this.schedulerTask(task,PriorityType.MEDIUM);
    }

    public synchronized void schedulerTask(ServerTask task,PriorityType priority){
        if (this.mainThread.isInterrupted())
            return;
        this.mainThreadTasks.get(priority).add(task);
    }

    public synchronized void schedulerAsyncTask(ServerTask task){
        if (this.asyncTaskPool.isShutdown())
            return;
        this.asyncTasks.add(task);
    }

    //在异步线程池调用一个立即执行的runnable，scheduler(Async)Task()不能保证立即执行
    public synchronized void schedulerImmediateInvokeAsyncRunnable(Runnable runnable){
        this.asyncTaskPool.execute(runnable);
    }

    public double getMainThreadTPS() {
        return this.mainThreadTPS;
    }

    public Thread getMainThread() {
        return mainThread;
    }

    public ExecutorService getAsyncTaskPool() {
        return asyncTaskPool;
    }

    public String getShortMainThreadTPS(){
        return String.format("%.3f", this.mainThreadTPS);
    }

    public void start(){
        this.mainThread.start();
//        this.asyncTaskPool.execute(new MainThreadSafeScheduler());
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
                    for (ServerTask task : Scheduler.this.mainThreadTasks.get(PriorityType.values()[i])){
                        task.tryInvokeTask();
                        if (task.isCancel())Scheduler.this.mainThreadTasks.get(PriorityType.values()[i]).remove(task);
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
                    for (ServerTask task : Scheduler.this.mainThreadTasks.get(PriorityType.values()[i])){
                        task.setCancel();
                        Scheduler.this.mainThreadTasks.get(PriorityType.values()[i]).remove(task);
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
