package priv.hypo.chess.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;

public class DrawUtil {
	
	/**
	 * 绘制文本
	 * @param graphics 绘图的画笔
	 * @param text 绘制的文本
	 * @param location 绘制的位置
	 * @param color 颜色
	 * @param font 字体
	 */
	public static void drawText(Graphics graphics, String text, Point location, Color color, Font font) {
		if(graphics == null || text == null) {
			return;
		}
		Color oldColor = graphics.getColor();
		Font oldFont = graphics.getFont();
		if(color != null) {
			graphics.setColor(color);
		}
		if(font != null) {
			graphics.setFont(font);
		}
		if(location == null) {
			location = new Point();
		}
		graphics.drawString(text, location.x, location.y);
		graphics.setColor(oldColor);
		graphics.setFont(oldFont);
	}
	
	public static void drawText(Graphics graphics, String text) {
		drawText(graphics, text, null, null, null);
	}
	
	public static void drawText(Graphics graphics, String text, Point location) {
		drawText(graphics, text, location, null, null);
	}
	
	public static void drawText(Graphics graphics, String text, Point location, Color color) {
		drawText(graphics, text, location, null, null);
	}
	
	public static void drawImage(Graphics graphics, Image image, Rectangle bounds, ImageObserver observer) {
		if(graphics == null || image == null) {
			return;
		}
		if(bounds == null) {
			bounds = new Rectangle();
			bounds.width = image.getWidth(observer);
			bounds.height = image.getHeight(observer);
		}
		graphics.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, observer);
	}
}