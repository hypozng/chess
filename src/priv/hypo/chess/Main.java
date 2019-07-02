package priv.hypo.chess;

import javax.swing.JFrame;

import priv.hypo.chess.frame.ChessFrame;
/**
 * 程序入口 
 * @author Hypo
 */
public class Main {

	public static void main(String[] args) throws Exception {
		ChessFrame frame = new ChessFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}