package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

/**
 * 棋子(車)
 *
 * @author Hypo
 */
public class ChessPieceRook extends ChessPiece {
    private static final long serialVersionUID = 8359243995056297942L;

    public ChessPieceRook(ChessRole role) {
        super(ChessPieceType.ROOK, role);
    }

    @Override
    protected void findValidTargets(final int x, final int y) {
        for (int i = 0; i < 2; ++i) {
            for (int j = -1; j < 2; j += 2) {
                int s = i == 0 ? x + j : y + j;
                int e = j == -1 ? -1 : i == 0 ? ChessBoard.WIDTH : ChessBoard.HEIGHT;
                int ptx = i == 0 ? -1 : x, pty = i == 1 ? -1 : y;
                for (int k = s; k != e; k += j) {
                    int tx = ptx == -1 ? k : ptx, ty = pty == -1 ? k : pty;
                    ChessPiece piece = chessBoard.getPiece(tx, ty);
                    if (piece != null) {
                        if (!piece.getRole().equals(role)) {
                            addValidTarget(tx, ty);
                        }
                        break;
                    }
                    addValidTarget(tx, ty);
                }
            }
        }
    }

}