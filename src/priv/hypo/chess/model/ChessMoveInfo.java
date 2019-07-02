package priv.hypo.chess.model;

import java.awt.Point;

/**
 * 棋子移动数据
 * @author Hypo
 */
public class ChessMoveInfo implements java.io.Serializable{
	private static final long serialVersionUID = -1053861612362115761L;
	
	private Point origin;
	private Point target;
	
	public ChessMoveInfo() { }
	
	public ChessMoveInfo(Point origin, Point target) { 
		this.origin = new Point(origin);
		this.target = new Point(target);
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
	
	
}