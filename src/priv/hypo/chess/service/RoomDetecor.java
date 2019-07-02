package priv.hypo.chess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.model.Message;
import priv.hypo.chess.model.Room;
import priv.hypo.chess.model.RoomStatus;
import priv.hypo.chess.util.StringUtil;

/**
 * 房间检测器
 * @author Hypo
 */
public class RoomDetecor implements Receiver.Listener {
	// 检测到的房间
	private Map<String, Room> rooms = new HashMap<String, Room>();
	
	// 数据接收器
	private Receiver receiver;
	
	// 监听接口
	private Listener listener;

	/**
	 * 设置监听接口
	 * @param listener
	 */
	public void setListener(Listener listener) {
		this.listener = listener;
	}
	
	/**
	 * 获取探测到的房间信息
	 * @return
	 */
	public List<Room> getRooms() {
		List<Room> list = new ArrayList<Room>(rooms.size());
		list.addAll(rooms.values());
		return list;
	}
	
	/**
	 * 初始化探测器
	 */
	public void init() {
		receiver = new Receiver();
		receiver.setPort(RoomBroadcast.ROOM_BROADCAST_PORT);
		receiver.setListener(this);
		receiver.init();
		receiver.open();
	}
	
	@Override
	public void onReceived(Message message) {
		Map<String, String> data = StringUtil.splitAsMap(message.getContent());
		if ("room_broadcast".equals(data.get("type"))) {
			String id = data.get("id");
			if (rooms.containsKey(id)) {
				return;
			}
			Room room = new Room();
			room.setId(id);
			room.setName(data.get("name"));
			room.setStatus(RoomStatus.valueOf(data.get("status")));
			room.setRole(ChessRole.valueOf(data.get("role")));
			room.setAddress(message.getAddress());
			room.setPort(message.getPort());
			rooms.put(id, room);
			if (listener != null) {
				listener.onFind(room);
			}
		}
	}

	
	/**
	 * 返回true表示探测器已关闭
	 * @return
	 */
	public boolean isClosed() {
		return receiver.isClosed();
	}
	
	/**
	 * 关闭探测器
	 */
	public void close() {
		receiver.close();
	}
	
	/**
	 * 探测器监听 
	 */
	public interface Listener {
		/**
		 * 找到房间时调用
		 * @param room
		 */
		void onFind(Room room);
	}
}