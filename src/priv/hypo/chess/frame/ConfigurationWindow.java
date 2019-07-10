package priv.hypo.chess.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import priv.hypo.chess.common.Callback;
import priv.hypo.chess.common.Global;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.ChessRoleCategory;
import priv.hypo.chess.model.Configuration;
import priv.hypo.chess.util.ApplicationUtil;
import priv.hypo.chess.util.LayoutUtil;

/**
 * 设置窗口
 * 
 * @author Hypo
 */
public class ConfigurationWindow extends PopupWindow {
	private static final long serialVersionUID = -3318654307159405403L;
	private static final Font DEFAULT_FONT = new Font(Global.DEFAULT_FONT_NAME, Font.PLAIN, 16);
	private final Dimension buttonSize = new Dimension(70, 24);

	private Window owner;

	private JLabel lblRedRole;
	private JComboBox<ChessRoleCategory> cmbRedRole;
	private JLabel lblBlackRole;
	private JComboBox<ChessRoleCategory> cmbBlackRole;
	private JButton btnOK;
	private JButton btnCancel;
	private Callback<Configuration> confirmCallback;

	public ConfigurationWindow(Window owner) {
		super(owner);
		this.owner = owner;
		initialize();
	}
	
	private void initialize() {
		//
		// owner
		//
		owner.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				adjustLocation();
			}
		});
		//
		// ChessSetWindow
		//
		this.setSize(200, 200);
		adjustLocation();
//		this.setBackground(new Color(223, 191, 134));
		this.setTitle("对局设置");
		this.setIconImage(ApplicationUtil.getImage("logo"));
		this.getContentPane().setLayout(null);

		//
		// lblRedRole
		//
		lblRedRole = new JLabel("红方");
		lblRedRole.setFont(DEFAULT_FONT);
		lblRedRole.setBounds(30, 30, 50, 20);
		this.getContentPane().add(lblRedRole);
		//
		// cmbRedRole
		//
		cmbRedRole = new JComboBox<ChessRoleCategory>();
		cmbRedRole.setFont(DEFAULT_FONT);
		cmbRedRole.setBounds(80, 30, 100, 20);
		cmbRedRole.addItem(ChessRoleCategory.PLAYER);
		cmbRedRole.addItem(ChessRoleCategory.COMPUTER);
		this.getContentPane().add(cmbRedRole);
		//
		// lblBlackRole
		//
		lblBlackRole = new JLabel("黑方");
		lblBlackRole.setFont(DEFAULT_FONT);
		lblBlackRole.setBounds(30, 70, 50, 20);
		this.getContentPane().add(lblBlackRole);
		//
		// cmbBlackRole
		//
		cmbBlackRole = new JComboBox<ChessRoleCategory>();
		cmbBlackRole.setFont(DEFAULT_FONT);
		cmbBlackRole.setBounds(80, 70, 100, 20);
		cmbBlackRole.addItem(ChessRoleCategory.PLAYER);
		cmbBlackRole.addItem(ChessRoleCategory.COMPUTER);
		this.getContentPane().add(cmbBlackRole);
		//
		// btnOK
		//
		btnOK = new JButton("确定");
		btnOK.setFont(DEFAULT_FONT);
		btnOK.setSize(buttonSize);
		btnOK.setLocation(20, this.getHeight() - TITLE_HEIGHT - buttonSize.height - 10);
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Configuration configuration = new Configuration();
				configuration.setCategory(ChessRole.RED, (ChessRoleCategory) cmbRedRole.getSelectedItem());
				configuration.setCategory(ChessRole.BLACK, (ChessRoleCategory) cmbBlackRole.getSelectedItem());
				if(confirmCallback != null) {
					confirmCallback.invoke(configuration);
				}
				ConfigurationWindow.this.dispose();
			}
		});
		this.getContentPane().add(btnOK);
		//
		// btnCancel
		//
		btnCancel = new JButton("取消");
		btnCancel.setFont(DEFAULT_FONT);
		btnCancel.setSize(buttonSize);
		btnCancel.setLocation(110, this.getHeight() - TITLE_HEIGHT - buttonSize.height - 10);
		btnCancel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				ConfigurationWindow.this.dispose();
			}
		});
		this.getContentPane().add(btnCancel);
	}
 
	/**
	 * 设置配置内容
	 * @param configuration
	 */
	public void setConfiguration(Configuration configuration) {
		if(configuration == null) {
			return;
		}
		cmbRedRole.setSelectedItem(configuration.getCategory(ChessRole.RED));
		cmbBlackRole.setSelectedItem(configuration.getCategory(ChessRole.BLACK));
	}
	
	/**
	 * 设置回调
	 * @param callback
	 */
	public void setConfirmCallback(Callback<Configuration> callback) {
		this.confirmCallback = callback;
	}

	private void adjustLocation() {
		this.setLocation(LayoutUtil.align(LayoutUtil.CENTER, this.getOwner().getBounds(), this.getBounds()));
	}
}