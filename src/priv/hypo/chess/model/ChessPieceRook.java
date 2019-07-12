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
    public void findValidTargets(Collection<Point> validTargets) {
        if (chessBoard == null || validTargets == null) {
            return;
        }

        validTargets.clear();
        int x = location.x, y = location.y;
        // 此二维数组，每一个子数组代表一个方向
        int[][] params = new int[][]{
                new int[]{x - 1, -1, -1, y},
                new int[]{x + 1, ChessBoard.WIDTH, -1, y},
                new int[]{y - 1, -1, x, -1},
                new int[]{y + 1, ChessBoard.HEIGHT, x, -1}
        };
        Point target = null;
        int start = 0, end = 0, increment = 0, tx = 0, ty = 0;
        for (int[] param : params) {
            start = param[0]; // 循环的初始值
            end = param[1]; // 循环的结束值
            increment = end < 0 ? -1 : 1; // 循环的增量
            tx = param[2]; // 目标点x值，小于0则使用循环中的变量代替该值，否则保持不变
            ty = param[3]; // 目标点y值，小于0则使用循环中的变量代替该值，否则保持不变
            for (int i = start; i != end; i += increment) {
                target = new Point(tx < 0 ? i : tx, ty < 0 ? i : ty);
                if (chessBoard.hasPiece(target)) {
                    validTargets.add(target);
                    break;
                }
                validTargets.add(target);
            }
        }
    }

}