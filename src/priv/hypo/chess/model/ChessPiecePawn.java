package priv.hypo.chess.model;

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
	protected void findValidTargets(final int x, final int y) {
		ChessPieceKing king = chessBoard.getKing(role);
        int ky = king.getLocation().y;
        int f = ky < ChessBoard.BOUNDARY ? 1 : -1;
        boolean boundary = ky < ChessBoard.BOUNDARY ? y > ChessBoard.BOUNDARY : y < ChessBoard.BOUNDARY;
        int e = boundary ? 2 : 1;
        for (int i = 0; i < e; ++i) {
            for (int j = -1; j < 2 * i; j += 2) {
                int tx = i == 0 ? x : x + j, ty = i != 0 ? y : y + f;
                if (tx < 0 || tx >= ChessBoard.WIDTH) {
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