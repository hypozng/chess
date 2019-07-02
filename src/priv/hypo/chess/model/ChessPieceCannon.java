package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(炮)
 * 
 * @author Hypo
 */
public class ChessPieceCannon extends ChessPiece {
	private static final long serialVersionUID = -7332983421420045298L;

	public ChessPieceCannon(ChessRole role) {
		super(ChessPieceType.CANNON, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if(chessBoard == null || validTargets == null) {
			return;
		}
		
		int x = location.x, y = location.y;
		// 炮架
		boolean hasShelf = false;
		Point point = null;
		int[][] params = new int[][] { new int[] { x - 1, -1, -1, -1, y },
				new int[] { x + 1, chessBoard.getWidth(), 1, -1, y }, new int[] { y - 1, -1, -1, x, -1 },
				new int[] { y + 1, chessBoard.getHeight(), 1, x, -1 }, };
		int init = 0, limit = 0, increment = 0, localX = 0, localY = 0;
		for (int i = 0; i < params.length; i++) {
			hasShelf = false;
			init = params[i][0];
			limit = params[i][1];
			increment = params[i][2];
			localX = params[i][3];
			localY = params[i][4];
			for (int j = init; j != limit; j += increment) {
				point = new Point(localX < 0 ? j : localX, localY < 0 ? j : localY);
				if (chessBoard.hasPiece(point)) {
					if (hasShelf) {
						if (chessBoard.isReachablePoint(this, point)) {
							validTargets.add(point);
						}
						break;
					}
					hasShelf = true;
					continue;
				}
				if (!hasShelf) {
					validTargets.add(point);
				}
			}
		}
	}
}