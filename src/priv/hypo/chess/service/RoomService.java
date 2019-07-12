package priv.hypo.chess.service;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.hypo.chess.listener.RoomListener;
import priv.hypo.chess.model.*;
import priv.hypo.chess.util.StringUtil;

/**
 * 多玩家服务
 * 
 * @author Hypo
 */
public class RoomService implements Receiver.Listener {
	
	public static RoomService instance;
	
	// 房间数据
	private Room room;
	
	// 在房间中的角色
	private ChessRole role;
	
	// 对手
	private Player player;
	
	// 房间广播
	private RoomBroadcast broadcast;
	
	// 数据接收器
	private Receiver receiver;
	
	// 房间相关状态监听
	private List<RoomListener> listeners = new ArrayList<RoomListener>();
	
	// Constructors
	private RoomService() {
	}
	
	public static RoomService getInstance() {
		if (instance == null) {
			synchronized (RoomService.class) {
				if (instance == null) {
					instance = new RoomService();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取在房间中的角色
	 * @return
	 */
	public ChessRole getRole() {
		return role;
	}
	
	/**
	 * 获取当前创建/加入的房间
	 * @return
	 */
	public Room getRoom() {
		return room;
	}
	
	/**
	 * 添加监听程序
	 * @param l
	 */
	public void addListener(RoomListener l) {
		listeners.add(l);
	}

	// Methods
	/**
	 * 创建房间
	 * @param room 房间信息
	 */
	public void create(Room room) {
		if (this.room != null) {
			leave();
		}
		room.setId(StringUtil.uuid());
		room.setStatus(RoomStatus.WATING);
		this.room = room;
		role = room.getRole();
		receiver = new Receiver();
		receiver.setListener(this);
		receiver.init();
		broadcast = new RoomBroadcast(room, receiver);
		broadcast.start();
	}
	
	/**
	 * 加入房间
	 * @param room 房间信息
	 */
	public void join(Room room) {
		if (this.room != null) {
			leave();
		}
		this.room = room;
		role = room.getRole().getEnemy();
		receiver = new Receiver();
		receiver.setListener(this);
		receiver.init();

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", "room_join");
		data.put("room_id", room.getId());
		player = new Player();
		player.setAddress(room.getAddress());
		player.setPort(room.getPort());
		receiver.send(StringUtil.join(data), room.getAddress(), room.getPort());
		
		GameService.getInstance().init(room.getRole().getEnemy());
	}
	
	/**
	 * 停止房间服务
	 */
	public void leave() {
		if (broadcast != null) {
			broadcast.close();
		}
		if (receiver != null) {
			receiver.close();
		}
		this.room = null;
		this.role = null;
	}
	
	/**
	 * 是否存在可用的房间
	 * @return
	 */
	public boolean exist() {
		return room != null;
	}
	
	/**
	 * 移动旗子
	 */
	public void movePiece(ChessStep step) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", "piece_move");
		data.put("originx", 8 - step.getOrigin().x);
		data.put("originy", 9 - step.getOrigin().y);
		data.put("targetx", 8 - step.getTarget().x);
		data.put("targety", 9 - step.getTarget().y);
		receiver.send(StringUtil.join(data), player.getAddress(), player.getPort());
	}

	/**
	 * 悔棋
	 */
	public void revoke() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", "piece_revoke");
		receiver.send(data, player.getAddress(), player.getPort());
	}
	
	@Override
	public void onReceived(Message message) {
		Map<String, String> data = StringUtil.splitAsMap(message.getContent());
		String type = data.get("type");
		switch(type) {
		case "room_join":
			if (!room.getId().equals(data.get("room_id"))) {
				break;
			}
			room.setStatus(RoomStatus.PLAYING);
			player = new Player();
			player.setAddress(message.getAddress());
			player.setPort(message.getPort());
			postOnJoin();
			break;
		case "piece_move":
			Point origin = new Point(Integer.parseInt(data.get("originx")), Integer.parseInt(data.get("originy")));
			Point target = new Point(Integer.parseInt(data.get("targetx")), Integer.parseInt(data.get("targety")));
			postOnMove(origin, target);
			break;
		case "piece_revoke":
			postOnRevoke();
			break;
		}
	}
	
	// post event methods
	private void postOnJoin() {
		for (RoomListener l : listeners) {
			l.onJoin();
		}
	}
	
	private void postOnMove(Point origin, Point target) {
		for (RoomListener l : listeners) {
			l.onMove(origin, target);
		}
	}
	
	private void postOnRevoke() {
		for (RoomListener l : listeners) {
			l.onRevoke();
		}
	}
}