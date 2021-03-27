package cn.cookiestudio.easy4chess_server.scheduler.tasks;

public class NoDelayTask extends ServerTask{

    /**
     * This class is created for thread security requirements
     * sometimes you need to put some operations into the server main thread call waiting chain to
     * avoid concurrency problems
     */

    public NoDelayTask(Runnable task) {
        super(task);
    }

    @Override
    public void tryInvokeTask() {
        this.task.run();
        this.setCancel();
    }
}
