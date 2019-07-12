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
        int[][] params = new int[][] {
                new int[] {x, y - 1, x - 1, y - 2, x + 1, y - 2},
                new int[] {x, y + 1, x - 1, y + 2, x + 1, y + 2},
                new int[] {x - 1, y, x - 2, y - 1, x - 2, y + 1},
                new int[] {x + 1, y, x + 2, y - 1, x + 2, y + 1}
        };
        for (int[] param : params) {
            Point hinder = new Point(param[0], param[1]);
            Point target1 = new Point(param[2], param[3]);
            Point target2 = new Point(param[4], param[5]);
            if (!chessBoard.hasPiece(hinder)) {
                if (chessBoard.inBoard(target1)) {
                    validTargets.add(target1);
                }
                if (chessBoard.inBoard(target2)) {
                    validTargets.add(target2);
                }
            }
        }
	}
}