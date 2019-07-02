package priv.hypo.chess.listener;

import java.awt.Point;
import java.util.EventListener;

/**
 * 房间状态监听接口
 * @author Hypo
 */
public interface RoomListener extends EventListener {
	
	/**
	 * 有玩家加入时触发
	 */
	void onJoin();

	/**
	 * 棋子移动时触发
	 * @param origin 棋子起始位置
	 * @param target 棋子移动的目标位置
	 */
	void onMove(Point origin, Point target);

	/**
	 * 悔棋时调用
	 */
	void onRevoke();
	
}