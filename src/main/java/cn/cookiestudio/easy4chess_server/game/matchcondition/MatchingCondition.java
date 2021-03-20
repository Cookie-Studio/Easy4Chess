package cn.cookiestudio.easy4chess_server.game.matchcondition;

import cn.cookiestudio.easy4chess_server.player.Player;

public interface MatchingCondition {
    boolean isMatched(Player player);
}
