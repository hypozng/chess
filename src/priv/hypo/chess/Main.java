package priv.hypo.chess;

import javax.swing.JFrame;

import priv.hypo.chess.frame.ChessFrame;
/**
 * 程序入口 
 * @author Hypo
 */
public class Main {

	public static void main(String[] args) throws Exception {
        // 创建主窗体实例
		ChessFrame frame = new ChessFrame();

        // 设置主窗体关闭退出程序的操作
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 显示主窗体
		frame.setVisible(true);
	}

}