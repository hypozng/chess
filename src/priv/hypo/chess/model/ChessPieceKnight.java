package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(马)
 * 
 * @author Hypo
 */
public class ChessPieceKnight extends ChessPiece {
	private static final long serialVersionUID = -5447931347386014688L;

	public ChessPieceKnight(ChessRole role) {
		super(ChessPieceType.KNIGHT, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if (chessBoard == null || validTargets == null) {
			return;
		}

		int x = location.x, y = location.y;
		Point[] hinders = new Point[] {
				new Point(x, y - 1),
				new Point(x, y + 1),
				new Point(x - 1, y),
				new Point(x + 1, y),
			};
		Point[] points = new Point[] {
				new Point(x - 1, y - 2),
				new Point(x + 1, y - 2),
				new Point(x - 1, y + 2),
				new Point(x + 1, y + 2),
				new Point(x - 2, y - 1),
				new Point(x - 2, y + 1),
				new Point(x + 2, y - 1),
				new Point(x + 2, y + 1),
			};
		for (int i = 0; i < hinders.length; i++) {
			Point hinder = hinders[i];
			Point point1 = points[i * 2], point2 = points[i * 2 + 1];
			if (!chessBoard.hasPiece(hinder)) {
				if (chessBoard.isReachablePoint(this, point1)) {
					validTargets.add(point1);
				}
				if (chessBoard.isReachablePoint(this, point2)) {
					validTargets.add(point2);
				}
			}
		}
	}
}