package priv.hypo.chess.model;
/**
 * 棋子种类
 * @author Hypo
 */
public enum ChessPieceType {
	/** 車 */
	ROOK("車"),
	/** 马 */
	KNIGHT("马"),
	/** 象 */
	BISHOP("象"),
	/** 仕 */
	GUARD("仕"),
	/** 将 */
	KING("将"),
	/** 炮 */
	CANNON("炮"),
	/** 兵 */
	PAWN("兵");

    private String text;

    private ChessPieceType(String text){
        this.text = text;
    }
}