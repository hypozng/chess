package priv.hypo.chess.event;

import priv.hypo.chess.model.ChessRole;

public class PlayerEvent extends ChessEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 轮到文件下棋时调用
	public static final int PLAY = 1;
	
	private ChessRole role;

	public PlayerEvent(int id, Object source, ChessRole role) {
		super(id, source);
		this.role = role;
	}
	
	public ChessRole getRole() {
		return role;
	}

}