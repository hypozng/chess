package priv.hypo.chess.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import priv.hypo.chess.event.PlayerEvent;
import priv.hypo.chess.listener.PlayerListener;
import priv.hypo.chess.model.ChessBoard;
import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.ChessStep;
import priv.hypo.chess.service.GameService;
import priv.hypo.chess.util.ApplicationUtil;

/**
 * npc
 * @author Hypo
 */
public class ComputerPlayer implements PlayerListener {
	// Fields
	// 游戏服务
	private GameService service;
	
	// 棋盘
	private ChessBoard chessBoard;
	
	// 角色
	private ChessRole role;
	
	// Constructors
	public ComputerPlayer(ChessRole role, GameService service) {
		this.service = service;
		this.chessBoard = service.getChessBoard();
		this.role = role;
		init();
	}

	// Property access
	public GameService getService() {
		return service;
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}

	public ChessRole getRole() {
		return role;
	}

	/**
	 * 初始化
	 */
	public void init() {
		service.addListener(this);
	}
	
	/**
	 * 断开电脑角色与游戏的关系
	 */
	public void dispose() {
		service.removeListener(this);
	}

	@Override
	public void onPlay(PlayerEvent e) {
		if (!e.getRole().equals(role)) {
			return;
		}
		randomMove();
	}

    /**
     * 随机获取有效的走棋步骤
     * @param steps
     * @return
     */
    private ChessStep getValidStep(List<ChessStep> steps) {
        if (steps == null || steps.isEmpty()) {
            return null;
        }
        List<ChessStep> _steps = new ArrayList<ChessStep>();
        _steps.addAll(steps);
        while (!_steps.isEmpty()) {
            ChessStep step = ApplicationUtil.randomGet(_steps);
            if (service.isValidStep(step)) {
                return step;
            }
            _steps.remove(step);
        }
        return null;
    }

	/**
	 * 随机走棋
	 */
	private void randomMove() {
        if (!service.getCurrentRole().equals(role)) {
            return;
        }
		if (service.kingHasThreaten()) {
			List<ChessStep> steps = service.solveKingThreaten();
			service.move(ApplicationUtil.randomGet(steps));
			return;
		}
        System.out.println("randomMove ");
        List<ChessStep> steps = new ArrayList<ChessStep>();
        List<ChessPiece> pieces = chessBoard.getPieces(role);
		for (ChessPiece piece : pieces) {
			for (Point target : piece.getValidTargets()) {
				ChessPiece enemy = chessBoard.getPiece(target);
                if (enemy == null) {
                    continue;
                }
                if (enemy.getRole().getEnemy().equals(role)) {
                    steps.add(new ChessStep(piece, target));
                }
			}
		}
        ChessStep step = getValidStep(steps);
        if (step == null) {
            do {
                ChessPiece piece = ApplicationUtil.randomGet(pieces);
                Point target = ApplicationUtil.randomGet(piece.getValidTargets());
                if (target == null) {
                    continue;
                }
                step = new ChessStep(piece, target);
            } while (!service.isValidStep(step));
        }
		service.move(step);
	}
	
}