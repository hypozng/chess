package priv.hypo.chess.util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;
/**
 * 动画工具
 * @author Hypo
 */
public class AnimationUtil {
	
	/**
	 * 直线移动
	 * @param origin 原点
	 * @param target 终点
	 * @param frameCount 动画帧数
	 * @return 帧
	 */
	public static Queue<Point> moveLine(Point origin, Point target, int frameCount) {
		if(origin == null || target == null) {
			return null;
		}
		frameCount = frameCount <= 2 ? 2 : frameCount;
		int x = origin.x, y = origin.y;
		int tx = target.x, ty = target.y;
		int ax = (tx - x) / frameCount, ay = (ty - y) / frameCount;
		Queue<Point> frames = new LinkedList<Point>();
		for(int i = 0; i <= frameCount; ++i) {
			frames.offer(new Point(x + ax * i, y + ay * i));
		}
		return frames;
	}
	
	/**
	 * 获取停留的运行轨迹
	 * @param location 位置
	 * @param frameCount 暂停的帧数
	 * @return 运行轨迹队列
	 */
	public static Queue<Point> pause(Point location, int frameCount) {
		if(location == null) {
			return null;
		}
		frameCount = frameCount <= 2 ? 2 : frameCount;
		Queue<Point> frames = new LinkedList<Point>();
		for(int i = 0; i <= frameCount; ++i) {
			frames.offer(location);
		}
		return frames;
	}

}