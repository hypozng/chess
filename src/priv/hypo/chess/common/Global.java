package priv.hypo.chess.common;

import java.awt.*;

public class Global {
	/** 软件名 */
	public static final String APP_NAME = "中国象棋";
	
	/** 系统默认字体名 */
	public static final String DEFAULT_FONT_NAME = "Microsoft Yahei UI";

    /** 内边距 */
    public static final int CONTENT_PADDING = 5;

    /** 标题栏高度 */
    public static final int TITLE_HEIGHT = 24;

    /** 广播端口 */
    public static final int BROADCAST_PORT = 1136;

    /** 广播地址 */
    public static final String BROADCAST_HOST = "255.255.255.255";

    /** 广播频率间隔 (ms) */
    public static final int BROADCAST_INTERVAL = 500;

    /** 系统默认字体 */
    public static final Font DEFAULT_FONT= new Font(DEFAULT_FONT_NAME, Font.PLAIN, 12);

    /** 红状态颜色 */
    public static final Color STATE_RED_COLOR = new Color(0x009C0F00);

    /** 黑状态颜色 */
    public static final Color STATE_BLACK_COLOR = new Color(0x000B1619);
}