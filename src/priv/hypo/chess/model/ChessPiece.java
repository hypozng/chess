package priv.hypo.chess.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 棋子
 * @author Hypo
 */
public abstract class ChessPiece implements java.io.Serializable {
	private static final long serialVersionUID = 8028585425019005393L;
	
	public transient static final String SF_ROLE = "role"; 
	public transient static final String SF_TYPE = "type"; 
	public transient static final String SF_LOCATION_X = "location.x"; 
	public transient static final String SF_LOCATION_Y = "location.y"; 
	
	// Fields
	// 有效的目标点 棋子走一步能够移动到的点则是有效的目标点
	private List<Point> validTargets = new ArrayList<Point>();
	
	// 棋子的位置
	protected Point location = new Point();
	
	// 棋子的类型
	protected ChessPieceType type;
	
	// 棋子的属性
	protected ChessRole role = ChessRole.RED;
	
	// 所属的棋盘
	protected ChessBoard chessBoard;
	
	// 棋盘布局版本号
	private long boardLayoutVersion;
	
	// Constructors
	protected ChessPiece(ChessPieceType type, ChessRole role) {
		this.type = type;
		this.role = role;
	}
	
	// Property accessors
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public void setLocation(int x, int y) {
		this.location = new Point(x, y);
	}

	public ChessBoard getChessBoard() {
		return chessBoard;
	}
	
	public void setChessBoard(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
	}
	
	public ChessPieceType getType() {
		return type;
	}

	public ChessRole getRole() {
		return role;
	}
	
	public void setRole(ChessRole role) {
		this.role = role;
	}
	
	/**
	 * 获取当前棋盘中棋子的有效目标点
	 * 棋子走一步能够到达的点则是棋子的有效目标点
	 * @return 棋子所在棋盘中所有的有效目标点
	 */
	public List<Point> getValidTargets() {
		if(chessBoard == null) {
			return null;
		}
		if(chessBoard.getLayoutVersion() != boardLayoutVersion) {
			validTargets = new ArrayList<Point>();
			findValidTargets(validTargets);
            removeInvalidTargets();
			boardLayoutVersion = chessBoard.getLayoutVersion();
		}
		return validTargets;
	}

    /**
     * 删除无效的目标点（若目标点被己方棋子占用则视为无效）
     */
    private void removeInvalidTargets() {
        Iterator<Point> iter = validTargets.iterator();
        while (iter.hasNext()) {
            Point target = iter.next();
            ChessPiece piece = chessBoard.getPiece(target);
            if (piece != null && piece.getRole().equals(role)) {
                iter.remove();
            }
        }
    }
	
	/**
	 * 判断当前的棋子是否可以移动到指定的位置
	 * @param x 目标位置的x坐标
	 * @param y 目标位置的y坐标
	 * @return 如果棋子能够移动到目标位置则返回true，否则返回false
	 */
	public boolean isValidTarget(int x, int y){
		return getValidTargets().contains(new Point(x, y));
	}
	
	/**
	 * 判断棋子是否能够移动到目标位置
	 * @param location 目标位置
	 * @return
	 */
	public boolean isValidTarget(Point location) {
		return getValidTargets().contains(location);
	}
	
	/**
	 * 查找棋子的有效目标点，并将找到的目标点放到容器中
	 * @param validTargets 用于存放有效目标点的容器
	 */
	public abstract void findValidTargets(Collection<Point> validTargets);
	
	public String toString() {
		return String.format("[%5s, %6s%s]", role, type,
				location == null ? "" : String.format(", x=%d, y=%d", location.x, location.y));
		
	}	
}