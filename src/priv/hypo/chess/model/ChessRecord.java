package priv.hypo.chess.model;

import java.awt.Point;

/**
 * 走棋记录
 * @author Hypo
 */
public class ChessRecord implements java.io.Serializable{
	private static final long serialVersionUID = 188486202565848216L;
	
	// Fields
	private Point origin;
	private Point target;
	private ChessPiece piece;
	private ChessPiece killed;
	
	// Constructors
	/** default constructor */
	public ChessRecord() {
		
	}
	
	public ChessRecord(Point origin, Point target,
			ChessPiece piece, ChessPiece killed) {
		this.origin = new Point(origin);
		this.target = new Point(target);
		this.piece = piece;
		this.killed = killed;
	}

    public ChessRecord(ChessStep step, ChessPiece killed) {
        this.origin = step.getOrigin();
        this.target = step.getTarget();
        this.piece = step.getPiece();
        this.killed = killed;
    }

	// Property accessors
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

	public ChessPiece getPiece() {
		return piece;
	}

	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}
	
	public ChessPiece getKilled() {
		return killed;
	}
	
	public void setKilled(ChessPiece killed) {
		this.killed = killed;
	}
	
	// Methods
	@Override
	public String toString() {
		return String.format("[%5s %6s] move origin [x=%d, y=%d] to [x=%d, y=%d]", piece.getRole(), piece.getType(), origin.x, origin.y, target.x, target.y)
				+ (killed != null ? String.format(", [%5s %6s] killed", killed.getRole(), killed.getType()) : ""); 
	}
}