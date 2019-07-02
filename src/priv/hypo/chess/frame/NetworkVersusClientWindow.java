package priv.hypo.chess.frame;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

import priv.hypo.chess.model.Room;
import priv.hypo.chess.service.RoomDetecor;
import priv.hypo.chess.service.RoomService;
/**
 * 网络对战客户端窗口
 * @author hypo
 */
public class NetworkVersusClientWindow extends NetworkVersusWindow implements RoomDetecor.Listener {

	private static final long serialVersionUID = 7824762814322739938L;
	private JButton btnJoin;
    private JList<Room> lstRooms;
    private DefaultListModel<Room> model;
    private RoomDetecor detecor;

	public NetworkVersusClientWindow(Window owner) {
		super(owner);
		initializeComponent();
	}
	
	/**
	 * 初始化控件
	 */
	public void initializeComponent() {
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				if (detecor != null) {
					detecor.close();
				}
			}
			
		});
		this.getContentPane().setLayout(new BorderLayout());
		
		model = new DefaultListModel<Room>();
		lstRooms = new JList<Room>(model);
        JScrollPane scrollPane = new JScrollPane(lstRooms);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        //
        // btnJoin
        // 
        btnJoin = new JButton("加入");
        btnJoin.setFont(font);
        btnJoin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				join();
			}
        	
        });
        this.getContentPane().add(btnJoin, BorderLayout.SOUTH);
        
		detecor = new RoomDetecor();
		detecor.setListener(this);
		detecor.init();
	}
	
	@Override
	public void onFind(Room room) {
		model.addElement(room);
	}
	
	/**
	 * 加入房间
	 */
	private void join() {
		Room room = lstRooms.getSelectedValue();
		if (room == null) {
			return;
		}
		RoomService.getInstance().join(room);
		setVisible(false);
	}
}