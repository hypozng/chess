package priv.hypo.chess.event;

import java.util.EventObject;
/**
 * 象棋事件
 * @author Hypo
 */
public class ChessEvent extends EventObject {
	private static final long serialVersionUID = -6341721829648563456L;
	
	private int id;
	
	public ChessEvent(int id, Object source) {
		super(source);
		this.id = id;
	}

	public int getId() {
		return id;
	}
}