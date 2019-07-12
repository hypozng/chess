package priv.hypo.chess.model;

import java.awt.Point;
import java.util.Collection;

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
    public void findValidTargets(Collection<Point> validTargets) {
        if (chessBoard == null || validTargets == null) {
            return;
        }

        int x = location.x, y = location.y;
        // 炮架
        boolean hasShelf = false;
        int[][] params = new int[][]{
                new int[]{x - 1, -1, -1, y},
                new int[]{x + 1, ChessBoard.WIDTH, -1, y},
                new int[]{y - 1, -1, x, -1},
                new int[]{y + 1, ChessBoard.HEIGHT, x, -1},
        };
        int start = 0, end = 0, increment = 0, tx = 0, ty = 0;
        for (int[] param : params) {
            hasShelf = false; // 遇到炮架时将此值设置为true
            start = param[0]; // 循环初始值
            end = param[1]; // 循环结束值
            increment = end < 0 ? -1 : 1; // 循环增量
            tx = param[2]; // 目标点x值，若小于0则使用循环中的变量代替该值，否则保持不变
            ty = param[3]; // 目标点y值，若小于0则使用循环中的变量代替该值，否则保持不变
            for (int i = start; i != end; i += increment) {
                Point target = new Point(tx < 0 ? i : tx, ty < 0 ? i : ty);
                if (chessBoard.hasPiece(target)) {
                    if (hasShelf) {
                        // 已经遇到炮架，将之后遇到的第一个棋子添加到有效目标点后退出本次循环，继续寻找下一个方向的有效目标点
                        validTargets.add(target);
                        break;
                    }
                    // 遇到炮架
                    hasShelf = true;
                    continue;
                }
                if (!hasShelf) {
                    validTargets.add(target);
                }
            }
        }
    }
}