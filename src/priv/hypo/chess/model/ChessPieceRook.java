package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(車)
 * 
 * @author Hypo
 */
public class ChessPieceRook extends ChessPiece {
	private static final long serialVersionUID = 8359243995056297942L;

	public ChessPieceRook(ChessRole role) {
		super(ChessPieceType.ROOK, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if (chessBoard == null || validTargets == null) {
			return;
		}

		validTargets.clear();
		int x = location.x, y = location.y;
		int[][] params = new int[][] { new int[] { x - 1, -1, -1, -1, y },
				new int[] { x + 1, chessBoard.getWidth(), 1, -1, y }, new int[] { y - 1, -1, -1, x, -1 },
				new int[] { y + 1, chessBoard.getHeight(), 1, x, -1 }, };
		Point point = null;
		int init = 0, limit = 0, increment = 0, localx = 0, localy = 0;
		for (int[] param : params) {
			init = param[0];
			limit = param[1];
			increment = param[2];
			localx = param[3];
			localy = param[4];
			for (int j = init; j != limit; j += increment) {
				point = new Point(localx < 0 ? j : localx, localy < 0 ? j : localy);
				if (chessBoard.hasPiece(point)) {
					if (chessBoard.isReachablePoint(this, point)) {
						validTargets.add(point);
					}
					break;
				}
				validTargets.add(point);
			}
		}
	}

}