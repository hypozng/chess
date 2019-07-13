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
	protected void findValidTargets(final int x, final int y) {
        for (int i = 0; i < 2; ++i) {
            for (int j = -1; j < 2; j += 2) {
                int hx = i == 1 ? x : x + j, hy = i == 0 ? y : y + j;
                if (!chessBoard.inBoard(hx, hy)) {
                    continue;
                }
                if (chessBoard.hasPiece(hx, hy)) {
                    continue;
                }
                int ptx = x == hx ? x : x + (hx - x) * 2;
                int pty = y == hy ? y : y + (hy - y) * 2;
                for (int k = -1; k < 2; k += 2) {
                    int tx = x == hx ? x + k : ptx, ty = y == hy ? y + k : pty;
                    if (!chessBoard.inBoard(tx, ty)) {
                        continue;
                    }
                    ChessPiece piece = chessBoard.getPiece(tx, ty);
                    if (piece != null && piece.getRole().equals(role)) {
                        continue;
                    }
                    addValidTarget(tx, ty);
                }

            }
        }
	}
}