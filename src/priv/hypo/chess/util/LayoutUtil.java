package priv.hypo.chess.util;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * 布局工具
 * 
 * @author Hypo
 */
public class LayoutUtil {
	/** 左对齐 */
	public static final int LEFT = 0x00000001;
	/** 右对齐 */
	public static final int RIGHT = 0x00000002;
	/** 水平居中 */
	public static final int CENTER_HORIZONTAL = 0x00000004;
	/** 顶部对齐 */
	public static final int TOP = 0x00000010;
	/** 底部对齐 */
	public static final int BOTTOM = 0x00000020;
	/** 垂直居中 */
	public static final int CENTER_VERTICAL = 0x00000040;
	/** 居中对齐 */
	public static final int CENTER = CENTER_HORIZONTAL | CENTER_VERTICAL;

	/**
	 * 调整第二个矩形的位置，调整后第二个矩形以第一个矩形为基准按照指定的方式对齐
	 * 
	 * @param align
	 *            对齐方式
	 * @param x1
	 *            第一个矩形的位置的x坐标
	 * @param y1
	 *            第一个矩形的位置的y坐标
	 * @param width1
	 *            第一个矩形的宽度
	 * @param height1
	 *            第一个矩形的高度
	 * @param x2
	 *            第二个矩形的x坐标
	 * @param y2
	 *            第二个矩形的y坐标
	 * @param width2
	 *            第二个矩形的宽度
	 * @param height2
	 *            第二个矩形的高度
	 * @return 调整后第二个矩形的位置
	 */
	public static Point align(int align, int x1, int y1, int width1, int height1,
			int x2, int y2, int width2, int height2) {
		int x = 0, y = 0;
		int halign = 0x0000000f & align;
		int valign = 0x000000f0 & align;
		switch (halign) {
		case LEFT:
			x = x1;
			break;
		case RIGHT:
			x = x1 + width1 - width2;
			break;
		case CENTER_HORIZONTAL:
			x = (width1 - width2) / 2 + x1;
			break;
		}
		switch (valign) {
		case TOP:
			y = y1;
			break;
		case BOTTOM:
			y = y1 + height1 - height2;
			break;
		case CENTER_VERTICAL:
			y = (height1 - height2) / 2 + y1;
			break;
		}
		return new Point(x, y);
	}

	/**
	 * 调整第二个矩形的位置，调整后第二个矩形以第一个矩形为基准按照指定的方式对齐
	 * 
	 * @param align
	 *            对齐方式
	 * @param rect1
	 *            第一个矩形
	 * @param rect2
	 *            第二个矩形
	 * @return 调整后第二个矩形的位置
	 */
	public static Point align(int align, Rectangle rect1, Rectangle rect2) {
		if (rect1 == null || rect2 == null) {
			return null;
		}
		return align(align, rect1.x, rect1.y, rect1.width, rect1.height, 
				rect2.x, rect2.y, rect2.width, rect2.height);
	}

	/**
	 * 调整第二个矩形的位置，调整后第二个矩形以第一个矩形为基准按照指定的方式对齐
	 * 
	 * @param align
	 *            对齐方式
	 * @param location1
	 *            第一个矩形的位置
	 * @param size1
	 *            第一个矩形的大小
	 * @param location2
	 *            第二个矩形的位置
	 * @param size2
	 *            第二个矩形的大小
	 * @return 调整后第二个矩形的位置
	 */
	public static Point align(int align, Point location1, Dimension size1, Point location2, Dimension size2) {
		if (size1 == null || size2 == null) {
			return null;
		}
		if (location1 == null) {
			location1 = new Point();
		}
		if (location2 == null) {
			location2 = new Point();
		}
		return align(align, location1.x, location1.y, size1.width, size1.height, location2.x, location2.y, size2.width,
				size2.height);
	}

	/**
	 * 调整第二个矩形的位置，调整后第二个矩形以第一个矩形为基准按照指定的方式对齐
	 * 
	 * @param align
	 *            对齐方式
	 * @param size1
	 *            第一个矩形的大小
	 * @param size2
	 *            第二个矩形的大小
	 * @return 调整后第二个矩形相对于第一个矩形的位置
	 */
	public static Point align(int align, Dimension size1, Dimension size2) {
		if (size1 == null || size2 == null) {
			return null;
		}
		return align(align, 0, 0, size1.width, size1.height, 0, 0, size2.width, size2.height);
	}
	
	/**
	 * 获取字符串打印边界
	 * @param graphics
	 * @param text
	 * @param font
	 * @return
	 */
	public static Rectangle getTextBounds(Graphics graphics, String text, Font font) {
		if(graphics == null || text == null) {
			return null;
		}
		Rectangle bounds = new Rectangle();
		FontMetrics metrics = null;
		if(font != null) {
			metrics = graphics.getFontMetrics(font);
		} else {
			metrics = graphics.getFontMetrics();
		}
		Rectangle2D textBounds = metrics.getStringBounds(text, graphics);
		bounds.x = (int)(Math.round(textBounds.getX()));
		bounds.y = (int)(Math.round(textBounds.getY()));
		bounds.width = (int)(Math.round(textBounds.getWidth()));
		bounds.height = (int)(Math.round(textBounds.getHeight()));
		return bounds;
	}
	
	
	/**
	 * 对齐文字
	 * @param graphics 画笔
	 * @param bounds 绘制范围
	 * @param text 需要绘制的文本
	 * @param font 使用的字体
	 * @param align 对齐方式
	 * @return 绘制坐标
	 */
	public static Point alignText(Graphics graphics, Rectangle bounds, String text, Font font, int align) {
		if(graphics == null || bounds == null || text == null);
		Rectangle textBounds = getTextBounds(graphics, text, font);
		int ascenter = -textBounds.y;
		Point location = align(align, bounds, textBounds);
		location.y += ascenter;
		return location;
	}
}