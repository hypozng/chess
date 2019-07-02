package priv.hypo.chess.listener;

import priv.hypo.chess.event.PlayerEvent;

/**
 * 玩家监听接口，主要用于电脑控制的角色
 * @author Hypo
 * @date 2019-03-11
 */
public interface PlayerListener extends ChessListener {
	/**
	 * 轮到玩家走棋时调用
	 * @param role 轮到的玩家
	 */
	void onPlay(PlayerEvent e);
}