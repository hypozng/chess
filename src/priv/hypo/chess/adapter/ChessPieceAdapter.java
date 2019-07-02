package priv.hypo.chess.adapter;

import priv.hypo.chess.event.ChessPieceEvent;
import priv.hypo.chess.listener.ChessPieceListener;

/**
 * 棋子事件适配器类
 * @author Hypo
 */
public abstract class ChessPieceAdapter implements ChessPieceListener {
    /**
     * {@inheritDoc}
     */
	public void onSelected(ChessPieceEvent e) { }

    /**
     * {@inheritDoc}
     */
	public void onDeselected(ChessPieceEvent e) { }

    /**
     * {@inheritDoc}
     */
	public void onDied(ChessPieceEvent e) { }

    /**
     * {@inheritDoc}
     */
	public void onMoved(ChessPieceEvent e) { }
	
}