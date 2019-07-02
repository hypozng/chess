package priv.hypo.chess.service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

import priv.hypo.chess.event.ChessPieceEvent;
import priv.hypo.chess.event.GameEvent;
import priv.hypo.chess.event.PlayerEvent;
import priv.hypo.chess.listener.ChessListener;
import priv.hypo.chess.listener.ChessPieceListener;
import priv.hypo.chess.listener.GameListener;
import priv.hypo.chess.listener.PlayerListener;
import priv.hypo.chess.listener.RoomListener;
import priv.hypo.chess.logic.ComputerPlayer;
import priv.hypo.chess.model.ChessBoard;
import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessPieceCannon;
import priv.hypo.chess.model.ChessPieceKing;
import priv.hypo.chess.model.ChessPieceKnight;
import priv.hypo.chess.model.ChessPieceRook;
import priv.hypo.chess.model.ChessPieceType;
import priv.hypo.chess.model.ChessRecord;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.ChessRoleCategory;
import priv.hypo.chess.model.ChessStep;
import priv.hypo.chess.model.Configuration;
import priv.hypo.chess.util.ApplicationUtil;
import priv.hypo.chess.util.StorageUtil;

/**
 * 游戏服务
 * 
 * @author Hypo
 */
public class GameService implements RoomListener {
	// Fields
	private static GameService instance;

	// 棋盘
	private ChessBoard chessBoard = new ChessBoard();

	// 当前选中的棋子
	private ChessPiece selected = null;

	// 房间服务
	private RoomService roomService = RoomService.getInstance();

	// 象棋事件监听器
	private EventListenerList listeners = new EventListenerList();
	
	@SuppressWarnings("rawtypes")
	private Class[] listenerClasses = new Class[] {
			ChessPieceListener.class,
			GameListener.class,
			PlayerListener.class
		};

	// 走棋历史记录
	private LinkedList<ChessRecord> records = new LinkedList<ChessRecord>();

	// 当前走棋方
	private ChessRole currentRole = ChessRole.RED;

	// 胜利者
	private ChessRole winner = null;

	// 相关配置信息
	private Configuration configuration = new Configuration();
	
	private List<ComputerPlayer> players = new ArrayList<ComputerPlayer>();

	// Constructors
	private GameService() {
		init(null);
		roomService.addListener(this);
	}

	// Property accessors
	public static GameService getInstance() {
		if (instance == null) {
			synchronized (GameService.class) {
				if (instance == null) {
					instance = new GameService();
				}
			}
		}
		return instance;
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}

	public ChessPiece getSelected() {
		return selected;
	}

	public LinkedList<ChessRecord> getRecords() {
		return records;
	}

	public ChessRole getCurrentRole() {
		return currentRole;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		if (configuration != null) {
			this.configuration = configuration;
		}
	}

	/**
	 * 获取指定角色的类型
	 * 
	 * @param role
	 *            角色(红方或者黑方)
	 * @return 角色类型(玩家或者电脑)
	 */
	public ChessRoleCategory getRoleCategory(ChessRole role) {
		return configuration.getCategory(role);
	}

	/**
	 * 获取胜利者角色
	 * 
	 * @return 胜利者角色
	 */
	public ChessRole getWinner() {
		return winner;
	}

	/**
	 * 初始化游戏
	 * 
	 * @param role
	 *            使用的角色
	 */
	public void init(ChessRole role) {
		if (role == null) {
			role = ChessRole.RED;
		}
		String fileName = null;
		switch (role) {
		case RED:
			fileName = "chess_board_red.dat";
			break;
		case BLACK:
			fileName = "chess_board_black.dat";
			break;
		}
		chessBoard = new ChessBoard();
		List<ChessPiece> pieces = StorageUtil.loadPieces(ApplicationUtil.getDataResourceAsStream(fileName));
		chessBoard.addPieces(pieces);

		currentRole = ChessRole.RED;
		selected = null;
		winner = null;
		records = new LinkedList<ChessRecord>();
		if (!players.isEmpty()) {
			for (ComputerPlayer player : players) {
				player.dispose();
			}
			players.clear();
		}
		
		for (ChessRole r : ChessRole.values()) {
			if (configuration.getCategory(r).equals(ChessRoleCategory.COMPUTER)) {
				players.add(new ComputerPlayer(r, this));
			}
		}
		System.gc();
	}

	/**
	 * 添加监听程序
	 * @param l
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addListener(ChessListener l) {
		if (l == null) {
			return;
		}
		Class lcls = l.getClass();
		for (Class cls : listenerClasses) {
			if (cls.isAssignableFrom(lcls)) {
				listeners.add(cls, l);
			}
		}
	}
	
	/**
	 * 删除监听程序
	 * @param l
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeListener(ChessListener l) {
		if (l == null) {
			return;
		}
		Class lcls = l.getClass();
		for (Class cls : listenerClasses) {
			if (cls.isAssignableFrom(lcls)) {
				listeners.remove(cls, l);
			}
		}
	}
	
	/**
	 * 用指定的步骤走棋，如果直接会导致输掉比赛返回true
	 * 
	 * @param piece
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean willLose(ChessPiece piece, int x, int y) {
		Point origin = new Point(piece.getLocation());
		ChessPiece enemy = chessBoard.getPiece(x, y);
		chessBoard.move(piece, x, y);
		if (isChecking(currentRole)) {
			return true;
		}
		chessBoard.move(piece, origin);
		if (enemy != null) {
			chessBoard.setPiece(x, y, enemy);
		}
		return false;
	}

	public boolean willLose(ChessPiece piece, Point target) {
		if (piece == null || target == null) {
			return false;
		}
		return willLose(piece, target.x, target.y);
	}

	/**
	 * 选中一个棋子
	 * 
	 * @param piece
	 *            要选择的棋子
	 */
	public void select(ChessPiece piece) {
		if (roomService.exist() && !roomService.getRole().equals(piece.getRole())) {
			return;
		}
		if (selected != null) {
			deselect();
		}
		selected = piece;
		postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_SELECTED, selected, selected.getLocation()));
	}

	/**
	 * 取消选择
	 */
	public void deselect() {
		if (selected != null) {
			ChessPiece selected = this.selected;
			this.selected = null;
			postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_DESELECTED, selected, selected.getLocation()));
		}
	}

	/**
	 * 移动一个棋子
	 * 
	 * @param piece
	 *            要移动的棋子
	 * @param x
	 *            目标位置的x值
	 * @param y
	 *            目标位置的y值
	 */
	public void move(ChessPiece piece, int x, int y) {
		if (winner != null || piece == null) {
			return;
		}
		Point origin = new Point(piece.getLocation()), target = new Point(x, y);
		ChessPiece enemy = chessBoard.getPiece(x, y);
		if (willLose(piece, x, y)) {
			deselect();
			return;
		}
		chessBoard.move(piece, target);
		postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_MOVED, piece, origin));
		if (roomService.exist() && roomService.getRole().equals(piece.getRole())) {
			roomService.movePiece(origin, target);
		}
		kill(enemy);
		deselect();
		records.addLast(new ChessRecord(origin, target, piece, enemy));

		currentRole = piece.getRole().getEnemy();
		if (isChecking()) {
			if (getCheckStrategies().isEmpty()) {
				winner = ApplicationUtil.getEnemy(currentRole);
			}
		}
		if (chessBoard.getPieces(currentRole).size() == 1) {
			ChessPiece king = chessBoard.getKing(currentRole);
			List<Point> movedLocations = new ArrayList<Point>();
			movedLocations.addAll(king.getValidTargets());
			boolean flag = false;
			for (Point movedLocation : movedLocations) {
				if (!willLose(king, movedLocation)) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				winner = ApplicationUtil.getEnemy(currentRole);
			}
		}
	}

	/**
	 * 移动一个棋子
	 * 
	 * @param piece
	 * @param target
	 */
	public void move(ChessPiece piece, Point target) {
		if (target == null) {
			return;
		}
		move(piece, target.x, target.y);
	}

	/**
	 * 杀死一个棋子
	 * 
	 * @param piece
	 *            棋子
	 */
	public void kill(ChessPiece piece) {
		if (piece != null) {
			Point location = piece.getLocation();
			chessBoard.removePiece(piece);
			postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_DIED, piece, location));
		}
	}

	/**
	 * 处理棋盘的点击事件
	 * 
	 * @param x
	 * @param y
	 */
	public void click(int x, int y) {
		if (winner != null || !chessBoard.inBoard(x, y)) {
			return;
		}
		ChessPiece piece = chessBoard.getPiece(x, y);
		if (selected != null) {
			if (selected.isValidTarget(x, y)) {
				move(selected, x, y);
			} else {
				if (piece != null) {
					if (selected == piece) {
						deselect();
					} else if (selected.getRole() == piece.getRole()) {
						select(piece);
					}
				} else {
					deselect();
				}
			}
		} else {
			if (piece != null && piece.getRole() == currentRole) {
				select(piece);
			}
		}
	}

	/**
	 * 悔棋
	 */
	public void revoke(boolean send) {
		winner = null;
		deselect();
		if (records.isEmpty()) {
			return;
		}
		ChessRecord record = records.getLast();
		chessBoard.move(record.getPiece(), record.getOrigin());
		if (record.getKilled() != null) {
			chessBoard.setPiece(record.getTarget(), record.getKilled());
		}
		currentRole = record.getPiece().getRole();
		postEvent(new GameEvent(GameEvent.REVOKE, this));
		records.removeLast();
		if (roomService.exist()) {
			if (send) {
				roomService.revoke();
				if (roomService.getRole().equals(currentRole.getEnemy())) {
					revoke(true);
				}
			}
		}
	}

	/**
	 * 判断指定的king是否能够被杀死，可以用来判断“将军”
	 * 
	 * @param king
	 *            要判断的king
	 * @return 返回true表示能够被杀死，返回false表示不能够被杀死
	 */
	public List<ChessPiece> getCheckers(ChessRole role) {
		ChessPiece king = chessBoard.getKing(role);
		List<ChessPiece> checkers = new ArrayList<ChessPiece>();
		if (king != null) {
			List<ChessPiece> pieces = chessBoard.getPieces(ApplicationUtil.getEnemy(king.getRole()));
			for (ChessPiece piece : pieces) {
				if (piece.getType() == ChessPieceType.GUARD || piece.getType() == ChessPieceType.BISHOP) {
					continue;
				}
				if (piece.getValidTargets().contains(king.getLocation())) {
					checkers.add(piece);
				}
			}
		}
		return checkers;
	}

	public boolean isChecking(ChessRole role) {
		if (role == null) {
			return false;
		}
		ChessPiece king = chessBoard.getKing(role);
		List<ChessPiece> pieces = chessBoard.getPieces(ApplicationUtil.getEnemy(king.getRole()));
		for (ChessPiece piece : pieces) {
			if (piece.getType() == ChessPieceType.GUARD || piece.getType() == ChessPieceType.BISHOP) {
				continue;
			}
			if (piece.getValidTargets().contains(king.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public boolean isChecking() {
		return !this.getCheckers(currentRole).isEmpty();
	}

	/**
	 * 获取将军的对策
	 * 
	 * @return
	 */
	public List<ChessStep> getCheckStrategies() {
		List<ChessStep> strategies = new ArrayList<ChessStep>();
		ChessPieceKing king = chessBoard.getKing(currentRole);
		int kx = king.getLocation().x, ky = king.getLocation().y;
		List<ChessPiece> checkers = getCheckers(currentRole);
		if (checkers == null || checkers.isEmpty()) {
			return strategies;
		}
		List<ChessPiece> pieces = chessBoard.getPieces(currentRole);
		// 方案一 (吃掉将军的棋子)
		for (ChessPiece checker : checkers) {
			for (ChessPiece piece : pieces) {
				// List<Point> movedLocations = piece.getNewMovedLocations();
				List<Point> validTargets = piece.getValidTargets();
				int index = validTargets.indexOf(checker.getLocation());
				if (index >= 0) {
					Point target = new Point(validTargets.get(index));
					if (!willLose(piece, target)) {
						strategies.add(new ChessStep(piece, piece.getLocation(), target));
					}
				}
			}
		}

		// 方案二 (帅将移动)
		for (Point movedLocation : king.getValidTargets()) {
			if (!willLose(king, movedLocation)) {
				strategies.add(new ChessStep(king, king.getLocation(), movedLocation));
			}
		}

		// 方案三 (设置障碍)
		for (ChessPiece checker : checkers) {
			List<Point> hinders = new ArrayList<Point>();
			int cx = checker.getLocation().x, cy = checker.getLocation().y;
			if (checker instanceof ChessPieceRook || checker instanceof ChessPieceCannon) {
				// 车炮将
				int init = 0, limit = 0;
				Point hinder = null;
				if (cx == kx) {
					init = Math.min(cy, ky) + 1;
					limit = Math.max(cy, ky);
				} else {
					init = Math.min(cx, kx) + 1;
					limit = Math.max(cx, kx);
				}
				for (int i = init; i < limit; ++i) {
					if (cx == kx) {
						hinder = new Point(cx, i);
					} else {
						hinder = new Point(i, cy);
					}
					if (!chessBoard.hasPiece(hinder)) {
						hinders.add(hinder);
					}
				}
			} else if (checker instanceof ChessPieceKnight) {
				// 马将
				if (cx == kx - 2) {
					hinders.add(new Point(cx + 1, cy));
				} else if (cx == kx + 2) {
					hinders.add(new Point(cx - 1, cy));
				} else if (cy == ky - 2) {
					hinders.add(new Point(cx, cy + 1));
				} else if (cy == ky + 2) {
					hinders.add(new Point(cx, cy - 1));
				}
			}
			for (Point hinder : hinders) {
				for (ChessPiece piece : pieces) {
					if (piece.getValidTargets().contains(hinder)) {
						if (!willLose(piece, hinder)) {
							strategies.add(new ChessStep(piece, piece.getLocation(), hinder));
						}
					}
				}
			}
		}

		// 方案4 (撤炮架)
		for (ChessPiece checker : checkers) {
			int cx = checker.getLocation().x, cy = checker.getLocation().y;
			ChessPiece pivot = null;
			if (checker instanceof ChessPieceCannon) {
				int init = 0, limit = 0;
				if (cx == kx) {
					init = Math.min(cy, ky) + 1;
					limit = Math.max(cy, ky);
				} else {
					init = Math.min(cx, kx) + 1;
					limit = Math.max(cx, kx);
				}
				for (int i = init; i < limit; ++i) {
					if (cx == kx) {
						pivot = chessBoard.getPiece(cx, i);
					} else {
						pivot = chessBoard.getPiece(i, cy);
					}
					if (pivot != null) {
						if (pivot.getRole() != king.getRole()) {
							pivot = null;
						}
						break;
					}
				}
			}
			if (pivot != null) {
				for (Point movedLocation : pivot.getValidTargets()) {
					if (!willLose(pivot, movedLocation)) {
						strategies.add(new ChessStep(pivot, pivot.getLocation(), movedLocation));
					}
				}
			}
		}
		return strategies;
	}

	/**
	 * 判断游戏是否已结束
	 * 
	 * @return
	 */
	public boolean isGameover() {
		return winner != null;
	}

	/**
	 * 随机走棋
	 */
	public void randomMove() {
		if (winner != null) {
			return;
		}

		if (isChecking(currentRole)) {
			List<ChessStep> steps = getCheckStrategies();
			if (steps.isEmpty()) {
				return;
			}
			ChessStep step = steps.get(ApplicationUtil.random(steps.size()));
			move(step.getPiece(), step.getTarget());
			return;
		}
		List<ChessPiece> pieces = chessBoard.getPieces(currentRole);
		for (ChessPiece piece : pieces) {
			for (Point target : piece.getValidTargets()) {
				ChessPiece enemy = chessBoard.getPiece(target);
				if (enemy != null && enemy.getRole() != piece.getRole()) {
					if (!willLose(piece, target)) {
						move(piece, target);
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
		} while (willLose(piece, target));
		move(piece, target);
	}
	
	/**
	 * 通知可以走下一步了
	 */
	public void notifyNext() {
		postEvent(new PlayerEvent(PlayerEvent.PLAY, this, currentRole));
	}

	/**
	 * 发送事件
	 * 
	 * @param e
	 */
	public void postEvent(ChessPieceEvent e) {
		for (ChessPieceListener l : listeners.getListeners(ChessPieceListener.class)) {
			switch (e.getId()) {
			case ChessPieceEvent.PIECE_SELECTED:
				l.onSelected(e);
				break;
			case ChessPieceEvent.PIECE_DESELECTED:
				l.onDeselected(e);
				break;
			case ChessPieceEvent.PIECE_DIED:
				l.onDied(e);
				break;
			case ChessPieceEvent.PIECE_MOVED:
				l.onMoved(e);
				break;
			}
		}
	}

	public void postEvent(GameEvent e) {
		for (GameListener l : listeners.getListeners(GameListener.class)) {
			switch (e.getId()) {
			case GameEvent.REVOKE:
				l.onRevoke(e);
				break;
			}
		}
	}
	
	public void postEvent(PlayerEvent e) {
		for (PlayerListener l : listeners.getListeners(PlayerListener.class)) {
			switch (e.getId()) {
			case PlayerEvent.PLAY:
				l.onPlay(e);
				break;
			}
		}
	}
	
	@Override
	public void onJoin() {
	}

	@Override
	public void onMove(Point origin, Point target) {
		move(chessBoard.getPiece(origin), target);
	}

	@Override
	public void onRevoke() {
		revoke(false);
	}

}