package priv.hypo.chess.event;

import java.awt.Point;

import priv.hypo.chess.model.ChessPiece;

/** 象棋事件 */
public class ChessPieceEvent extends ChessEvent {
	private static final long serialVersionUID = 1594000340095502848L;
	
	public static final int PIECE_SELECTED = 1;
	public static final int PIECE_DESELECTED = 2;
	public static final int PIECE_DIED = 3;
	public static final int PIECE_MOVED = 4;
	
	// Fields
	private Point location;	// 棋子的位置
	
	// Constructors
	public ChessPieceEvent(int id, ChessPiece piece, Point location) {
		super(id, piece);
		this.location = new Point(location);
	}

	// Property accessors
	public Point getLocation() {
		return location;
	}
	
	public ChessPiece getPiece() {
		return (ChessPiece) source;
	}
}