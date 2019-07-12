package priv.hypo.chess.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import priv.hypo.chess.common.AutomaticQueue;
import priv.hypo.chess.model.Message;
import priv.hypo.chess.util.StringUtil;

/**
 * 数据接收器
 * @author Hypo
 */
public class Receiver {
	/** 关闭指令 */
	private static final String CLOSE_COMMAND = "close_" + StringUtil.uuid();
	
	// 用来接收信息的套接字
	private DatagramSocket socket;
	
	// 通信端口号
	private int port;

	// 关闭状态
	private boolean closed = false;
	
	// 监听
	private Listener listener;
	
	// 数据接收线程
	private ReceiveThread receiveThread;

    // 数据接收队列
    private AutomaticQueue<Message> receiveQueue;
	
	// 数据发送队列
	private AutomaticQueue<Message> sendQueue;

	// Property accesses
	public void setListener(Listener l) {
		listener = l;
	}
	
	public DatagramSocket getSocket() {
		return socket;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		try {
			if (port == 0) {
				socket = new DatagramSocket();
			} else {
				socket = new DatagramSocket(port);
			}
			socket.setSoTimeout(3000);
            receiveQueue = new ReceiveQueue();
            receiveQueue.start();
            receiveThread = new ReceiveThread();
            receiveThread.start();
            sendQueue = new SendQueue();
            sendQueue.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送消息 
	 * @param content 消息内容
	 * @param address 接收方地址
	 * @param port 接收方端口号
	 */
	public void send(String content, InetAddress address, int port) {
		if (closed) {
			return;
		}
		Message message = new Message();
		message.setContent(content);
		message.setAddress(address);
		message.setPort(port);
		sendQueue.offer(message);
		
	}
	
	/**
	 * 发送消息
	 * @param message 消息内容
	 * @param host 接收方主机名
	 * @param port 接收方端口号
	 */
	public void send(String message, String host, int port) {
		try {
			send(message, InetAddress.getByName(host), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送消息
	 * @param data 消息内容
	 * @param address 接收方地址
	 * @param port 接收方端口
	 */
	public void send(Map<String, Object> data, InetAddress address, int port) {
		send(StringUtil.join(data), address, port);
	}
	
	/**
	 * 发送消息
	 * @param data 消息内容
	 * @param host 接收方主机名
	 * @param port 接收方端口号
	 */
	public void send(Map<String, Object> data, String host, int port) {
		send(StringUtil.join(data), host, port);
	}

	/**
	 * 关闭接收器，结束接收消息
	 */
	public void close() {
		send(CLOSE_COMMAND, "127.0.0.1", socket.getLocalPort());
		closed = true;
	}

	/**
	 * 返回true表示接收器已关闭，返回false表示接收器未关闭
	 * @return
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * 接收消息时的监听
	 */
	public interface Listener {
		/**
		 * 接收到信息时调用
		 * @param message
		 */
		void onReceived(Message message);
	}

    /**
     * 消息接收监听队列
     */
    private class ReceiveQueue extends AutomaticQueue<Message> {

        @Override
        protected boolean onPoll(Message message) throws Exception {
            if (message == null) {
                return false;
            }
            if (listener != null) {
                listener.onReceived(message);
            }
            return message.getContent().equals(CLOSE_COMMAND);
        }
    }

    /**
     * 数据接收线程
     */
    private class ReceiveThread extends Thread {

        @Override
        public void run() {
            byte[] buf = new byte[10240];
            while (!closed) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    Message message = new Message(buf);
                    message.setAddress(packet.getAddress());
                    message.setPort(packet.getPort());
                    receiveQueue.offer(message);
                    if (message.getContent().equals(CLOSE_COMMAND)) {
                        break;
                    }
                } catch (SocketTimeoutException e) {
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        }

    }

    /**
     * 数据发送队列
     */
    private class SendQueue extends AutomaticQueue<Message> {

        @Override
        protected void init() {
            interval = 50;
        }

        @Override
        protected boolean onPoll(Message message) throws Exception {
            byte[] bytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, message.getAddress(), message.getPort());
            socket.send(packet);
            return message.getContent().equals(CLOSE_COMMAND);
        }

    }
}