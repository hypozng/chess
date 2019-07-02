package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(仕)
 * 
 * @author Hypo
 */
public class ChessPieceGuard extends ChessPiece {
	private static final long serialVersionUID = 1956824207767832580L;

	public ChessPieceGuard(ChessRole role) {
		super(ChessPieceType.GUARD, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if(chessBoard == null || validTargets == null) {
			return;
		}

		int x = location.x, y = location.y;
		Point[] points = new Point[] { new Point(x - 1, y - 1), new Point(x + 1, y - 1), new Point(x - 1, y + 1),
				new Point(x + 1, y + 1), };
		for (Point point : points) {
			if (chessBoard.isReachablePoint(this, point) && chessBoard.inBoardSamePalace(location, point)) {
				validTargets.add(point);
			}
		}
	}

}