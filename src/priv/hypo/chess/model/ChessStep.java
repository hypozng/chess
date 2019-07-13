package priv.hypo.chess.model;

import java.awt.Point;
/**
 * 棋子移动信息
 * @author Hypo
 */
public class ChessStep {
	
    // 棋子
	private ChessPiece piece;

    // 原点
    private Point origin;

    // 目标点
	private Point target;
	
	// Constructors
    public ChessStep(ChessPiece piece, Point target) {
        this.piece = piece;
        this.origin = piece.getLocation().getLocation();
        this.target = target.getLocation();
        if (origin.equals(target)) {
            throw new RuntimeException("The origin is the same as the target. " + this);
        }
    }

	// Property accessors
	public ChessPiece getPiece() {
		return piece;
	}

	public Point getTarget() {
		return target;
	}

    public Point getOrigin() {
        return origin;
    }

    /**
     * 回到之前的位置
     * @return
     */
    public ChessStep back() {
        if (piece.getLocation().equals(origin)) {
            System.out.println("not move yet. " + this);
            return null;
        }
        return new ChessStep(piece, origin);
    }

	// Methods
	public String toString() {
		return String.format("step[piece=%s, target=%s]", piece, target);
	}
}