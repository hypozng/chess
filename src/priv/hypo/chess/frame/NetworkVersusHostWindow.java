package priv.hypo.chess.frame;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import priv.hypo.chess.listener.RoomListener;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.Room;
import priv.hypo.chess.service.RoomService;
import priv.hypo.chess.util.LayoutUtil;

/**
 * 多人游戏服务器窗口
 * 
 * @author Hypo
 */
public class NetworkVersusHostWindow extends NetworkVersusWindow {
	private static final long serialVersionUID = -4546619048794570584L;

	// Fields
	private JLabel lblRoomName;
	private JTextField txtRoomName;
	private JLabel lblRole;
	private JComboBox<ChessRole> cmbRole;
	private JButton btnStart;
	private JLabel lblMessage;
	private NetworkVersusHostWindow instance = this;

	public NetworkVersusHostWindow(Window owner) {
		super(owner);
		initializeComponent();
	}

	/**
	 * 初始化页面布局
	 */
	private void initializeComponent() {
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				lblMessage.setLocation(LayoutUtil.align(LayoutUtil.LEFT | LayoutUtil.BOTTOM,
						instance.getContentPane().getSize(), lblMessage.getSize()));
			}
		});
		//
		// lblRoomName
		//
		lblRoomName = new JLabel("房间名");
		lblRoomName.setBounds(10, 10, 50, 24);
		lblRoomName.setFont(DEFAULT_FONT);
		this.getContentPane().add(lblRoomName);
		//
		// txtRoomName
		//
		txtRoomName = new JTextField();
		txtRoomName.setText(System.getenv("COMPUTERNAME"));
		txtRoomName.setBounds(70, 10, 200, 24);
		txtRoomName.setFont(DEFAULT_FONT);
		this.getContentPane().add(txtRoomName);
		//
		// lblRole
		//
		lblRole = new JLabel("角色");
		lblRole.setBounds(10, 70, 50, 24);
		lblRole.setFont(DEFAULT_FONT);
		lblRole.setHorizontalAlignment(SwingConstants.RIGHT);
		this.getContentPane().add(lblRole);
		//
		// cmbRole
		//
		cmbRole = new JComboBox<ChessRole>();
		cmbRole.setBounds(70, 70, 200, 24);
		cmbRole.setFont(DEFAULT_FONT);
		cmbRole.addItem(ChessRole.RED);
		cmbRole.addItem(ChessRole.BLACK);
		this.getContentPane().add(cmbRole);
		//
		// btnStart
		//
		btnStart = new JButton("创建对局");
		btnStart.setSize(100, 24);
		btnStart.setLocation((this.getWidth() - btnStart.getSize().width) / 2, 100);
		btnStart.setFont(DEFAULT_FONT);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createRoom();
			}
		});
		this.getContentPane().add(btnStart);
//		FieldPanel<JTextField> fp = new FieldPanel<JTextField>("用户名", new JTextField());
//		fp.setBounds(0, 130, getWidth(), 24);
//		this.getContentPane().add(fp);
//		JTextField txtUser = new JTextField();
		
		//
		// lblMessage
		//
		lblMessage = new JLabel();
		lblMessage.setSize(this.getWidth(), 24);
		lblMessage.setFont(DEFAULT_FONT);
		this.getContentPane().add(lblMessage);
	}
	
	/**
	 * 创建房间
	 */
	private void createRoom() {
		Room room = new Room();
		room.setName(txtRoomName.getText().trim());
		room.setRole((ChessRole) cmbRole.getSelectedItem());
		RoomService.getInstance().create(room);
		RoomService.getInstance().addListener(new RoomListener() {

			@Override
			public void onJoin() {
				instance.setVisible(false);
			}

			@Override
			public void onMove(Point origin, Point target) {
			}

			@Override
			public void onRevoke() {
			}
			
		});

		txtRoomName.setEnabled(false);
		cmbRole.setEnabled(false);
		btnStart.setEnabled(false);
		lblMessage.setText("游戏已创建，等待玩家加入...");
		gameService.init(room.getRole());
	}
}