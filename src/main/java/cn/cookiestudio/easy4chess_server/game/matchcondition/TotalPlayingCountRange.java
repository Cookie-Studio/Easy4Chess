package cn.cookiestudio.easy4chess_server.game.matchcondition;

import cn.cookiestudio.easy4chess_server.player.Player;

public class TotalPlayingCountRange implements MatchingCondition {

    private int minPlayingCount;
    private int maxPlayingCount;

    public TotalPlayingCountRange(int minPlayingCount, int maxPlayingCount) {
        this.maxPlayingCount = maxPlayingCount;
        this.minPlayingCount = minPlayingCount;
    }

    @Override
    public boolean isMatched(Player player) {
        return (maxPlayingCount >= player.getTotalPlayingCount()) && (minPlayingCount <= player.getTotalPlayingCount());
    }
}
