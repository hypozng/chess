package priv.hypo.chess.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import priv.hypo.chess.model.Message;
import priv.hypo.chess.util.StringUtil;

/**
 * 数据接收器
 * @author Hypo
 */
public class Receiver {
	/** 关闭指令 */
	private static final String CLOSE_COMMOND = "close_" + StringUtil.uuid();
	
	// 用来接收信息的套接字
	private DatagramSocket socket;
	
	// 通信端口号
	private int port;

	// 接收到的消息队列
	private Queue<Message> receiveMessages = new LinkedList<Message>();
	
	// 消息发送队列
	private Queue<Message> sendMessages = new LinkedList<Message>();

//	 接收数据时使用的缓存
//	private byte[] rbuffer = new byte[10240];
	
	// 关闭状态
	private boolean closed = false;
	
	// 监听
	private Listener listener;
	
	// 数据接收线程
	private Thread receiveThread;
	
	// 数据发送线程
	private Thread sendThread;

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
	 * @param port
	 */
	public void init() {
		try {
			if (port == 0) {
				socket = new DatagramSocket();
			} else {
				socket = new DatagramSocket(port);
			}
			socket.setSoTimeout(3000);
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (!closed && receiveMessages.isEmpty()) {
						try {
							if (receiveMessages.isEmpty()) {
								synchronized (receiveMessages) {
									receiveMessages.wait();
								}
								if (receiveMessages.isEmpty()) {
									continue;
								}
							}
							Message message = receiveMessages.poll();
							if (message.getContent().equals(CLOSE_COMMOND)) {
								break;
							}
							if (listener != null) {
								listener.onReceived(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			}).start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取长度数据
	 * @param num
	 * @return
	 */
	private byte[] toBytes(int num) {
		byte[] data = new byte[4];
		data[0] = (byte) (num >> 24);
		data[1] = (byte) (num >> 16);
		data[2] = (byte) (num >> 8);
		data[3] = (byte) num;
		return data;
	}
	
	/**
	 * 转换为int
	 * @param data
	 * @return
	 */
	private int toInt(byte[] data) {
		if (data == null || data.length != 4) {
			return 0;
		}
		return data[3] | data[2] << 8 | data[1] << 16 | data[0] << 24;
	}

	/**
	 * 打开接收器，开始接收消息
	 */
	public void open() {
		receiveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] buf = new byte[10240];
				while (!closed) {
					try {
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						socket.receive(packet);
						byte[] data = new byte[4];
						System.arraycopy(buf, 0, data, 0, 4);
						int length = toInt(data);
						String text = new String(buf, 4, length);
						Message message = new Message();
						message.setAddress(packet.getAddress());
						message.setPort(packet.getPort());
						message.setContent(text);
						synchronized (receiveMessages) {
							receiveMessages.offer(message);
							receiveMessages.notifyAll();
						}
						if (text.equals(CLOSE_COMMOND)) {
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

		});
		receiveThread.start();
		sendThread = new Thread(new Runnable() {

			@Override
			public void run() {
				byte[] buf = new byte[10240];
				while(!closed) {
					try {
						if (sendMessages.isEmpty()) {
							synchronized(sendMessages) {
								sendMessages.wait();
							}
							if (sendMessages.isEmpty()) {
								continue;
							}
						}
						Message message = sendMessages.poll();
						byte[] data = message.getContent().getBytes();
						System.arraycopy(toBytes(data.length), 0, buf, 0, 4);
						System.arraycopy(data, 0, buf, 4, data.length);
						DatagramPacket packet = new DatagramPacket(buf, data.length + 4, message.getAddress(), message.getPort());
						socket.send(packet);
						if (message.getContent().equals(CLOSE_COMMOND)) {
							break;
						}
						Thread.sleep(100);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		sendThread.start();
	}

	/**
	 * 发送消息 
	 * @param message 消息内容
	 * @param address 接收方地址
	 * @param port 接收方端口号
	 */
	public void send(String message, InetAddress address, int port) {
		if (closed) {
			return;
		}
		Message msg = new Message();
		msg.setContent(message);
		msg.setAddress(address);
		msg.setPort(port);
		synchronized (sendMessages) {
			sendMessages.offer(msg);
			sendMessages.notifyAll();
		}
		
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
		send(CLOSE_COMMOND, "127.0.0.1", socket.getLocalPort());
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
}