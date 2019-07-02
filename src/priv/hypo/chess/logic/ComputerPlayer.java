package priv.hypo.chess.logic;

import java.awt.Point;
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
	 * 随机走棋
	 */
	private void randomMove() {
		if (service.isChecking(role)) {
			List<ChessStep> steps = service.getCheckStrategies();
			if (steps.isEmpty()) {
				return;
			}
			ChessStep step = steps.get(ApplicationUtil.random(steps.size()));
			service.move(step.getPiece(), step.getTarget());
			return;
		}
		List<ChessPiece> pieces = chessBoard.getPieces(role);
		for (ChessPiece piece : pieces) {
			for (Point target : piece.getValidTargets()) {
				ChessPiece enemy = chessBoard.getPiece(target);
				if (enemy != null && enemy.getRole() != piece.getRole()) {
					if (!service.willLose(piece, target)) {
						service.move(piece, target);
						return;
					}
				}
			}
		}
		Point target = null;
		ChessPiece piece = null;
		do {
			piece = ApplicationUtil.randomGet(pieces);
			target = ApplicationUtil.randomGet(piece.getValidTargets());
		} while (service.willLose(piece, target));
		service.move(piece, target);
	}
	
}