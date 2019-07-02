package priv.hypo.chess.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;

import priv.hypo.chess.common.Callback;
import priv.hypo.chess.common.Global;
import priv.hypo.chess.component.CanvasPanel;
import priv.hypo.chess.event.ChessPieceEvent;
import priv.hypo.chess.event.GameEvent;
import priv.hypo.chess.listener.ChessPieceListener;
import priv.hypo.chess.listener.GameListener;
import priv.hypo.chess.model.Animation;
import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessRecord;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.ChessRoleCategory;
import priv.hypo.chess.model.Configuration;
import priv.hypo.chess.service.GameService;
import priv.hypo.chess.service.RoomService;
import priv.hypo.chess.util.AnimationUtil;
import priv.hypo.chess.util.ApplicationUtil;
import priv.hypo.chess.util.DrawUtil;
import priv.hypo.chess.util.LayoutUtil;
import priv.hypo.chess.util.ShapeUtil;

/**
 * 棋盘界面
 * 
 * @author Hypo
 */
public class ChessFrame extends JFrame implements ChessPieceListener, GameListener, ImageObserver {
	// Fields
	private static final long serialVersionUID = -1183777640924992228L;

	/** 状态文本颜色-红色 */
	private static final Color STATE_RED = new Color(156, 15, 0);
	/** 状态文本颜色-黑色 */
	private static final Color STATE_BLACK = new Color(11, 22, 25);

	private static final Font STATUS_FONT = new Font("STXingkai", Font.CENTER_BASELINE, 32);

	// 棋盘背景图片
	private static final Image BOARD_IMAGE;
	
	// 选中图片
	private static final Image SELECTED_IMAGE;
	
	// 棋子大小
	private static final Dimension PIECE_SIZE = new Dimension(54, 54);
	
	// 棋盘逻辑范围
	private static final Rectangle BOARD_BOUNDS;
	
	// 棋盘背景范围
	private static final Rectangle BACKGROUND_BOUNDS;
	
	// 游戏逻辑
	private static final GameService gameService;

	// 当前实例
	private ChessFrame instance;
	
	// 动画路径集合
	private Map<ChessPiece, Animation> animations;

	// 窗体控件
	private CanvasPanel displayCanvas;
	private JMenuBar menuBar;
	private JMenu meOption;
	private JMenuItem miStart;
	private JMenuItem miRetract;
	private JMenuItem miSet;
	private JMenu meNetwork;
	private JMenuItem miServer;
	private JMenuItem miClient;
	private JMenuItem miLeave;
	private Timer refreshDisplayTimer;

	static {
		gameService = GameService.getInstance();
		BOARD_IMAGE = ApplicationUtil.getImage("chessBoard");
		SELECTED_IMAGE = ApplicationUtil.getImage("selected");
		BOARD_BOUNDS = new Rectangle(20, 20, PIECE_SIZE.width * gameService.getChessBoard().getWidth(),
				PIECE_SIZE.height * gameService.getChessBoard().getHeight());
		BACKGROUND_BOUNDS = new Rectangle(0, 0, BOARD_BOUNDS.width + 40, BOARD_BOUNDS.height + 40);
	}

	// Constructors
	/** default constructor */
	public ChessFrame() {
		initComponent();
		initService();
	}
	
	// Methods
	/**
	 * 初始化页面布局
	 */
	private void initComponent() {
		//
		// Fields
		//
		instance = this;
		//
		// ChessFrame
		//
		this.setIconImage(ApplicationUtil.getImage("logo"));
		this.setTitle(Global.APPLICATION_NAME);
		this.setBounds(50, 50, 526, 635);
		this.setLocation(LayoutUtil.align(LayoutUtil.CENTER, ApplicationUtil.toolkit.getScreenSize(), this.getSize()));
		this.setResizable(false);
		this.setFont(Global.DEFAULT_FONT);
		this.getContentPane().setLayout(null);
		this.setType(Type.NORMAL);
//		this.setUndecorated(true);
		this.setIconImage(ApplicationUtil.getImage("logo"));
		this.getContentPane().setLayout(new BorderLayout());
		//
		// menuBar
		//
		menuBar = new JMenuBar();
		this.getContentPane().add(menuBar, BorderLayout.NORTH);
		//
		// meOption
		//
		meOption = new JMenu("选项");
		meOption.setFont(Global.DEFAULT_FONT);
		menuBar.add(meOption);
		//
		// miStart
		//
		miStart = new JMenuItem("开始");
		miStart.setFont(Global.DEFAULT_FONT);
		miStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (RoomService.getInstance().exist()) {
					gameService.init(RoomService.getInstance().getRole());
				} else {
					setConfiguration(true);
				}
			}
		});
		meOption.add(miStart);
		//
		// miSet
		//
		miSet = new JMenuItem("设置");
		miSet.setFont(Global.DEFAULT_FONT);
		miSet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setConfiguration(false);
			}
		});
		meOption.add(miSet);
		//
		// miRetract
		//
		miRetract = new JMenuItem("悔棋");
		miRetract.setFont(Global.DEFAULT_FONT);
		miRetract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameService.revoke(true);
			}
		});
		meOption.add(miRetract);
		//
		// meNetwork
		//
		meNetwork = new JMenu("多人游戏");
		meNetwork.setFont(Global.DEFAULT_FONT);
		menuBar.add(meNetwork);
		//
		// miServer
		//
		miServer = new JMenuItem("创建游戏");
		miServer.setFont(Global.DEFAULT_FONT);
		miServer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RoomService.getInstance().exist()) {
					NetworkVersusWindow win = new NetworkVersusHostWindow(instance);
					win.addWindowListener(new WindowAdapter() {
						
						@Override
						public void windowClosed(WindowEvent e) {
							RoomService.getInstance().leave();
						}
						
					});
					win.setVisible(true);
				}
			}
		});
		meNetwork.add(miServer);
		//
		// miClient
		//
		miClient = new JMenuItem("查找对局");
		miClient.setFont(Global.DEFAULT_FONT);
		miClient.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!RoomService.getInstance().exist()) {
					NetworkVersusWindow win = new NetworkVersusClientWindow(instance);
					win.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e) {
							RoomService.getInstance().leave();
						}
					});
					win.setVisible(true);
				}
			}
			
		});
		meNetwork.add(miClient);
		//
		// miLeave
		//
		miLeave = new JMenuItem("离开");
		miLeave.setFont(Global.DEFAULT_FONT);
		miLeave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				RoomService.getInstance().leave();
			}
			
		});
		//
		// displayCanvas
		//
		displayCanvas = new CanvasPanel(this.getWidth(), this.getHeight());
		displayCanvas.setBackground(Color.black);
		displayCanvas.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = new Point(e.getX(), e.getY());
				if (ShapeUtil.inRect(point, BOARD_BOUNDS)) {
					Point coord = ApplicationUtil.getBoardCoord(BOARD_BOUNDS, PIECE_SIZE, point);
					gameService.click(coord.x, coord.y);
				}
			}
			
		});
		this.getContentPane().add(displayCanvas, BorderLayout.CENTER);
		//
		// refreshDisplayTimer
		//
		refreshDisplayTimer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDisplay();
			}
			
		});
		refreshDisplayTimer.start();
	}
	
	/**
	 * 初始化逻辑
	 */
	private void initService() {
		animations = new HashMap<ChessPiece, Animation>();
		gameService.addListener(this);
		gameService.init(null);
	}

	/**
	 * 设置配置信息
	 * @param restart 设置后是否重置棋子
	 */
	private void setConfiguration(final boolean restart) {
		ConfigurationWindow win = new ConfigurationWindow(this);
		win.setConfirmCallback(new Callback<Configuration>() {

			@Override
			public void invoke(Configuration value) {
				gameService.setConfiguration(value);
				if(restart) {
					gameService.init(ChessRole.RED);
				}
				gameService.randomMove();
			}
			
		});
		win.setConfiguration(gameService.getConfiguration());
		win.setVisible(true);
	}
	
	/**
	 * 更新象棋显示界面
	 */
	public void refreshDisplay() {
		Graphics graphics = displayCanvas.beginPaint();
		if (graphics != null) {
			DrawUtil.drawImage(graphics, BOARD_IMAGE, BACKGROUND_BOUNDS, null);
			drawState(graphics);
			drawPieces(graphics, gameService.getChessBoard().getPieces());
			ChessPiece selected = gameService.getSelected();
			if (selected != null) {
				drawSelected(graphics, selected.getLocation());
				drawTips(graphics, Color.green, selected.getValidTargets());
			}
			if (!gameService.getRecords().isEmpty()) {
				graphics.setColor(Color.red);
				ChessRecord record = gameService.getRecords().getLast();
				drawSelected(graphics, record.getOrigin(), record.getTarget());
			}
			drawAnimation(graphics);
		}
		displayCanvas.finishPaint();
	}

	/**
	 * 绘制棋子
	 * 
	 * @param graphics
	 * @param pieces
	 */
	private void drawPiece(Graphics graphics, ChessPiece piece, Point location) {
		graphics.drawImage(ApplicationUtil.getImage(piece), location.x + 1, location.y + 1, PIECE_SIZE.width - 2,
				PIECE_SIZE.height - 2, displayCanvas);
	}

	/**
	 * 绘制棋子
	 */
	private void drawPieces(Graphics graphics, Collection<ChessPiece> pieces) {
		if (graphics != null && pieces != null && !pieces.isEmpty()) {
			for (ChessPiece piece : pieces) {
				if (piece.getLocation() != null) {
					if (animations.containsKey(piece)) {
						continue;
					}
					drawPiece(graphics, piece,
							ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, piece.getLocation()));
				}
			}
		}
	}

	/**
	 * 绘制动画
	 * 
	 * @param graphics
	 */
	private void drawAnimation(Graphics graphics) {
		if (graphics != null) {
			Set<ChessPiece> keys = new HashSet<ChessPiece>();
			keys.addAll(animations.keySet());
			for (int i = 0; i < 3; ++i) {
				for (ChessPiece piece : keys) {
					Animation animation = animations.get(piece);
					if (animation != null) {
						if (animation.getZIndex() == i) {
							Point p = animation.next();
							if (p != null) {
								drawPiece(graphics, piece, p);
							}
							if (animation.isStoped()) {
								animations.remove(piece);
								animation.invokeCallback();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 绘制选择点
	 * 
	 * @param graphics
	 * @param points
	 */
	private void drawSelected(Graphics graphics, Point... points) {
		if (graphics != null && points != null && points.length > 0) {
			for (Point point : points) {
				Point p = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, point);
				graphics.drawImage(SELECTED_IMAGE, p.x, p.y, PIECE_SIZE.width, PIECE_SIZE.height, displayCanvas);
			}
		}
	}

	/**
	 * 绘制提示点
	 * 
	 * @param graphics
	 * @param color
	 * @param points
	 */
	private void drawTips(Graphics graphics, Color color, Collection<Point> points) {
		if (graphics != null && points != null && points.size() > 0) {
			Color savedColor = graphics.getColor();
			graphics.setColor(color != null ? color : Color.green);
			for (Point point : points) {
				Point p = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, point);
				graphics.drawOval(p.x, p.y, PIECE_SIZE.width, PIECE_SIZE.height);
			}
			graphics.setColor(savedColor);
		}
	}

	/**
	 * 绘制当前的状态
	 * 
	 * @param graphics
	 */
	private void drawState(Graphics graphics) {
		if (graphics == null) {
			return;
		}
		Color color = null;
		String text = null;
		if (gameService.getWinner() != null) {
			ChessRole winner = gameService.getWinner();
			if (winner.equals(ChessRole.RED)) {
				text = "红旗胜";
				color = STATE_RED;
			} else if (winner.equals(ChessRole.BLACK)) {
				text = "黑旗胜";
				color = STATE_BLACK;
			}
		} else if (gameService.isChecking()) {
			ChessRole checker = gameService.getCurrentRole();
			if (checker.equals(ChessRole.RED)) {
				text = "黑旗将军";
				color = STATE_BLACK;
			} else if (checker.equals(ChessRole.BLACK)) {
				text = "红旗将军";
				color = STATE_RED;
			}
		} else {
			ChessRole role = gameService.getCurrentRole();
			if (role.equals(ChessRole.BLACK)) {
				text = "黑方走棋";
				color = STATE_BLACK;
			} else if (role.equals(ChessRole.RED)) {
				text = "红方走棋";
				color = STATE_RED;
			}
		}

		if (text != null && color != null) {
			Point location = LayoutUtil.alignText(graphics, BACKGROUND_BOUNDS, text, STATUS_FONT, LayoutUtil.CENTER);
			DrawUtil.drawText(graphics, text, location, color, STATUS_FONT);
		}
	}

	@Override
	public void onRevoke(GameEvent e) {
		ChessRecord record = gameService.getRecords().getLast();
		Point origin = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, record.getTarget());
		Point target = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, record.getOrigin());
		Queue<Point> frames = AnimationUtil.moveLine(origin, target, 10);
		Animation.Callback callback = null;
		if (!RoomService.getInstance().exist()) {
			callback = new Animation.Callback() {
				@Override
				public void invoke() {
					if (gameService.getRoleCategory(gameService.getCurrentRole()).equals(ChessRoleCategory.COMPUTER)) {
						gameService.revoke(true);
					}
				}
			};
		}
		animations.put(record.getPiece(), new Animation(frames, 2, callback));
	}

	@Override
	public void onOver(GameEvent e) {
	}

	@Override
	public void onSelected(ChessPieceEvent e) {
	}

	@Override
	public void onDeselected(ChessPieceEvent e) {
	}

	@Override
	public void onDied(ChessPieceEvent e) {
		Point location = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, e.getLocation());
		Queue<Point> frames = AnimationUtil.pause(location, 10);
		animations.put(e.getPiece(), new Animation(frames, 1, null));
	}

	@Override
	public void onMoved(ChessPieceEvent e) {
		Point origin = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, e.getLocation());
		Point target = ApplicationUtil.getDrawCoord(BOARD_BOUNDS, PIECE_SIZE, e.getPiece().getLocation());
		Queue<Point> frames = AnimationUtil.moveLine(origin, target, 10);
		animations.put(e.getPiece(), new Animation(frames, 2, new Animation.Callback() {
			public void invoke() {
				if (RoomService.getInstance().exist()) {
					return;
				}
				gameService.notifyNext();
			}
		}));
	}

	@Override
	public void onPlay(GameEvent e) {
	}

	@Override
	public void onInitialized(GameEvent e) {
	}
}