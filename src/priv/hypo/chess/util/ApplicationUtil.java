package priv.hypo.chess.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessPieceType;
import priv.hypo.chess.model.ChessRole;

/**
 * 程序工具
 * 
 * @author Hypo
 */
public class ApplicationUtil {
	public static final Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	/** 随机数生成器 */
	public static final Random randomNumberGenerator = new Random();
	
	/** 路径分隔符 */
	public static final String PATH_SEPARATOR = "/";

	/** 根路径 */
	public static final String ROOT_PATH = PATH_SEPARATOR;

	/** 图片资源路径 */
	public static final String IMAGE_PATH = ROOT_PATH + "image";

	/** 数据资源路径 */
	public static final String DATA_PATH = ROOT_PATH + "data";

	/** 配置资源路径 */
	public static final String CONFIG_PATH = ROOT_PATH + "config";

	// 象棋棋子图片集合
	private static Map<ChessRole, Map<ChessPieceType, Image>> pieceImages = new HashMap<ChessRole, Map<ChessPieceType, Image>>();
	// 象棋图片集合
	private static Map<String, Image> images = new HashMap<String, Image>();

	static {
		try {
			init();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * 初始化象棋工具类
	 */
	private static void init() {
		Map<ChessPieceType, Image> redPieceImages = new HashMap<ChessPieceType, Image>();
		redPieceImages.put(ChessPieceType.ROOK, getImageOnToolkit("red_rook.gif"));
		redPieceImages.put(ChessPieceType.KNIGHT, getImageOnToolkit("red_knight.gif"));
		redPieceImages.put(ChessPieceType.BISHOP, getImageOnToolkit("red_bishop.gif"));
		redPieceImages.put(ChessPieceType.GUARD, getImageOnToolkit("red_guard.gif"));
		redPieceImages.put(ChessPieceType.KING, getImageOnToolkit("red_king.gif"));
		redPieceImages.put(ChessPieceType.CANNON, getImageOnToolkit("red_cannon.gif"));
		redPieceImages.put(ChessPieceType.PAWN, getImageOnToolkit("red_pawn.gif"));
		pieceImages.put(ChessRole.RED, redPieceImages);

		Map<ChessPieceType, Image> blackPieceImages = new HashMap<ChessPieceType, Image>();
		blackPieceImages.put(ChessPieceType.ROOK, getImageOnToolkit("black_rook.gif"));
		blackPieceImages.put(ChessPieceType.KNIGHT, getImageOnToolkit("black_knight.gif"));
		blackPieceImages.put(ChessPieceType.BISHOP, getImageOnToolkit("black_bishop.gif"));
		blackPieceImages.put(ChessPieceType.GUARD, getImageOnToolkit("black_guard.gif"));
		blackPieceImages.put(ChessPieceType.KING, getImageOnToolkit("black_king.gif"));
		blackPieceImages.put(ChessPieceType.CANNON, getImageOnToolkit("black_cannon.gif"));
		blackPieceImages.put(ChessPieceType.PAWN, getImageOnToolkit("black_pawn.gif"));
		pieceImages.put(ChessRole.BLACK, blackPieceImages);

		images.put("chessBoard", getImageOnToolkit("chess_board.jpg"));
		images.put("selected", getImageOnToolkit("selected.gif"));
		images.put("logo", getImageOnToolkit("logo.png"));
	}

	/**
	 * 获取res包下的文件输入流
	 * 
	 * @param fileName
	 *            文件名
	 * @return res包下的文件输入流
	 */
	public static InputStream getResourceAsStream(String fileName) {
		return ApplicationUtil.class.getResourceAsStream(ROOT_PATH + fileName);
	}

	/**
	 * 获取数据资源
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream getDataResourceAsStream(String fileName) {
		return ApplicationUtil.class.getResourceAsStream(DATA_PATH + PATH_SEPARATOR + fileName);
	}

	/**
	 * 获取配置资源文件
	 * 
	 * @param fileName
	 *            配置资源文件名
	 * @return
	 */
	public static InputStream getConfigResourceAsStream(String fileName) {
		return ApplicationUtil.class.getResourceAsStream(CONFIG_PATH + PATH_SEPARATOR + fileName);
	}

	/**
	 * 获取res包下的图片文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return res包下的图片文件
	 */
	public static Image getImageOnToolkit(String fileName) {
		return toolkit.getImage(ApplicationUtil.class.getResource(IMAGE_PATH + PATH_SEPARATOR + fileName));
	}

	/**
	 * 获取象棋图片集合中指定key的图片
	 * 
	 * @param name
	 *            图片的名字
	 * @return
	 */
	public static Image getImage(String name) {
		return images.get(name);
	}

	/**
	 * 根据角色和棋子类型获取相对应的棋子图片
	 * 
	 * @param role
	 *            角色
	 * @param type
	 *            棋子类型
	 * @return 对应的棋子图片
	 */
	public static Image getImage(ChessRole role, ChessPieceType type) {
		if (role == null || type == null) {
			return null;
		}
		return pieceImages.get(role).get(type);
	}

	/**
	 * 根据棋子获取对应的图片
	 * 
	 * @param piece
	 *            棋子
	 * @return 棋子对应的图片
	 */
	public static Image getImage(ChessPiece piece) {
		if (piece == null) {
			return null;
		}
		return getImage(piece.getRole(), piece.getType());
	}

	/**
	 * 获取角色对应的敌对方
	 * 
	 * @param role
	 *            角色
	 * @return 敌对方角色
	 */
	public static ChessRole getEnemy(ChessRole role) {
		switch (role) {
		case BLACK:
			return ChessRole.RED;
		case RED:
			return ChessRole.BLACK;
		default:
			break;
		}
		return null;
	}

	/**
	 * 获取棋盘坐标
	 * 
	 * @param board
	 *            棋盘的范围
	 * @param piece
	 *            棋子的大小
	 * @param point
	 *            要转换的绘制坐标
	 * @return 棋盘坐标
	 */
	public static Point getBoardCoord(Rectangle board, Dimension piece, Point point) {
		if (board == null || piece == null || point == null) {
			return null;
		}
		int x = (point.x - board.x) / piece.width;
		int y = (point.y - board.y) / piece.height;
		return new Point(x, y);
	}

	/**
	 * 获取绘制坐标
	 * 
	 * @param board
	 *            棋盘的范围
	 * @param piece
	 *            棋子的大小
	 * @param point
	 *            要转换的棋盘坐标
	 * @return 绘制坐标
	 */
	public static Point getDrawCoord(Rectangle board, Dimension piece, Point point) {
		if (board == null || piece == null || point == null) {
			return null;
		}
		int x = board.x + point.x * piece.width;
		int y = board.y + point.y * piece.height;
		return new Point(x, y);
	}
	
	/**
	 * 获取随机数
	 * @param min 最小值
	 * @param max 最大值
	 * @return 指定范围的随机数
	 */
	public static int random(int min, int max) {
		return randomNumberGenerator.nextInt(max - min + 1) + min;
	}
	
	/**
	 * 获取 0 - (bound - 1)范围内的随机数
	 * @param bound 随机数的范围
	 * @return 指定范围的随机数
	 */
	public static int random(int bound) {
		return random(0, bound - 1);
	}
	
	/**
	 * 从集合中随机获取一个成员
	 * @param list 集合
	 * @return 随机获取的成员
	 */
	public static <T> T randomGet(List<T> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(random(list.size()));
	}
}