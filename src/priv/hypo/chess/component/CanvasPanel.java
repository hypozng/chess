package priv.hypo.chess.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * 画板
 */
public class CanvasPanel extends JPanel {
	private static final long serialVersionUID = 922469580475377972L;
	
	private BufferedImage content;
	
	public CanvasPanel() {
		super();
	}
	
	public CanvasPanel(int width, int height) {
		this();
		setSize(width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		if(content == null) {
			return;
		}
		
		g.drawImage(content, 0, 0, this);
	}
	
	/**
	 * 获取显示内容
	 * @return 
	 */
	public BufferedImage getContent() {
		return content;
	}
	
	/**
	 * 开始绘图
	 * @return 用于绘图的画笔
	 */
	public Graphics beginPaint() {
		content = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		return content.getGraphics();
	}
	
	/**
	 * 结束绘图
	 */
	public void finishPaint() {
		refresh();
	}
	
	/**
	 * 刷新显示内容
	 */
	public void refresh() {
		this.repaint();
	}
}