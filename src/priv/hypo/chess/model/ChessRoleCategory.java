package priv.hypo.chess.model;
/**
 * 角色类型
 * @author Hypo
 */
public enum ChessRoleCategory {
	/** 玩家 */
	PLAYER {
		@Override
		public String toString() {
			return "玩家";
		}
	},
	/** 电脑 */
	COMPUTER {
		@Override
		public String toString() {
			return "电脑";
		}
	},
}