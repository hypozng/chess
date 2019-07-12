package priv.hypo.chess.service;

import java.util.HashMap;
import java.util.Map;

import priv.hypo.chess.common.Global;
import priv.hypo.chess.model.Room;

/**
 * 房间广播
 * @author Hypo
 */
public class RoomBroadcast extends Thread {
	// 房间查找通信端口号
	public static final int ROOM_BROADCAST_PORT = 1136;
	
	// 房间信息
	private Room room;
	
	// 广播关闭标志  为true时表示广播已关闭
	private boolean closed;
	
	// 数据接收器
	private Receiver receiver;
	
	public RoomBroadcast(Room room, Receiver receiver) {
		this.room = room;
		this.receiver = receiver;
	}
	
	// Property accesses
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public Receiver getReceiver() {
		return receiver;
	}
	
	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	// Methods
	@Override
	public void run() {
		if (receiver == null) {
			return;
		}
		while (!closed) {
			try {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("type", "room_broadcast");
				data.put("id", room.getId());
				data.put("name", room.getName());
				data.put("status", room.getStatus().name());
				data.put("role", room.getRole().name());
				receiver.send(data, Global.BROADCAST_HOST, Global.BROADCAST_PORT);
				Thread.sleep(Global.BROADCAST_INTERVAL);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		closed = true;
	}
}