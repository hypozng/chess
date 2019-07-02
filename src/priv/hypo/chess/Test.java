package priv.hypo.chess;

import priv.hypo.chess.util.StringUtil;

public class Test {
	public static void main(String[] args) throws Exception {
//		final Receiver receiver = new Receiver();
//		receiver.setListener(new Receiver.Listener() {
//
//			@Override
//			public void onReceived(Message message) {
//				System.out.println(message.getContent());
//			}
//			
//		});
////		receiver.setPort(1136);
//		receiver.init();
//		receiver.open();
//		Room room = new Room();
//		room.setId(UUID.randomUUID().toString().replaceAll("[\\-]", ""));
//		room.setName(System.getenv("COMPUTERNAME"));
//		RoomBroadcast broadcast = new RoomBroadcast(room, receiver);
//		broadcast.start();
		System.out.println(StringUtil.uuid());
		
		
//		receiver.send("hello", "255.255.255.255", 1136);
//		Thread.sleep(100);
//		receiver.close();
	}
}