package priv.hypo.chess.model;

/**
 * 下拉菜单项
 * @author Hypo
 * @param <T> 下拉菜单项绑定的值的类型
 */
public class ComboBoxItem<T> {
	// Fields
	private String text;
	private T value;
	
	// Constructors
	public ComboBoxItem() {
		
	}
	
	public ComboBoxItem(String text, T value) {
		this.text = text;
		this.value = value;
	}
	
	// Property accessors
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public String toString() {
		return text;
	}
}