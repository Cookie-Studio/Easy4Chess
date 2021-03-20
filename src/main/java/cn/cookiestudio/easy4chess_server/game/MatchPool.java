package cn.cookiestudio.easy4chess_server.game;

import cn.cookiestudio.easy4chess_server.Server;
import cn.cookiestudio.easy4chess_server.game.matchcondition.MatchingPlayer;
import cn.cookiestudio.easy4chess_server.scheduler.tasks.PeriodTask;

import java.util.concurrent.CopyOnWriteArrayList;

public class MatchPool {
    private CopyOnWriteArrayList<MatchingPlayer> matchers = new CopyOnWriteArrayList<>();

    public MatchPool(){
        Server.getInstance().getScheduler().schedulerAsyncTask(new PeriodTask(new MatchingTask(),1));
    }

    public CopyOnWriteArrayList<MatchingPlayer> getMatchers() {
        return matchers;
    }

    public void matchPlayer(MatchingPlayer player) {
        this.matchers.add(player);
    }

    private class MatchingTask implements Runnable{
        @Override
        public void run() {
            for (MatchingPlayer player : matchers){
                for (MatchingPlayer player1 : matchers){
                    if (player.isMatched(player1.getPlayer()) && player1.isMatched(player.getPlayer())){
                        matchers.remove(player);
                        matchers.remove(player1);

                        //todo

                        return;
                    }
                }
            }
        }
    }
}
