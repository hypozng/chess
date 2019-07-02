package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(象)
 * 
 * @author Hypo
 */
public class ChessPieceBishop extends ChessPiece {
	private static final long serialVersionUID = 2304372397233473985L;

	public ChessPieceBishop(ChessRole role) {
		super(ChessPieceType.BISHOP, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if(chessBoard == null || validTargets == null) {
			return;
		}
		
		int x = location.x, y = location.y;
		Point[] hinders = new Point[] { new Point(x - 1, y - 1), new Point(x + 1, y - 1), new Point(x - 1, y + 1),
				new Point(x + 1, y + 1), };
		Point[] points = new Point[] { new Point(x - 2, y - 2), new Point(x + 2, y - 2), new Point(x - 2, y + 2),
				new Point(x + 2, y + 2), };
		for (int i = 0; i < hinders.length; i++) {
			Point hinder = hinders[i];
			Point point = points[i];
			if (!chessBoard.hasPiece(hinder)) {
				if (chessBoard.isReachablePoint(this, point) && chessBoard.inBoardSameBound(location, point)) {
					validTargets.add(point);
				}
			}
		}
	}

}