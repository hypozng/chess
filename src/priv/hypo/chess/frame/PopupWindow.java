package priv.hypo.chess.frame;

import priv.hypo.chess.common.Global;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * 弹出窗口
 * @author Hypo
 */
public class PopupWindow extends Dialog {
	private static final long serialVersionUID = 2200501715752352399L;
	public static final int TITLE_HEIGHT = 24;
	protected Font font = Global.DEFAULT_FONT;

    private Panel rootPane;
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

    /**
     * 初始化页面布局
     */
	private void initialize() {
		//
		// PopupWindow
		//
		this.setSize(300, 300);
        this.setUndecorated(true);
        this.setLayout(null);
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                if (rootPane != null) {
                    int width = PopupWindow.this.getWidth() - Global.CONTENT_PADDING * 2;
                    int height = PopupWindow.this.getHeight() - Global.CONTENT_PADDING * 2;
                    rootPane.setSize(new Dimension(width, height));
                }
            }

        });
        //
        // rootPane
        //
        rootPane = new Panel();
        rootPane.setBounds(5, 5, this.getWidth() - 10, this.getHeight() - 10);
        rootPane.setBackground(Color.white);
        rootPane.setLayout(new BorderLayout());
        rootPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (titlePane != null) {
                    int width = rootPane.getWidth();
                    int height = Global.TITLE_HEIGHT;
                    titlePane.setSize(new Dimension(width, height));
                }
            }
        });
        this.add(rootPane);
		//
		// titlePane
		//
		titlePane = new Panel();
		titlePane.setBackground(Color.white);
        titlePane.setSize(new Dimension(0, TITLE_HEIGHT));
		titlePane.setLayout(new BorderLayout());
        titlePane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (textLabel != null) {
                    int width = titlePane.getWidth() - Global.TITLE_HEIGHT * 2;
                    int height = Global.TITLE_HEIGHT;
                    textLabel.setSize(new Dimension(width, height));
                }
                if (closePane != null) {
                    int x = titlePane.getWidth() - closePane.getWidth();
                    int y = closePane.getY();
                    closePane.setLocation(new Point(x, y));
                }
            }
        });
		rootPane.add(titlePane, BorderLayout.NORTH);
        //
        // iconPane
        //
        iconPane = new Panel() {
            private static final long serialVersionUID = -3203123809404933488L;

            @Override
            public void paint(Graphics graphics) {
                super.paint(graphics);
                if (iconImage == null) {
                    return;
                }
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int padding = 2;
                int left = 2, top = 2,
                    width = getWidth() - padding * 2,
                    height = getHeight() - padding * 2;
                g2d.drawImage(iconImage, left, top, width, height, this);
            }

        };
        iconPane.setPreferredSize(new Dimension(titlePane.getHeight(), 0));
        titlePane.add(iconPane, BorderLayout.WEST);
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
        closePane.setPreferredSize(new Dimension(titlePane.getHeight(), 0));
        closePane.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    PopupWindow.this.dispose();
                }
            }

        });
        titlePane.add(closePane, BorderLayout.EAST);
        //
        // textLabel
        //
        textLabel = new Label("TWindow");
        titlePane.add(textLabel, BorderLayout.CENTER);
        //
		// contentPane
		//
		contentPane = new Panel();
		rootPane.add(contentPane, BorderLayout.CENTER);
	}
	
	public void setTitle(String title) {
		textLabel.setText(title);
	}
	public String getTitle() {
		return textLabel.getText();
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