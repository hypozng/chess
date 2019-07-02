package priv.hypo.chess.listener;

import java.util.EventListener;

import priv.hypo.chess.event.GameEvent;
/**
 * 游戏事件监听器
 * 包括悔棋、角色变换事件
 * @author Hypo
 */
public interface GameListener extends EventListener {
	void onInitialized(GameEvent e);
	
	/**
	 * 悔棋时调用
	 */
	void onRevoke(GameEvent e);
	
	/**
	 * 每一步走棋时调用
	 */
	void onPlay(GameEvent e);
	
	/**
	 * 游戏结束时调用
	 */
	void onOver(GameEvent e);
}