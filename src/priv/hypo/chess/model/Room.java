package priv.hypo.chess.model;

import java.net.InetAddress;

/**
 * 房间信息
 * 
 * @author Hypo
 */
public class Room implements java.io.Serializable {
	private static final long serialVersionUID = -554010814813135595L;

	// Fields
	// 房间ID 用来标识一个房间
	private String id;
	
	// 房间名
	private String name;
	
	// 房间状态
	private RoomStatus status;
	
	// 房主角色
	private ChessRole role;
	
	// 房间地址信息
	private InetAddress address;
	
	// 房间端口号
	private int port;

	// Property accessors
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	
	public ChessRole getRole() {
		return role;
	}
	
	public void setRole(ChessRole role) {
		this.role = role;
	}

	public InetAddress getAddress() {
		return address;
	}
	
	public void setAddress(InetAddress address) {
		this.address = address;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	// Methods
	@Override
	public String toString() {
		return name;
	}
	
}