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
	protected void findValidTargets(final int x, final int y) {
        // 单数为x值，双数为y值 总共4租
        int palaceY = y < ChessBoard.BOUNDARY ? ChessBoard.PALACE_Y1 : ChessBoard.PALACE_Y2;
        for (int tx = x - 1; tx < x + 2; tx += 2) {
            for (int ty = y - 1; ty < y + 2; ty += 2) {
                if (Math.abs(tx - ChessBoard.PALACE_X) > 1
                        || Math.abs(ty - palaceY) > 1) {
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