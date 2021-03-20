package cn.cookiestudio.easy4chess_server.game.matchcondition;

import cn.cookiestudio.easy4chess_server.player.Player;

public class WinRateRange implements MatchingCondition {

    private double maxRate;
    private double minRate;

    public WinRateRange(double maxRate, double minRate) {
        this.maxRate = maxRate;
        this.minRate = minRate;
    }

    @Override
    public boolean isMatched(Player player) {
        return (player.getWinRate() <= maxRate) && (player.getWinRate() >= minRate);
    }
}
