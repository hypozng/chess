package priv.hypo.chess.model;

/**
 * 棋子(炮)
 *
 * @author Hypo
 */
public class ChessPieceCannon extends ChessPiece {
    private static final long serialVersionUID = -7332983421420045298L;

    public ChessPieceCannon(ChessRole role) {
        super(ChessPieceType.CANNON, role);
    }

    @Override
    protected void findValidTargets(final int x, final int y) {
        for (int i = 0; i < 2; ++i) {
            for (int j = -1; j < 2; j += 2) {
                int s = i == 0 ? x + j : y + j;
                int e = j == -1 ? -1 : i == 0 ? ChessBoard.WIDTH : ChessBoard.HEIGHT;
                int ptx = i == 0 ? -1 : x, pty = i == 1 ? -1 : y;
                boolean pivot = false;
                for (int k = s; k != e; k += j) {
                    int tx = ptx == -1 ? k : ptx, ty = pty == -1 ? k : pty;
                    ChessPiece piece = chessBoard.getPiece(tx, ty);
                    if (piece != null) {
                        if (!pivot) {
                            pivot = true;
                            continue;
                        }
                        if (!piece.getRole().equals(role)) {
                            addValidTarget(tx, ty);
                        }
                        break;
                    }
                    if (!pivot) {
                        addValidTarget(tx, ty);
                    }
                }
            }
        }
    }
}