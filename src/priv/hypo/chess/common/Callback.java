package priv.hypo.chess.common;

/**
 * 可调用的接口，用于回调 
 * @author Administrator
 * @param <T>
 */
public interface Callback<T> {
	/**
	 * 调用
	 * @param arg
	 * @param parans
	 */
	public void invoke(T value);
}