package priv.hypo.chess.model;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

import priv.hypo.chess.util.AnimationUtil;
/**
 * 动画类
 * @author Hypo
 */
public class Animation {
	
	// Fields
	private Queue<Point> frames = new LinkedList<Point>();
	private int zIndex;
	private Callback callback;
	
	public boolean isStoped() {
		return frames != null && frames.isEmpty();
	}
	
	public Callback getCallback() {
		return callback;
	}
	
	public int getZIndex() {
		return zIndex;
	}
	public void setZIndex(int zIndex) {
		this.zIndex = zIndex;
	}
	
	// Constructors
	public Animation() {
		
	}
	
	public Animation(Queue<Point> frames, int zIndex, Callback callback) {
		this.frames = frames;
		this.zIndex = zIndex;
		this.callback = callback;
	}
	
	// Methods
	public Point next() {
		return frames.poll();
	}
	
	public void setLine(Point origin, Point target, int frameCount, Callback callback) {
		this.frames = AnimationUtil.moveLine(origin, target, frameCount);
		this.callback = callback;
	}
	
	public void invokeCallback() {
		if(callback != null) {
			callback.invoke();
		}
	}
	
	// Inner interfaces
	public static interface Callback{
		public abstract void invoke();
	}
}