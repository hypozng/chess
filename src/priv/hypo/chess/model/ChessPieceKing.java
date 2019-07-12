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
        // 单数为x值，双数为y值， x, y
        int[] params = new int[] {x - 1, y, x + 1, y, x, y - 1, x, y + 1};
        for (int i = 0; i < params.length / 2; ++i) {
            Point target = new Point(params[i * 2], params[i * 2 + 1]);
            if (chessBoard.inBoardSamePalace(location, target)) {
                validTargets.add(target);
            }
        }

        // 判断将是否能够击杀对方的帅
		ChessPieceKing king = chessBoard.getKing(ApplicationUtil.getEnemy(role));
        if (king == null) {
            return;
        }
        Point target = new Point(king.getLocation());
        if (target.x != location.x) {
            return;
        }
        int start = Math.min(y, target.y) + 1, end = Math.max(y, target.y);
        for (int i = start; i < end; ++i) {
            if (chessBoard.hasPiece(x, i)) {
                break;
            }
            if (i == end - 1) {
                validTargets.add(target);
            }
        }
    }

}