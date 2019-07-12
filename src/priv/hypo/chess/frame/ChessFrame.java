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
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import priv.hypo.chess.common.Callback;
import priv.hypo.chess.common.Global;
import priv.hypo.chess.component.CanvasPanel;
import priv.hypo.chess.event.ChessPieceEvent;
import priv.hypo.chess.event.GameEvent;
import priv.hypo.chess.listener.ChessPieceListener;
import priv.hypo.chess.listener.GameListener;
import priv.hypo.chess.model.*;
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
    private static final long serialVersionUID = 1L;
    // Fields

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

    // 菜单列表
    private List<MenuItem> menus = new ArrayList<>();

    // 显示刷新定时器
    private Timer refreshDisplayTimer;

    static {
        gameService = GameService.getInstance();
        BOARD_IMAGE = ApplicationUtil.getImage("chessBoard");
        SELECTED_IMAGE = ApplicationUtil.getImage("selected");
        BOARD_BOUNDS = new Rectangle(20, 20, PIECE_SIZE.width * ChessBoard.WIDTH,
                PIECE_SIZE.height * ChessBoard.HEIGHT);
        BACKGROUND_BOUNDS = new Rectangle(0, 0, BOARD_BOUNDS.width + 40, BOARD_BOUNDS.height + 40);
    }

    // Constructors

    /**
     * default constructor
     */
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
        this.setTitle(Global.APP_NAME);
        this.setSize(526, 635);
        this.setLocation(LayoutUtil.align(LayoutUtil.CENTER, ApplicationUtil.toolkit.getScreenSize(), this.getSize()));
        this.setResizable(false);
        this.setFont(Global.DEFAULT_FONT);
        this.setType(Type.NORMAL);
//		this.setUndecorated(true);
        this.getContentPane().setLayout(new BorderLayout());

        initMenus();
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
     * 初始化菜单项
     */
    private void initMenus() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("开始", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RoomService.getInstance().exist()) {
                    gameService.init(RoomService.getInstance().getRole());
                } else {
                    setConfiguration(true);
                }
            }
        }));
        options.add(new MenuItem("设置", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setConfiguration(false);
            }
        }));
        options.add(new MenuItem("悔棋", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameService.revoke(true);
            }
        }));
        menus.add(new MenuItem("选项", options));
        options = new ArrayList<MenuItem>();
        options.add(new MenuItem("创建对局", new ActionListener() {
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
        }));
        options.add(new MenuItem("查找对局", new ActionListener() {
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
        }));
        options.add(new MenuItem("离开", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoomService.getInstance().leave();
            }
        }));
        menus.add(new MenuItem("多人游戏", options));
        JMenuBar menuBar = new JMenuBar();
        for (MenuItem item : menus) {
            JMenu menu = new JMenu(item.getText());
            menu.setFont(Global.DEFAULT_FONT);
            menuBar.add(menu);
            for (MenuItem sub : item.getSubMenus()) {
                JMenuItem mi = new JMenuItem(sub.getText());
                mi.addActionListener(sub.getAction());
                mi.setFont(Global.DEFAULT_FONT);
                menu.add(mi);
            }
        }
        this.getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    /**
     * 初始化逻辑
     */
    private void initService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                animations = new HashMap<ChessPiece, Animation>();
                gameService.addListener(ChessFrame.this);
                gameService.init(null);
            }
        }).start();
    }

    /**
     * 设置配置信息
     *
     * @param restart 设置后是否重置棋子
     */
    private void setConfiguration(final boolean restart) {
        ConfigurationWindow win = new ConfigurationWindow(this);
        win.setConfirmCallback(new Callback<Configuration>() {

            @Override
            public void invoke(Configuration value) {
                gameService.setConfiguration(value);
                if (restart) {
                    gameService.init(ChessRole.RED);
                }
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
        ref:
        if (graphics != null) {
            DrawUtil.drawImage(graphics, BOARD_IMAGE, BACKGROUND_BOUNDS, null);
            if (gameService == null || gameService.getChessBoard() == null) {
                break ref;
            }
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
     */
    private void drawPiece(Graphics graphics, ChessPiece piece, Point location) {
        if (graphics == null || piece == null || location == null) {
            return;
        }
        graphics.drawImage(ApplicationUtil.getImage(piece), location.x + 1, location.y + 1, PIECE_SIZE.width - 2,
                PIECE_SIZE.height - 2, displayCanvas);
    }

    /**
     * 绘制棋子
     */
    private void drawPieces(Graphics graphics, Collection<ChessPiece> pieces) {
        if (graphics == null || pieces == null || pieces.isEmpty()) {
            return;
        }
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
        if (gameService.getChessBoard() == null) {
            text = "未开始";
            color = Global.STATE_RED_COLOR;
        } else if (gameService.getWinner() != null) {
            switch (gameService.getWinner()) {
                case RED:
                    text = "红旗胜";
                    color = Global.STATE_RED_COLOR;
                    break;
                case BLACK:
                    text = "黑旗胜";
                    color = Global.STATE_BLACK_COLOR;
                    break;
            }
        } else if (gameService.kingHasThreaten()) {
            switch(gameService.getCurrentRole()) {
                case RED:
                    text = "黑旗将军";
                    color = Global.STATE_BLACK_COLOR;
                    break;
                case BLACK:
                    text = "红旗将军";
                    color = Global.STATE_RED_COLOR;
                    break;
            }
        } else {
            switch (gameService.getCurrentRole()) {
                case RED:
                    text = "红方走棋";
                    color = Global.STATE_RED_COLOR;
                    break;
                case BLACK:
                    text = "黑方走棋";
                    color = Global.STATE_BLACK_COLOR;
                    break;
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
            @Override
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