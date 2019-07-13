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
	protected void findValidTargets(final int x, final int y) {
        for (int hx = x - 1; hx < x + 2; hx += 2) {
            for (int hy = y - 1; hy < y + 2; hy += 2) {
                if (!chessBoard.inBoard(hx, hy)) {
                    continue;
                }
                if (chessBoard.hasPiece(hx, hy)) {
                    continue;
                }
                int tx = x + (hx - x) * 2, ty = y + (hy - y) * 2;
                if (!chessBoard.inBoard(tx, ty)) {
                    continue;
                }
                if (y < ChessBoard.BOUNDARY ? ty > chessBoard.BOUNDARY : ty < chessBoard.BOUNDARY) {
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