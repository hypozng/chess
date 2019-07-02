package priv.hypo.chess.event;


/**
 * 游戏事件
 * @author Hypo
 */
public class GameEvent extends ChessEvent {
	private static final long serialVersionUID = -5486701891712200798L;
	
	/** 悔棋 */
	public static final int REVOKE = 1;
	
	public GameEvent(int id, Object source) {
		super(id, source);
	}
	
}