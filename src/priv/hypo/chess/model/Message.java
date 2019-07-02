package priv.hypo.chess.model;

import java.net.InetAddress;

/**
 * 传输的信息
 * @author Hypo
 *
 */
public class Message {
	/**
	 * 消息发送方
	 */
	private InetAddress address;
	
	/**
	 * 消息发送方的端口
	 */
	private int port;
	
	/**
	 * 消息内容
	 */
	private String content;

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress sender) {
		this.address = sender;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}