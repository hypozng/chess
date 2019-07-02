package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

import priv.hypo.chess.util.ApplicationUtil;

/**
 * 棋子(将)
 * 
 * @author Hypo
 */
public class ChessPieceKing extends ChessPiece {
	private static final long serialVersionUID = -2537936415992633861L;

	public ChessPieceKing(ChessRole role) {
		super(ChessPieceType.KING, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if (chessBoard == null || validTargets == null) {
			return;
		}

		int x = location.x, y = location.y;
		Point[] points = new Point[] { new Point(x - 1, y), new Point(x + 1, y), new Point(x, y - 1),
				new Point(x, y + 1), };
		for (Point point : points) {
			if (chessBoard.isReachablePoint(this, point) && chessBoard.inBoardSamePalace(location, point)) {
				validTargets.add(point);
			}
		}

		ChessPieceKing king = chessBoard.getKing(ApplicationUtil.getEnemy(role));
		Point kloc = null;
		if (king != null && (kloc = king.getLocation()) != null) {
			int init = Math.min(y, kloc.y) + 1, limit = Math.max(y, kloc.y);
			if (x == kloc.x) {
				for (int i = init; i < limit; ++i) {
					if (chessBoard.hasPiece(x, i)) {
						break;
					}
					if (i == limit - 1) {
						validTargets.add(new Point(king.getLocation()));
					}
				}
			}
		}
	}

}