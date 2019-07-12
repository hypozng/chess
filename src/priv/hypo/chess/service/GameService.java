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
import priv.hypo.chess.model.ChessPieceKing;
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
	private ChessBoard chessBoard;

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
		if (selected == null) {
            return;
		}
        ChessPiece selected = this.selected;
        this.selected = null;
        postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_DESELECTED, selected, selected.getLocation()));
	}

    /**
     * 判断按照指定步骤走棋是否有效
     *  若按照指定步骤走棋会导致被将军则视为无效的步骤
     * @param step 棋子移动信息
     * @return 返回true表示步骤有效，返回false表示步骤无效
     */
    public boolean isValidStep(ChessStep step) {
        if (step == null) {
            return false;
        }
        ChessPiece enemy = chessBoard.getPiece(step.getTarget());
        chessBoard.move(step);
        boolean flag = !kingHasThreaten(step.getPiece().getRole());
        chessBoard.move(step.back());
        if (enemy != null) {
            chessBoard.setPiece(step.getTarget(), enemy);
        }
        return flag;
    }

    /**
	 * 移动一个棋子
	 * @param step 棋子移动信息
	 */
	public void move(ChessStep step) {
		if (winner != null || step == null) {
			return;
		}
		if (!isValidStep(step)) {
			deselect();
			return;
		}
        ChessPiece killed = chessBoard.getPiece(step.getTarget());
		chessBoard.move(step);
		postEvent(new ChessPieceEvent(ChessPieceEvent.PIECE_MOVED, step.getPiece(), step.getOrigin()));
		kill(killed);
		deselect();
		records.addLast(new ChessRecord(step, killed));
        if (roomService.exist() && roomService.getRole().equals(currentRole)) {
            roomService.movePiece(step);
        }

        checkOver();
		currentRole = currentRole.getEnemy();
	}

    /**
     * 检测是否结束
     * @return
     */
    public void checkOver() {
        // 被将军，没有任何有效的解决方案
        if (kingHasThreaten(currentRole.getEnemy())) {
            if (solveKingThreaten(currentRole.getEnemy()).isEmpty()) {
                winner = currentRole;
            }
        }

        // 进剩下将/帅，将/帅无法移动到安全位置
        if (chessBoard.getPieces(currentRole.getEnemy()).size() == 1) {
            ChessPiece king = chessBoard.getKing(currentRole.getEnemy());
            for (Point target : king.getValidTargets()) {
                ChessStep step = new ChessStep(king, target);
                if (isValidStep(step)) {
                    return;
                }
            }
            winner = currentRole;
        }
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
	 * @param x 棋盘点击位置的x值
	 * @param y 棋盘点击位置的y值
	 */
	public void click(int x, int y) {
		if (winner != null || chessBoard == null || !chessBoard.inBoard(x, y)) {
			return;
		}
        Point target = new Point(x, y);
		ChessPiece piece = chessBoard.getPiece(target);
        if (selected == piece) {
            deselect();
            return;
        }
        if (piece != null && piece.getRole().equals(currentRole)) {
            select(piece);
            return;
        }
        if (selected != null && selected.isValidTarget(target)) {
            move(new ChessStep(selected, target));
            return;
        }
	}

	/**
	 * 悔棋
	 */
	public void revoke(boolean send) {
		deselect();
		if (records.isEmpty()) {
			return;
		}
        winner = null;
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
	 * 获取将/帅的威胁 如果棋子能够击杀将/帅则视为威胁
	 * @param role 将/帅的角色
     * @param single 仅查找一个就直接返回
	 * @return 返回能够击杀将/帅的棋子
	 */
	public List<ChessPiece> getKingThreatens(ChessRole role, boolean single) {
		ChessPiece king = chessBoard.getKing(role);
		List<ChessPiece> threatens = new ArrayList<ChessPiece>();
		if (king != null) {
			List<ChessPiece> pieces = chessBoard.getPieces(role.getEnemy());
			for (ChessPiece piece : pieces) {
                if (piece.getType().equals(ChessPieceType.GUARD)
                        || piece.getType().equals(ChessPieceType.BISHOP)) {
                    continue;
                }
                if (piece.getValidTargets().contains(king.getLocation())) {
                    threatens.add(piece);
                    if (single) {
                        return threatens;
                    }
                }
            }
		}
		return threatens;
	}

    /**
     *
     * @param role
     * @return
     */
    public List<ChessPiece> getKingThreatens(ChessRole role) {
        return getKingThreatens(role, false);
    }

    /**
     * 判断是否被将军
     * @param role
     *      判断的角色
     * @return 如果指定的角色正在被将军则返回true，否则返回false
     */
	public boolean kingHasThreaten(ChessRole role) {
		if (role == null) {
			return false;
		}
        return !getKingThreatens(role, true).isEmpty();
	}

    /**
     * 判断当前的角色是否正在被将军
     * @return 如果当前的角色正在被将军则返回true，否则返回false
     */
	public boolean kingHasThreaten() {
		return kingHasThreaten(currentRole);
	}

	/**
	 * 获取将军的对策
	 * 
	 * @return
	 */
	public List<ChessStep> solveKingThreaten(ChessRole role) {
        if (role == null) {
            return null;
        }
        List<ChessStep> steps = new ArrayList<ChessStep>();
        List<ChessPiece> threatens = getKingThreatens(role);
        if (threatens == null || threatens.isEmpty()) {
            return steps;
        }

        ChessPieceKing king = chessBoard.getKing(role);
		int kx = king.getLocation().x, ky = king.getLocation().y;
		List<ChessPiece> pieces = chessBoard.getPieces(currentRole);

		// 方案一 (吃掉将军的棋子)
		for (ChessPiece threaten : threatens) {
			for (ChessPiece piece : pieces) {
                if (piece.getValidTargets().contains(threaten.getLocation())) {
                    ChessStep step = new ChessStep(piece, threaten.getLocation());
                    if (isValidStep(step)) {
                        steps.add(step);
                    }
                }
			}
		}

		// 方案二 (帅将移动)
		for (Point target : king.getValidTargets()) {
            ChessStep step = new ChessStep(king, target);
			if (isValidStep(step)) {
				steps.add(step);
			}
		}

		// 方案三 (设置障碍)
		for (ChessPiece threaten : threatens) {
			List<Point> hinders = new ArrayList<Point>();
			int tx = threaten.getLocation().x, ty = threaten.getLocation().y;
            switch (threaten.getType()) {
                case ROOK:
                case CANNON:
                    // 车炮将
                    boolean flag = tx == kx;
                    int start = Math.min(flag ? ty : tx, flag ? ky : kx) + 1;
                    int end = Math.max(flag ? ty : tx, flag ? ky : kx);
                    for (int i = start; i < end; ++i) {
                        int hx = flag ? tx : i, hy = flag ? i : ty;
                        Point hinder = new Point(hx, hy);
                        if (!chessBoard.hasPiece(hinder)) {
                            hinders.add(hinder);
                        }
                    }
                    break;
                case KNIGHT:
                    // 马将
                    int hx = kx + (tx > kx ? 1 : -1);
                    int hy = ky + (ty > ky ? 1 : -1);
                    hinders.add(new Point(hx, hy));
                    break;
            }
			for (Point hinder : hinders) {
				for (ChessPiece piece : pieces) {
					if (piece.getValidTargets().contains(hinder)) {
                        ChessStep step = new ChessStep(piece, hinder);
						if (isValidStep(step)) {
							steps.add(step);
						}
					}
				}
			}
		}

		// 方案4 (撤炮架)
		for (ChessPiece threaten : threatens) {
            if (!threaten.getType().equals(ChessPieceType.CANNON)) {
                continue;
            }
			int tx = threaten.getLocation().x, ty = threaten.getLocation().y;
            boolean flag = tx == kx;
            int start = Math.min(flag ? ty : tx, flag ? ky : kx) + 1;
            int end = Math.max(flag ? ty : tx, flag ? ky : kx);
            for (int i = start; i < end; ++i) {
                int px = flag ? tx : i, py = flag ? i : ty;
                ChessPiece pivot = chessBoard.getPiece(px, py);
                if (pivot == null) {
                    continue;
                }
                if (!pivot.getRole().equals(king.getRole())) {
                    break;
                }
                for (Point target : pivot.getValidTargets()) {
                    ChessStep step = new ChessStep(pivot, target);
                    if (isValidStep(step)) {
                        steps.add(step);
                    }
                }
            }
		}
        System.out.println("======== solve king threaten steps ========");
        for (ChessStep step : steps) {
            System.out.println(step);
        }
        System.out.println("================================");
		return steps;
	}

    public List<ChessStep> solveKingThreaten() {
        return solveKingThreaten(currentRole);
    }

	/**
	 * 判断游戏是否已结束
	 * 
	 * @return
	 */
	public boolean isOver() {
		return winner != null;
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
		move(new ChessStep(chessBoard.getPiece(origin), target));
	}

	@Override
	public void onRevoke() {
		revoke(false);
	}

}