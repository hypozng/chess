package priv.hypo.chess.listener;

import priv.hypo.chess.event.ChessPieceEvent;
/**
 * 监听棋子相关事件的监听器
 * 其中包括棋子被选中、取消选中、死亡、移动事件
 * @author Hypo
 *
 */
public interface ChessPieceListener extends ChessListener {
	/**
	 * 棋子被选中时调用
	 */
	public abstract void onSelected(ChessPieceEvent e);
	
	/**
	 * 棋子被取消选中时调用
	 */
	public abstract void onDeselected(ChessPieceEvent e);
	
	/**
	 * 棋子被吃掉时调用
	 */
	public abstract void onDied(ChessPieceEvent e);
	
	/**
	 * 棋子移动时调用
	 */
	public abstract void onMoved(ChessPieceEvent e);
}