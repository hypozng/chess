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
        int[][] params = new int[][] {
                new int[] {x - 1, y - 1, x - 2, y - 2},
                new int[] {x + 1, y - 1, x + 2, y - 2},
                new int[] {x - 1, y + 1, x - 2, y + 2},
                new int[] {x + 1, y + 1, x + 2, y + 2},
        };
        for (int[] param : params) {
            Point hinder = new Point(param[0], param[1]);
            Point target = new Point(param[2], param[3]);
            if (!chessBoard.hasPiece(hinder)
                    && chessBoard.inBoardSameBound(location, target)) {
                validTargets.add(target);
            }
        }
	}

}