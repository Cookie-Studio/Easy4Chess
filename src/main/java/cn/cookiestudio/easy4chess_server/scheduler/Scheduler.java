package cn.cookiestudio.easy4chess_server.scheduler;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.scheduler.tasks.ServerTask;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private Thread mainThread;
    private ExecutorService asyncTaskPool = Executors.newCachedThreadPool();
    private CopyOnWriteArrayList<ServerTask> mainThreadTasks = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<ServerTask> asyncTasks = new CopyOnWriteArrayList<>();
    private double mainThreadTPS = 20.000;//main thread tps, not async thread's!
    private double asyncThreadTPS = 20.000;//async thread tps, not main thread's!
    private int idealSleepMillisecond = (1000 / Server.getInstance().getServerTPS());

    public CopyOnWriteArrayList<ServerTask> getMainThreadTasks() {
        return mainThreadTasks;
    }

    public CopyOnWriteArrayList<ServerTask> getAsyncTasks() {
        return asyncTasks;
    }

    public int getIdealSleepMillisecond() {
        return idealSleepMillisecond;
    }

    public void schedulerTask(ServerTask task){
        this.schedulerTask(task,false);
    }

    public void schedulerTask(ServerTask task, boolean async){
        if (this.mainThread.isInterrupted())
            return;
        if (async)
            this.asyncTasks.add(task);
        else if(!async)
            this.mainThreadTasks.add(task);
    }

    public double getMainThreadTPS() {
        return this.mainThreadTPS;
    }

    public String getShortMainThreadTPS(){
        return String.format("%.3f", this.mainThreadTPS);
    }

    public double getAsyncThreadTPS() {
        return this.asyncThreadTPS;
    }

    public String getShortAsyncThreadTPS(){
        return String.format("%.3f", this.asyncThreadTPS);
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

                for (ServerTask asyncTask : Scheduler.this.asyncTasks)
                    Scheduler.this.asyncTaskPool.execute(() -> {
                        asyncTask.run();
                        if (asyncTask.isCancel())
                            Scheduler.this.asyncTasks.remove(asyncTask);
                    });
                for (ServerTask task : Scheduler.this.mainThreadTasks) {
                    task.run();
                    if (task.isCancel())Scheduler.this.mainThreadTasks.remove(task);
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
                Scheduler.this.mainThreadTasks.stream().forEach((t) -> {
                    t.setCancel();
                    Scheduler.this.mainThreadTasks.remove(t);
                });
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
