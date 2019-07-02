package priv.hypo.chess.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * 弹出窗口
 * @author Hypo
 */
public class PopupWindow extends Window{
	private static final long serialVersionUID = 2200501715752352399L;
	public static final int TITLE_HEIGHT = 24;
	protected Font font;
	
	private PopupWindow instance = this;
	private Panel titlePane;
	private Panel contentPane;
	private Panel iconPane;
	private Panel closePane;
	private Label textLabel;
	private Image iconImage;
	
	public PopupWindow(Window window) {
		super(window);
		initialize();
	}
	
	private void initialize() {
		//
		// PopupWindow
		//
		this.setSize(300, 300);
		this.setLayout(null);
		this.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		    	titlePane.setBounds(0, 0, instance.getWidth(), TITLE_HEIGHT);
		    	contentPane.setBounds(0, titlePane.getHeight(), instance.getWidth(), instance.getHeight() - titlePane.getHeight());
		    }
		});
		//
		// titlePane
		//
		titlePane = new Panel();
		titlePane.setBounds(0, 0, this.getWidth(), TITLE_HEIGHT);
		titlePane.setBackground(Color.white);
		titlePane.setLayout(null);
		titlePane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				textLabel.setBounds(iconPane.getWidth(), 0, titlePane.getWidth() - iconPane.getWidth() - closePane.getWidth(), titlePane.getHeight());
				closePane.setLocation(instance.getWidth() - closePane.getWidth(), 0);
			}
		});
		this.add(titlePane, BorderLayout.NORTH);
		//
		// contentPane
		//
		contentPane = new Panel();
		contentPane.setBounds(0, titlePane.getHeight(), this.getWidth(), this.getHeight() - titlePane.getHeight());
		this.add(contentPane);
		// 
		// iconPane
		//
		iconPane = new Panel() {
			private static final long serialVersionUID = -3203123809404933488L;

			public void paint(Graphics graphics) {
				if(iconImage != null) {
					graphics.drawImage(iconImage, 2, 2, this.getWidth() - 4, this.getHeight() - 4, this);
				}
			}
		};
		iconPane.setBounds(0, 0, titlePane.getHeight(), titlePane.getHeight());
		titlePane.add(iconPane);
		//
		// closePane
		//
		closePane = new Panel() {
			private static final long serialVersionUID = -498484906468156657L;
			public void paint(Graphics graphics) {
				Color oldColor = graphics.getColor();
				int x1 = this.getWidth() - this.getHeight() + (this.getHeight() - 9) / 2;
				int y1 = (this.getHeight() - 9) / 2;
				int x2 = x1 + 9, y2 = y1 + 9;
				graphics.setColor(Color.black);
				graphics.drawLine(x1, y1, x2, y2);
				graphics.drawLine(x2, y1, x1, y2);
				graphics.setColor(oldColor);
			}
		};
		closePane.setSize(TITLE_HEIGHT, TITLE_HEIGHT);
		closePane.setLocation(instance.getWidth() - closePane.getWidth(), 0);
		closePane.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					instance.dispose();
				}
			}
		});
		titlePane.add(closePane);
		//
		// textLabel
		//
		textLabel = new Label("TWindow");
		textLabel.setBounds(iconPane.getWidth(), 0, titlePane.getWidth() - iconPane.getWidth() - closePane.getWidth(), titlePane.getHeight());
		titlePane.add(textLabel);
	}
	
	public void setTitle(String title) {
		textLabel.setText(title);
	}
	public String getTitle() {
		return textLabel.getText();
	}
	
	public void setTitleColor(Color color) {
		titlePane.setBackground(color);
	}
	public Color getTitleColor() {
		return titlePane.getBackground();
	}
	
	public void setFont(Font font) {
		if(font != null) {
			this.font = font;
			textLabel.setFont(font);
		}
	}
	public Font getFont() {
		return font;
	}
	
	public void setIconImage(Image iconImage) {
		this.iconImage = iconImage;
	}
	public Image getIconImage() {
		return iconImage;
	}
	
	public Panel getTitlePane() {
		return titlePane;
	}
	public void setTitlePane(Panel titlePane) {
		this.titlePane = titlePane;
	}
	
	public Panel getContentPane() {
		return contentPane;
	}
	public void setContentPane(Panel contentPane) {
		this.contentPane = contentPane;
	}
}