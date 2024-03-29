package priv.hypo.chess.model;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 棋盘
 * 
 * @author hypo
 */
public class ChessBoard implements java.io.Serializable {
	private static final long serialVersionUID = -4201670298962888493L;

    // 棋盘宽度
	public static final int WIDTH = 9;

    // 棋盘高度
	public static final int HEIGHT = 10;

    // 双方分界线
    public static final double BOUNDARY = 4.5;

    // 宫位置x值
    public static final int PALACE_X = 4;

    // 1号宫位置y值
    public static final int PALACE_Y1 = 1;

    // 2号宫位置y值
    public static final int PALACE_Y2 = 8;

	public static final Dimension DEFAULT_SIZE = new Dimension(WIDTH, HEIGHT);

	// 棋子
	private ChessPiece[][] pieces = new ChessPiece[WIDTH][HEIGHT];
	// 将帅
	private Map<ChessRole, ChessPieceKing> kings = new HashMap<ChessRole, ChessPieceKing>();
	// 分角色暂存的棋子
	private Map<ChessRole, List<ChessPiece>> rolePieces = new HashMap<ChessRole, List<ChessPiece>>();
	private List<ChessPiece> allPieces = new ArrayList<ChessPiece>();

	// 棋盘布局版本号，棋盘布局变更后，版本号加1
	private long layoutVersion = Long.MIN_VALUE;
	// 旧的布局版本号，用来标识棋盘的一些资源的可用性
	private long oldLayoutVersion = layoutVersion;

	// Constructors
	public ChessBoard() {
	}

	/** 获取棋盘的大小 */
	public Dimension getSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	/** 获取棋盘布局版本号 */
	public long getLayoutVersion() {
		return layoutVersion;
	}

	/**
	 * 判断一个点是否能够对应棋盘中的一个位置
	 * 
	 * @param x
	 *            点的x值
	 * @param y
	 *            点的y值
	 * @return 如果该点能够在棋盘中对应一个位置则返回true，否则返回false
	 */
	public boolean inBoard(int x, int y) {
		return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT;
	}

	/**
	 * 判断一个点是否能够对应棋盘中的一个位置
	 * 
	 * @param point
	 *            要判断的点
	 * @return 如果该点能够在棋盘中对应一个位置则返回true，否则返回false
	 */
	public boolean inBoard(Point point) {
		if (point == null) {
			return false;
		}
		return inBoard(point.x, point.y);
	}

	/**
	 * 判断一个旗子是否在本棋盘内
	 * 
	 * @param piece
	 *            要判断的棋子
	 * @return 如果该棋子在本棋盘内则返回true，否则返回false
	 */
	public boolean inBoard(ChessPiece piece) {
		return piece != null && piece.getChessBoard() == this;
	}

	/**
	 * 判断棋盘的棋子中是否存在指定的棋子
	 * 
	 * @param piece
	 *            查找的棋子
	 * @return 如果棋盘中的棋子包含查找的棋子则返回true，否则返回false
	 */
	public boolean contains(ChessPiece piece) {
		return getPieces().contains(piece);
	}

	/**
	 * 判断一个点是否在棋盘的上半部分
	 * 
	 * @param x
	 *            点的x值
	 * @param y
	 *            点的y值
	 * @return 如果该点在棋盘的上半部分则返回true，否则返回false
	 */
	public boolean inBoardUpBound(int x, int y) {
		return (x >= 0 && y >= 0) && (x < WIDTH && y < HEIGHT / 2);
	}

	/**
	 * 判断一个点是否在棋盘的上半部分
	 * 
	 * @param point
	 *            要判断的点
	 * @return 如果该点在棋盘的上半部分则返回true，否则返回false
	 */
	public boolean inBoardUpBound(Point point) {
		if (point == null) {
			return false;
		}
		return inBoardUpBound(point.x, point.y);
	}

	/**
	 * 判断一个点是否在棋盘的下半部分
	 * 
	 * @param x
	 *            点的x值
	 * @param y
	 *            点的y值
	 * @return 如果该点在棋盘的下半部分则返回true，否则返回false
	 */
	public boolean inBoardDownBound(int x, int y) {
		return (x >= 0 && y >= HEIGHT / 2) && (x < WIDTH && y < HEIGHT);
	}

	/**
	 * 判断一个点是否在棋盘的下半部分
	 * 
	 * @param location
	 *            要判断的点
	 * @return 如果该点在棋盘的下半部分则返回true，否则返回false
	 */
	public boolean inBoardDownBound(Point location) {
		if (location == null) {
			return false;
		}
		return inBoardDownBound(location.x, location.y);
	}

	/**
	 * 判断两个点是否在棋盘中的同一半
	 * 
	 * @param x1
	 *            第一个点的x值
	 * @param y1
	 *            第一个点的y值
	 * @param x2
	 *            第二个点的x值
	 * @param y2
	 *            第二个点的y值
	 * @return 如果两个点在棋盘中的同一半则返回true，否则返回false
	 */
	public boolean inBoardSameBound(int x1, int y1, int x2, int y2) {
		if (inBoardUpBound(x1, y1)) {
			return inBoardUpBound(x2, y2);
		}
        if (inBoardDownBound(x1, y1)) {
			return inBoardDownBound(x2, y2);
		}
        return false;
	}

	/**
	 * 判断两个点是否在棋盘中的同一半
	 * 
	 * @param point1
	 *            第一个点
	 * @param point2
	 *            第二个点
	 * @return 如果两个点在棋盘中的同一半则返回true，否则返回false
	 */
	public boolean inBoardSameBound(Point point1, Point point2) {
		if (point1 == null || point2 == null) {
			return false;
		}
		return inBoardSameBound(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * 判断一个点是否在棋盘的上宫内
	 * 
	 * @param x
	 *            点的x值
	 * @param y
	 *            点的y值
	 * @return 如果该点在棋盘的上宫内则返回true，否则返回false
	 */
	public boolean inBoardUpPalace(int x, int y) {
		return (x >= 3 && y >= 0) && (x < 6 && y < 3);
	}

	/**
	 * 判断一个点是否在棋盘的上宫内
	 * 
	 * @param point
	 *            要判断的点
	 * @return 如果该点在棋盘的上宫内则返回true，否则返回false
	 */
	public boolean inBoardUpPalace(Point point) {
		if (point == null) {
			return false;
		}
		return inBoardUpPalace(point.x, point.y);
	}

	/**
	 * 判断一个点是否在棋盘的下宫内
	 * 
	 * @param x
	 *            点的x值
	 * @param y
	 *            点的y值
	 * @return 如果该点在棋盘的下宫内则返回true，否则返回false
	 */
	public boolean inBoardDownPalace(int x, int y) {
		return (x >= 3 && y >= 7) && (x < 6 && y < HEIGHT);
	}

	/**
	 * 判断一个点是否在棋盘的下宫内
	 * 
	 * @param point
	 *            要判断的点
	 * @return 如果该点在棋盘的下宫内则返回true，否则返回false
	 */
	public boolean inBoardDownPalace(Point point) {
		if (point == null) {
			return false;
		}
		return inBoardDownPalace(point.x, point.y);
	}

	/**
	 * 判断两个点是否在棋盘的同一个宫内
	 * 
	 * @param x1
	 *            第一个点的x值
	 * @param y1
	 *            第一个点的y值
	 * @param x2
	 *            第二个点的x值
	 * @param y2
	 *            第二个点的y值
	 * @return 如果两个点在棋盘的同一个宫内则返回true，否则返回false
	 */
	public boolean inBoardSamePalace(int x1, int y1, int x2, int y2) {
		if (inBoardUpPalace(x1, y1)) {
			return inBoardUpPalace(x2, y2);
		} else if (inBoardDownPalace(x1, y1)) {
			return inBoardDownPalace(x2, y2);
		} else {
			return false;
		}
	}

	/**
	 * 判断两个点是否在棋盘的同一个宫内
	 * 
	 * @param point1
	 *            第一个点
	 * @param point2
	 *            第二个点
	 * @return 如果两个点在棋盘的同一个宫内则返回true，否则返回false
	 */
	public boolean inBoardSamePalace(Point point1, Point point2) {
		if (point1 == null || point2 == null) {
			return false;
		}
		return inBoardSamePalace(point1.x, point1.y, point2.x, point2.y);
	}

	/**
	 * 判断在当前棋盘中指定的位置是否有棋子
	 * 
	 * @param location
	 *            要判断的位置
	 * @return 如果在指定位置有棋子则返回true，否则返回false
	 */
	public boolean hasPiece(Point location) {
		return getPiece(location) != null;
	}

	/**
	 * 判断在当前棋盘中指定的位置是否有棋子
	 * 
	 * @param x
	 *            要判断的位置的x值
	 * @param y
	 *            要判断的位置的y值
	 * @return 如果在指定位置有棋子则返回true，否则返回false
	 */
	public boolean hasPiece(int x, int y) {
		return getPiece(x, y) != null;
	}

	/**
	 * 在棋盘指定的位置设置一个棋子
	 * 
	 * @param x
	 *            棋子的位置的x值
	 * @param y
	 *            棋子的位置的y值
	 * @param piece
	 *            要添加到棋盘的棋子
	 */
	public void setPiece(int x, int y, ChessPiece piece) {
		if (inBoard(x, y)) {
			if (piece != null) {
				piece.setLocation(x, y);
				piece.setChessBoard(this);
			}
			pieces[x][y] = piece;
			++layoutVersion;
		}
	}

	/**
	 * 在棋盘指定的位置设置一个棋子
	 * 
	 * @param location
	 *            棋子的位置
	 * @param piece
	 *            要添加到棋盘的棋子
	 */
	public void setPiece(Point location, ChessPiece piece) {
		if (location == null) {
			return;
		}
		setPiece(location.x, location.y, piece);
	}

	/**
	 * 在棋盘中添加一个棋子
	 * 
	 * @param piece
	 *            要添加的棋子
	 */
	public void addPiece(ChessPiece piece) {
		if (piece == null) {
			return;
		}
		setPiece(piece.getLocation().x, piece.getLocation().y, piece);
	}

	/**
	 * 在棋盘中添加一组棋子
	 * 
	 * @param pieces
	 *            要添加的棋子
	 */
	public void addPieces(Collection<ChessPiece> pieces) {
		if (pieces == null) {
			return;
		}
		for (ChessPiece piece : pieces) {
			addPiece(piece);
		}
	}

	/**
	 * 将棋子从棋盘移除，使棋子与棋盘失去联系
	 * 
	 * @param piece
	 *            要移除的棋子
	 */
	public void removePiece(ChessPiece piece) {
		if (piece == null) {
			return;
		}
		if (getPiece(piece.getLocation()) != piece) {
			return;
		}
		setPiece(piece.getLocation(), null);
		piece.setLocation(null);
		piece.setChessBoard(null);
	}

	/**
	 * 获取棋盘中指定位置的棋子，如果该位置没有棋子则返回null
	 * 
	 * @param x
	 *            棋子的位置的x值
	 * @param y
	 *            棋子的位置的y值
	 * @return 指定位置的棋子
	 */
	public ChessPiece getPiece(int x, int y) {
		if (!inBoard(x, y)) {
            return null;
        }
        return pieces[x][y];
	}

	/**
	 * 获取棋盘中指定位置的棋子，如果该位置没有棋子则返回null
	 * 
	 * @param location
	 *            棋子的位置
	 * @return 指定位置的棋子
	 */
	public ChessPiece getPiece(Point location) {
		if (location == null) {
			return null;
		}
		return getPiece(location.x, location.y);
	}

	/**
	 * 将棋盘中的棋子移动到指定的位置
	 * 
	 * @param piece
	 *            要移动的棋子
	 * @param x
	 *            目标位置的x值
	 * @param y
	 *            目标位置的y值
	 */
	public void move(ChessPiece piece, int x, int y) {
		if (piece == null) {
			return;
		}
		if (inBoard(x, y)) {
			removePiece(piece);
			setPiece(x, y, piece);
		}
	}

	/**
	 * 将棋盘中的棋子移动到指定的位置
	 * 
	 * @param piece
	 *            要移动的棋子
	 * @param target
	 *            要移动的棋子
	 */
	public void move(ChessPiece piece, Point target) {
		if (target == null) {
			return;
		}
		move(piece, target.x, target.y);
	}

    /**
     * 按照指定的步骤信息移动棋盘中的棋子
     *
     * @param step 步骤信息
     */
    public void move(ChessStep step) {
        if (step == null) {
            return;
        }
        move(step.getPiece(), step.getTarget());
    }

	/**
	 * 清空棋盘中的所有棋子
	 */
	public void clear() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				pieces[i][j] = null;
			}
		}
		++layoutVersion;
	}

	/**
	 * 刷新棋盘布局版本号
	 */
	private void refreshLayoutVersion() {
		if (oldLayoutVersion != layoutVersion) {
			rolePieces.clear();
			kings.clear();
			allPieces.clear();
			oldLayoutVersion = layoutVersion;
		}
	}

	/**
	 * 获取指定角色的棋子
	 * 
	 * @param role
	 *            角色（红方或黑方）
	 * @return 指定角色的棋子
	 */
	public List<ChessPiece> getPieces(ChessRole role) {
		if (role == null) {
			return null;
		}

		refreshLayoutVersion();

		if (!rolePieces.containsKey(role)) {
			List<ChessPiece> rolePieces = new ArrayList<ChessPiece>();
			for (ChessPiece[] row : pieces) {
				for (ChessPiece piece : row) {
					if (piece != null && role.equals(piece.getRole())) {
						rolePieces.add(piece);
					}
				}
			}
			this.rolePieces.put(role, rolePieces);
		}

		return rolePieces.get(role);
	}

	/**
	 * 获取棋盘上所有的棋子
	 * 
	 * @return 棋盘中所有的棋子
	 */
	public List<ChessPiece> getPieces() {
		List<ChessPiece> pieces = new ArrayList<ChessPiece>(32);
		for (ChessRole role : ChessRole.values()) {
			pieces.addAll(getPieces(role));
		}
		return pieces;
	}

	/**
	 * 查找将帅
	 *
	 * @param role 将/帅角色
	 * @return
	 */
	public ChessPieceKing getKing(ChessRole role) {
		if (role == null) {
			return null;
		}
		refreshLayoutVersion();

		if (!kings.containsKey(role)) {
			List<ChessPieceKing> kings = new ArrayList<ChessPieceKing>();
			for (ChessPiece piece : getPieces(role)) {
				if (piece instanceof ChessPieceKing) {
					kings.add((ChessPieceKing) piece);
				}
			}
			if (kings.isEmpty()) {
				throw new RuntimeException(role + " king not found.");
			} else if (kings.size() > 1) {
				throw new RuntimeException(role + " has multiple king.");
			} else {
				this.kings.put(role, kings.get(0));
			}
		}

		return kings.get(role);
	}
}