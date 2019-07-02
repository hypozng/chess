package priv.hypo.chess.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import priv.hypo.chess.model.ChessBoard;
import priv.hypo.chess.model.ChessPiece;
import priv.hypo.chess.model.ChessPieceBishop;
import priv.hypo.chess.model.ChessPieceCannon;
import priv.hypo.chess.model.ChessPieceGuard;
import priv.hypo.chess.model.ChessPieceKing;
import priv.hypo.chess.model.ChessPieceKnight;
import priv.hypo.chess.model.ChessPiecePawn;
import priv.hypo.chess.model.ChessPieceRook;
import priv.hypo.chess.model.ChessRole;

public class ChessBoardDebuger {
	private static ChessBoard chessBoard;
	public static void debug(String[] args) throws Exception {
		List<ChessPiece> pieces;
		blackInitialize();
		StringBuilder text = new StringBuilder();
		pieces = chessBoard.getPieces(ChessRole.RED);
		for(ChessPiece piece : pieces) {
			text.append(piece.getRole() + " " + piece.getType() + " " + piece.getLocation().x + " " + piece.getLocation().y + "\r\n");
		}
		pieces = chessBoard.getPieces(ChessRole.BLACK);
		for(ChessPiece piece : pieces) {
			text.append(piece.getRole() + " " + piece.getType() + " " + piece.getLocation().x + " " + piece.getLocation().y + "\r\n");
		}
		OutputStream out = new FileOutputStream("D:\\temp\\boardData_black.txt");
		out.write(text.toString().getBytes());
		out.close();
		System.out.println(text);
	}
	
	public static void redInitialize() {
		chessBoard = new ChessBoard();
		chessBoard.setPiece(4, 0, new ChessPieceKing(ChessRole.BLACK));
		chessBoard.setPiece(4, 9, new ChessPieceKing(ChessRole.RED));

		chessBoard.setPiece(0, 0, new ChessPieceRook(ChessRole.BLACK));
		chessBoard.setPiece(8, 0, new ChessPieceRook(ChessRole.BLACK));
		chessBoard.setPiece(1, 0, new ChessPieceKnight(ChessRole.BLACK));
		chessBoard.setPiece(7, 0, new ChessPieceKnight(ChessRole.BLACK));
		chessBoard.setPiece(2, 0, new ChessPieceBishop(ChessRole.BLACK));
		chessBoard.setPiece(6, 0, new ChessPieceBishop(ChessRole.BLACK));
		chessBoard.setPiece(3, 0, new ChessPieceGuard(ChessRole.BLACK));
		chessBoard.setPiece(5, 0, new ChessPieceGuard(ChessRole.BLACK));
		chessBoard.setPiece(1, 2, new ChessPieceCannon(ChessRole.BLACK));
		chessBoard.setPiece(7, 2, new ChessPieceCannon(ChessRole.BLACK));
		chessBoard.setPiece(0, 3, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(2, 3, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(4, 3, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(6, 3, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(8, 3, new ChessPiecePawn(ChessRole.BLACK));
		
		chessBoard.setPiece(0, 9, new ChessPieceRook(ChessRole.RED));
		chessBoard.setPiece(8, 9, new ChessPieceRook(ChessRole.RED));
		chessBoard.setPiece(1, 9, new ChessPieceKnight(ChessRole.RED));
		chessBoard.setPiece(7, 9, new ChessPieceKnight(ChessRole.RED));
		chessBoard.setPiece(2, 9, new ChessPieceBishop(ChessRole.RED));
		chessBoard.setPiece(6, 9, new ChessPieceBishop(ChessRole.RED));
		chessBoard.setPiece(3, 9, new ChessPieceGuard(ChessRole.RED));
		chessBoard.setPiece(5, 9, new ChessPieceGuard(ChessRole.RED));
		chessBoard.setPiece(1, 7, new ChessPieceCannon(ChessRole.RED));
		chessBoard.setPiece(7, 7, new ChessPieceCannon(ChessRole.RED));
		chessBoard.setPiece(0, 6, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(2, 6, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(4, 6, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(6, 6, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(8, 6, new ChessPiecePawn(ChessRole.RED));
	}
	
	public static void blackInitialize() {
		chessBoard = new ChessBoard();
		chessBoard.setPiece(4, 0, new ChessPieceKing(ChessRole.RED));
		chessBoard.setPiece(4, 9, new ChessPieceKing(ChessRole.BLACK));

		chessBoard.setPiece(0, 9, new ChessPieceRook(ChessRole.BLACK));
		chessBoard.setPiece(8, 9, new ChessPieceRook(ChessRole.BLACK));
		chessBoard.setPiece(1, 9, new ChessPieceKnight(ChessRole.BLACK));
		chessBoard.setPiece(7, 9, new ChessPieceKnight(ChessRole.BLACK));
		chessBoard.setPiece(2, 9, new ChessPieceBishop(ChessRole.BLACK));
		chessBoard.setPiece(6, 9, new ChessPieceBishop(ChessRole.BLACK));
		chessBoard.setPiece(3, 9, new ChessPieceGuard(ChessRole.BLACK));
		chessBoard.setPiece(5, 9, new ChessPieceGuard(ChessRole.BLACK));
		chessBoard.setPiece(1, 7, new ChessPieceCannon(ChessRole.BLACK));
		chessBoard.setPiece(7, 7, new ChessPieceCannon(ChessRole.BLACK));
		chessBoard.setPiece(0, 6, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(2, 6, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(4, 6, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(6, 6, new ChessPiecePawn(ChessRole.BLACK));
		chessBoard.setPiece(8, 6, new ChessPiecePawn(ChessRole.BLACK));
		
		chessBoard.setPiece(0, 0, new ChessPieceRook(ChessRole.RED));
		chessBoard.setPiece(8, 0, new ChessPieceRook(ChessRole.RED));
		chessBoard.setPiece(1, 0, new ChessPieceKnight(ChessRole.RED));
		chessBoard.setPiece(7, 0, new ChessPieceKnight(ChessRole.RED));
		chessBoard.setPiece(2, 0, new ChessPieceBishop(ChessRole.RED));
		chessBoard.setPiece(6, 0, new ChessPieceBishop(ChessRole.RED));
		chessBoard.setPiece(3, 0, new ChessPieceGuard(ChessRole.RED));
		chessBoard.setPiece(5, 0, new ChessPieceGuard(ChessRole.RED));
		chessBoard.setPiece(1, 2, new ChessPieceCannon(ChessRole.RED));
		chessBoard.setPiece(7, 2, new ChessPieceCannon(ChessRole.RED));
		chessBoard.setPiece(0, 3, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(2, 3, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(4, 3, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(6, 3, new ChessPiecePawn(ChessRole.RED));
		chessBoard.setPiece(8, 3, new ChessPiecePawn(ChessRole.RED));
	}
}