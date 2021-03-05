package cn.cookiestudio.easy4chess_server.scheduler.timer;

import java.util.concurrent.TimeUnit;

public class Timer{
    private int period;
    private long runTicks;
    private Thread thread;

    public Timer(int period){
        this.period = period;
        this.thread = new Thread(new TimingTask());
    }

    private class TimingTask implements Runnable{

        @Override
        public void run() {
            while(!thread.isInterrupted()){
                Timer.this.runTicks++;
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 * (1 / period));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getPeriod(){
        return period;
    }

    public long getRunTicks(){
        return runTicks;
    }

    public void close(){
        this.thread.interrupt();
    }

    public boolean isClosed(){
        return this.thread.isInterrupted();
    }

    public void startTiming(){
        this.thread.start();
    }
}
