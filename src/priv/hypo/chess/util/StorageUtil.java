package priv.hypo.chess.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessPieceBishop;
import priv.hypo.chess.model.ChessPieceCannon;
import priv.hypo.chess.model.ChessPieceGuard;
import priv.hypo.chess.model.ChessPieceKing;
import priv.hypo.chess.model.ChessPieceKnight;
import priv.hypo.chess.model.ChessPiecePawn;
import priv.hypo.chess.model.ChessPieceRook;
import priv.hypo.chess.model.ChessPieceType;
import priv.hypo.chess.model.ChessRole;
import priv.hypo.chess.util.StringUtil;

/**
 * 存储工具
 * @author hypo
 */
public class StorageUtil {

	/**
	 * 获取存储指定棋子的字符串
	 * @param piece 参照的棋子
	 * @return 用于存储的字符串
	 */
	public static String getStoreText(ChessPiece piece) {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put(ChessPiece.SF_ROLE, piece.getRole().name());
		fields.put(ChessPiece.SF_TYPE, piece.getType().name());
		fields.put(ChessPiece.SF_LOCATION_X, Integer.toString(piece.getLocation().x));
		fields.put(ChessPiece.SF_LOCATION_Y, Integer.toString(piece.getLocation().y));
		return StringUtil.join(fields);
	}

	/**
	 * 从包含棋子信息的字符串中解析棋子信息
	 * @param source 包含棋子信息的字符串
	 * @return 解析出的棋子
	 */
	public static ChessPiece parsePiece(String source) {
		if (source == null || source.isEmpty()) {
			return null;
		}

		Map<String, String> map = StringUtil.splitAsMap(source);
		ChessRole role = ChessRole.valueOf(map.get(ChessPiece.SF_ROLE));
		ChessPieceType type = ChessPieceType.valueOf(map.get(ChessPiece.SF_TYPE));
		int x = Integer.parseInt(map.get(ChessPiece.SF_LOCATION_X));
		int y = Integer.parseInt(map.get(ChessPiece.SF_LOCATION_Y));

		ChessPiece piece = null;
		switch (type) {
		case BISHOP:
			piece = new ChessPieceBishop(role);
			break;
		case CANNON:
			piece = new ChessPieceCannon(role);
			break;
		case GUARD:
			piece = new ChessPieceGuard(role);
			break;
		case KING:
			piece = new ChessPieceKing(role);
			break;
		case KNIGHT:
			piece = new ChessPieceKnight(role);
			break;
		case PAWN:
			piece = new ChessPiecePawn(role);
			break;
		case ROOK:
			piece = new ChessPieceRook(role);
			break;
		default:
			break;
		}
		
		if (piece != null) {
			piece.setLocation(x, y);
		}
		return piece;
	}
	
	/**
	 * 从输入流加载棋子信息
	 * @param in
	 * @return
	 */
	public static List<ChessPiece> loadPieces(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		List<ChessPiece> pieces = new ArrayList<ChessPiece>();
		try {
			while((line = reader.readLine()) != null) {
				pieces.add(parsePiece(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return pieces; 
	}

	/**
	 * 从输入流加载棋盘
	 * 
	 * @param in
	 *            输入流
	 * @return 加载的棋盘
	 */
//	public static ChessBoard load(InputStream input) {
//		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//		List<ChessPiece> pieces = new ArrayList<ChessPiece>();
//		String line = null;
//		while ((line = reader.readLine()) != null) {
//			String[] datas = line.split("[\\s]");
//			ChessPieceType type = ChessPieceType.valueOf(datas[1].trim());
//			ChessRole role = ChessRole.valueOf(datas[0].trim());
//			Point location = new Point(Integer.parseInt(datas[2]), Integer.parseInt(datas[3]));
//
//			ChessPiece piece = null;
//			if (ChessPieceType.KING.equals(type)) {
//				piece = new ChessPieceKing(role);
//			} else if (ChessPieceType.ROOK.equals(type)) {
//				piece = new ChessPieceRook(role);
//			} else if (ChessPieceType.KNIGHT.equals(type)) {
//				piece = new ChessPieceKnight(role);
//			} else if (ChessPieceType.BISHOP.equals(type)) {
//				piece = new ChessPieceBishop(role);
//			} else if (ChessPieceType.GUARD.equals(type)) {
//				piece = new ChessPieceGuard(role);
//			} else if (ChessPieceType.CANNON.equals(type)) {
//				piece = new ChessPieceCannon(role);
//			} else if (ChessPieceType.PAWN.equals(type)) {
//				piece = new ChessPiecePawn(role);
//			}
//			piece.setLocation(location);
//			pieces.add(piece);
//		}
//
//	}
}