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
	protected void findValidTargets(final int x, final int y) {
        int palaceY = y < ChessBoard.BOUNDARY ? ChessBoard.PALACE_Y1 : ChessBoard.PALACE_Y2;
        for (int i = 0; i < 2; ++i) {
            for (int j = -1; j < 2; j += 2) {
                int tx = i == 0 ? x : x + j, ty = i == 1 ? y : y + j;
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
        // 判断将是否能够击杀对方的帅
		ChessPieceKing king = chessBoard.getKing(role.getEnemy());
        int kx = king.getLocation().x, ky = king.getLocation().y;
        if (kx != x) {
            return;
        }
        int s = Math.min(y, ky) + 1, e = Math.max(y, ky);
        for (int i = s; i < e; ++i) {
            if (chessBoard.hasPiece(x, i)) {
                return;
            }
        }
        addValidTarget(kx, ky);
    }

}