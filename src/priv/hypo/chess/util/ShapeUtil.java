package priv.hypo.chess.util;

import java.awt.*;
/**
 * 形状工具
 * @author Hypo
 */
public class ShapeUtil {
	
	/**
	 * 判断一个点是否在一个矩形范围内
	 * @param point 要判断的点
	 * @param rect 矩形范围
	 * @return 如果该点在该范围内则返回true，否则返回false
	 */
	public static boolean inRect(Point point, Rectangle rect) {
		if(point == null || rect == null) {
			return false;
		}
		return inRect(point.x, point.y, rect.x, rect.y, rect.width, rect.height);
	}
	
	/**
	 * 判断一个点是否在一个矩形范围内
	 * @param px 要判断的点的x坐标
	 * @param py 要判断的点的y坐标
	 * @param rx 矩形范围的x坐标
	 * @param ry 矩形范围的y坐标
	 * @param width 矩形范围的宽度
	 * @param height 矩形范围的高度
	 * @return 如果该点在该矩形范围内则返回true，否则返回false
	 */
	public static boolean inRect(int px, int py, int rx, int ry, int width, int height) {
		return (px >= rx && px < rx + width)
			&& (py >= ry && py < ry + height);
	}
	
	/**
	 * 判断一个点是否在一个矩形范围内
	 * @param x 要判断的点的x坐标
	 * @param y 要判断的点的y坐标
	 * @param rect 矩形范围
	 * @return
	 */
	public static boolean inRect(int x, int y, Rectangle rect) {
		if(rect == null) {
			return false;
		}
		return inRect(x, y, rect.x, rect.y, rect.width, rect.height);
	}
	
	/**
	 * 将两个点的X值和Y值分别相加
	 * @param x1 第一个点的X值
	 * @param y1 第一个点的Y值
	 * @param x2 第二个点的X值
	 * @param y2 第二个点的Y值
	 * @return 相加后生成的第三个点
	 */
	public static Point add(int x1, int y1, int x2, int y2) {
		return new Point(x1 + x2, y1 + y2);
	}
	
	/**
	 * 将两个点的X值和Y值分别相加
	 * @param point 第一个点
	 * @param x 第二个点的X值
	 * @param y 第二个点的Y值
	 * @return 相加后生成的第三个点
	 */
	public static Point add(Point point, int x, int y) {
		if(point == null) {
			return new Point(x, y);
		}
		return add(point.x, point.y, x, y);
	}
	
	/**
	 * 将两个点的X值和Y值分别相加
	 * @param point1 第一个点
	 * @param point2 第二个点
	 * @return 相加后生成的第三个点
	 */
	public static Point add(Point point1, Point point2) {
		if(point1 == null) {
			if(point2 == null) {
				return null;
			} else {
				return point2;
			}
		} else if(point2 == null) {
			return point1;
		}
		return add(point1.x, point1.y, point2.x, point2.y);
	}
	
	/**
	 * 将一个点的X值和Y值的相反数生成一个新的点
	 * @param x 点的X值
	 * @param y 点的Y值
	 * @return 处理后生成的新的点
	 */
	public static Point opposite(int x, int y) {
		return new Point(-x, -y);
	}
	
	/**
	 * 将一个点的X值和Y值的相反数生成一个新的点
	 * @param point 待处理的点
	 * @return 处理后生成的新的点
	 */
	public static Point opposite(Point point){
		if(point == null) {
			return null;
		}
		return new Point(point.x, point.y);
	}
}