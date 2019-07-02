package priv.hypo.chess.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import priv.hypo.chess.model.ChessBoard;
import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessRole;

/**
 * 对局分析器
 * @author hypo
 * @date 2019-03-12
 */
public class SituationAnalyzer {
	private ChessBoard chessBoard = new ChessBoard();
	
	// 棋盘布局版本号
	private long boardLayoutVersion;
	// 威胁棋子分析数据
	private Map<ChessPiece, List<ChessPiece>> threatenedPieces = new HashMap<ChessPiece, List<ChessPiece>>();
	// 危险目标点分析数据
	private Map<ChessRole, Set<Point>> dangerousTargets = new HashMap<ChessRole, Set<Point>>();
	
	public SituationAnalyzer(ChessBoard chessBoard) {
		this.chessBoard = chessBoard;
	}
	
	/**
	 * 刷新棋盘布局版本号，若布局版本号发生变化，则清空分析数据
	 */
	private void refreshLayoutVersion() {
		if(boardLayoutVersion != chessBoard.getLayoutVersion()) {
			threatenedPieces.clear();
			dangerousTargets.clear();
			boardLayoutVersion = chessBoard.getLayoutVersion();
		}
	}
	
	/**
	 * 查找对参照棋子具有威胁的棋子
	 *  如果A棋子能够走一步把B棋子吃掉，则A棋子是对B棋子具有威胁的棋子
	 * @param piece 作为参照的棋子
	 * @return 所有对参照棋子具有威胁的棋子
	 */
	public List<ChessPiece> findThreatenedPieces(ChessPiece piece) {
		refreshLayoutVersion();
		
		if(!threatenedPieces.containsKey(piece)) {
			List<ChessPiece> threatenedPieces = new ArrayList<ChessPiece>();
			if(piece != null) {
				List<ChessPiece> enemyPieces = chessBoard.getPieces(piece.getRole().getEnemy());
				for(ChessPiece enemyPiece : enemyPieces) {
					if(enemyPiece.getValidTargets().contains(piece.getLocation())) {
						threatenedPieces.add(enemyPiece);
					}
				}
			}
			this.threatenedPieces.put(piece, threatenedPieces);
		}
		
		return threatenedPieces.get(piece);
	}

	/**
	 * 查找参照角色的危险目标
	 * 	如果参照角色的棋子移动到一个目标后，敌方角色能够走一步把该棋子吃掉，则该目标时危险目标
	 * @param role 参照角色
	 * @return 参照角色的危险目标
	 */
	public Set<Point> findDangerousTargets(ChessRole role) {
		refreshLayoutVersion();
		
		if (!dangerousTargets.containsKey(role)) {
			Set<Point> targets = new HashSet<Point>();
			List<ChessPiece> enemies = chessBoard.getPieces(role.getEnemy());
			for (ChessPiece enemy : enemies) {
				targets.addAll(enemy.getValidTargets());
			}
			this.dangerousTargets.put(role, targets);
		}
		
		return dangerousTargets.get(role);
	}
}