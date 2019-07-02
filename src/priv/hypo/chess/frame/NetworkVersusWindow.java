package priv.hypo.chess.frame;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import priv.hypo.chess.common.Global;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.ComboBoxItem;
import priv.hypo.chess.service.GameService;
import priv.hypo.chess.service.RoomService;
import priv.hypo.chess.util.ApplicationUtil;
import priv.hypo.chess.util.LayoutUtil;
import priv.hypo.chess.util.ShapeUtil;

/**
 * 多人游戏窗口
 * 
 * @author Hypo
 */
public class NetworkVersusWindow extends PopupWindow {
	private static final long serialVersionUID = 4091712243357742854L;

	public static final ComboBoxItem<ChessRole> RED_ITEM = new ComboBoxItem<ChessRole>("红棋", ChessRole.RED);
	public static final ComboBoxItem<ChessRole> BLACK_ITEM = new ComboBoxItem<ChessRole>("黑棋", ChessRole.BLACK);
	protected static final Font DEFAULT_FONT = new Font("Microsoft Yahei UI", Font.PLAIN, 16);
	private NetworkVersusWindow instance = this;
	protected Window owner;
	protected GameService gameService = GameService.getInstance();

	public NetworkVersusWindow(Window owner) {
		super(owner);
		this.owner = owner;
		this.initializeComponent();
	}

	// Methods
	private void initializeComponent() {
		//
		// NetworkWindow
		//
		this.setSize(300, 400);
		this.setTitle("多人游戏");
		this.setFont(Global.DEFAULT_FONT);
		this.setIconImage(ApplicationUtil.getImage("logo"));
		this.setLocation(ShapeUtil.add(LayoutUtil.align(LayoutUtil.CENTER, owner.getSize(), this.getSize()),
				owner.getLocation()));
		this.getContentPane().setLayout(null);
		owner.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				instance.setLocation(ShapeUtil.add(
						LayoutUtil.align(LayoutUtil.CENTER, owner.getSize(), instance.getSize()), owner.getLocation()));
			}
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				RoomService.getInstance().leave();
			}
		});
	}

}