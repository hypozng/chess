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
        // 单数为x值，双数为y值 总共4租
        int[] params = new int[] {x - 1, y - 1, x + 1, y - 1, x - 1, y + 1, x + 1, y + 1};
        for (int i = 0; i < params.length / 2; ++i) {
            Point target = new Point(params[i * 2], params[i * 2 + 1]);
            if (chessBoard.inBoardSamePalace(location, target)) {
                validTargets.add(target);
            }
        }
	}

}