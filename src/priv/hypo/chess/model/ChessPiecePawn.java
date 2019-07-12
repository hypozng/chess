package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

/**
 * 棋子(兵)
 * 
 * @author Hypo
 */
public class ChessPiecePawn extends ChessPiece {
	private static final long serialVersionUID = -192322394135743277L;

	public ChessPiecePawn(ChessRole role) {
		super(ChessPieceType.PAWN, role);
	}

	@Override
	public void findValidTargets(Collection<Point> validTargets) {
		if (chessBoard == null || validTargets == null) {
			return;
		}

		int x = location.x, y = location.y;
		ChessPieceKing king = chessBoard.getKing(role);
		if (chessBoard.inBoardUpBound(king.location)) {
			validTargets.add(new Point(x, y + 1));
		} else {
			validTargets.add(new Point(x, y - 1));
		}
		if (!chessBoard.inBoardSameBound(king.location, location)) {
			validTargets.add(new Point(x - 1, y));
			validTargets.add(new Point(x + 1, y));
		}
	}
}