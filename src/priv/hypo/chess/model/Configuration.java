package priv.hypo.chess.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置
 * @author hypo
 */
public class Configuration {
	private Map<ChessRole, ChessRoleCategory> categories;
	
//	private ChessRoleCategory redCategory = ChessRoleCategory.PLAYER;
//	private ChessRoleCategory blackCategory = ChessRoleCategory.COMPUTER;
	
	public Configuration() {
		init();
	}
	
//	public ChessRoleCategory getRedCategory() {
//		return redCategory;
//	}
//	
//	public void setRedCategory(ChessRoleCategory redCategory) {
//		this.redCategory = redCategory;
//	}
//	
//	public ChessRoleCategory getBlackCategory() {
//		return blackCategory;
//	}
//	
//	public void setBlackCategory(ChessRoleCategory blackCategory) {
//		this.blackCategory = blackCategory;
//	}
	
	private void init() {
		categories = new HashMap<ChessRole, ChessRoleCategory>();
		categories.put(ChessRole.RED, ChessRoleCategory.PLAYER);
		categories.put(ChessRole.BLACK, ChessRoleCategory.COMPUTER);
	}
	
	public ChessRoleCategory getCategory(ChessRole role) {
		if (role == null) {
			return null;
		}
		return categories.get(role);
	}
	
	public void setCategory(ChessRole role, ChessRoleCategory category) {
		if (role == null || category == null) {
			return;
		}
		categories.put(role, category);
	}
	
	@Override
	public String toString() {
		return String.format("[redCategory=%s, blackCategory=%s]",
				categories.get(ChessRole.RED), categories.get(ChessRole.BLACK));
	}
}