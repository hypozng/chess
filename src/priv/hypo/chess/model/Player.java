package priv.hypo.chess.model;

import java.net.InetAddress;

/**
 * 用户信息
 * @author Hypo
 */
public class Player {
	// 用户ID
	private String id;
	
	// 用户名
	private String name;
	
	// 用户地址信息
	private InetAddress address;
	
	// 用户通信端口号
	private int port;

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
	
}