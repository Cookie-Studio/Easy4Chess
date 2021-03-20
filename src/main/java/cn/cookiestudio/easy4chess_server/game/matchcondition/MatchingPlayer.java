package cn.cookiestudio.easy4chess_server.game.matchcondition;

import cn.cookiestudio.easy4chess_server.player.Player;

public class MatchingPlayer {

    private Player player;
    private MatchingCondition[] matchConditions;

    public MatchingPlayer(Player player, MatchingCondition... conditions){
        this.player = player;
        this.matchConditions = conditions;
    }

    public Player getPlayer() {
        return player;
    }

    public MatchingCondition[] getMatchConditions() {
        return matchConditions;
    }

    public boolean isMatched(Player player){
        for (MatchingCondition condition : this.matchConditions){
            if (!condition.isMatched(player))
                return false;
        }
        return true;
    }
}
