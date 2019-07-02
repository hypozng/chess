package priv.hypo.chess.model;

/**
 * 房间状态
 * @author Hypo
 *
 */
public enum RoomStatus {
	WATING("等待中"),
	PLAYING("游戏中");
	
	private String name;
	
	private RoomStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}