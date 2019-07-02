package priv.hypo.chess.component;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import priv.hypo.chess.common.Global;

/**
 * 字段
 * @author Hypo
 * @date 2019-03-08
 */
public class FieldPanel<T extends JComponent> extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 字段名
	private JLabel label;
	
	// 字段
	private T field;

	public FieldPanel(String label, T field) {
		this.label = new JLabel(label);
		this.field = field;
		init();
	}
	
	public JLabel getLabel() {
		return label;
	}
	
	public T getField() {
		return field;
	}
	
	private void init() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10);
		setLayout(layout);
		label.setSize(100, label.getHeight());
		label.setFont(Global.DEFAULT_FONT);
		add(label, BorderLayout.WEST);
		add(field, BorderLayout.CENTER);
	}
}