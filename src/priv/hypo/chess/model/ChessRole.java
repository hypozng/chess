package priv.hypo.chess.model;
/**
 * 游戏角色
 * @author Hypo
 */
public enum ChessRole {
	/** 红方 */
	RED("红方"),
	
	/** 黑方 */
	BLACK("黑方");

	private String text;
	
	private ChessRole(String text) {
		this.text = text;
	}
	
	/**
	 * 获取敌方角色
	 * @return 
	 */
	public ChessRole getEnemy() {
		switch(this) {
		case RED:
			return BLACK;
		case BLACK:
			return RED;
		}
		return null;
	}

	@Override
	public String toString() {
		return text;
	}
}