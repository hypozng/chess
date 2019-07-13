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
        if (service.isOver() || !e.getRole().equals(role)) {
            return;
        }
		think();
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
	 * 思考如何走棋
	 */
	private void think() {
        ChessStep step = null;
        if (step == null) {
            step = emergency();
        }
        if (step == null) {
            step = attack();
        }
        if (step == null) {
            step = random();
        }
		service.move(step);
	}

    /**
     * 解决将军
     * @return
     */
    private ChessStep emergency() {
        if (service.kingHasThreaten()) {
            List<ChessStep> steps = service.solveKingThreaten();
            return ApplicationUtil.randomGet(steps);
        }
        return null;
    }

    /**
     * 攻击
     * @return
     */
    private ChessStep attack() {
        List<ChessStep> steps = new ArrayList<ChessStep>();
        List<ChessPiece> pieces = chessBoard.getPieces(role);
        List<ChessPiece> enemies = chessBoard.getPieces(role.getEnemy());
        for (ChessPiece enemy : enemies) {
            for (ChessPiece piece : pieces) {
                if (piece.getValidTargets().contains(enemy.getLocation())) {
                    steps.add(new ChessStep(piece, enemy.getLocation()));
                }
            }
        }
        return getValidStep(steps);
    }

    /**
     * 随机走棋
     * @return
     */
    private ChessStep random() {
        List<ChessStep> steps = new ArrayList<ChessStep>();
        List<ChessPiece> pieces = chessBoard.getPieces(role);
        for (ChessPiece piece : pieces) {
            for (Point target : piece.getValidTargets()) {
                steps.add(new ChessStep(piece, target));
            }
        }
        return getValidStep(steps);
    }
}