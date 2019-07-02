package priv.hypo.chess.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RoomThread extends Thread{
	public static final InetAddress PUBLISH_ADDRESS;
	static {
		try {
			PUBLISH_ADDRESS = InetAddress.getByName("255.255.255.255");
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ExceptionInInitializerError(e);
		}
	}
	private Room room;
	private boolean stoped;
	
	public RoomThread(Room room) {
		this.room = room;
	}
	
	public void run() {
		try {
			DatagramSocket socket = new DatagramSocket();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ObjectOutputStream objectOutput = new ObjectOutputStream(output);
			while(!stoped) {
				objectOutput.writeObject(room);
				objectOutput.flush();
				byte[] cache = output.toByteArray();
				DatagramPacket packet = new DatagramPacket(cache, cache.length, PUBLISH_ADDRESS, room.getPort());
				socket.send(packet);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			socket.close();
			objectOutput.close();
			output.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void stopPublish() {
		this.stoped = true;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		if(room != null) {
			this.room = room;
		}
	}
}