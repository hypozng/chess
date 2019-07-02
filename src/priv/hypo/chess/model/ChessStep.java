package priv.hypo.chess.model;

import java.awt.Point;
/**
 * 棋子移动一步信息
 * @author Hypo
 */
public class ChessStep {
	
	// Fields
	private ChessPiece piece;
	private Point origin;
	private Point target;
	
	// Constructors
	public ChessStep() {
		
	}
	
	public ChessStep(ChessPiece piece, Point origin, Point target) {
		this.piece = piece;
		this.origin = origin;
		this.target = target;
	}

	// Property accessors
	public ChessPiece getPiece() {
		return piece;
	}
	
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}

	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Point getTarget() {
		return target;
	}
	
	public void setTarget(Point target) {
		this.target = target;
	}
	
	// Methods
	public String toString() {
		return String.format("step[piece=%s, origin=%s, target=%s]", piece, origin, target);
	}
}