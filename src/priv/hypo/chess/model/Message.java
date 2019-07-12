package priv.hypo.chess.model;

import priv.hypo.chess.util.ConvertUtil;

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

    public Message() {

    }

    public Message(byte[] src) {
        parse(src);
    }

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
	
	public void parse(byte[] src) {
        if (src == null || src.length < 4) {
            return;
        }
        int length = ConvertUtil.toInt(src);
        content = new String(src, 4, length);
    }

    /**
     * 获取byte数组，用于传输数据
     * @return
     */
    public byte[] getBytes() {
        byte[] contentBytes = new byte[0];
        if (content != null) {
            contentBytes = content.getBytes();
        }
        byte[] lengthBytes = ConvertUtil.toBytes(contentBytes.length);
        return ConvertUtil.joinBytes(lengthBytes, contentBytes);
    }
}