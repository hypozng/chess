package priv.hypo.chess.common;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 监听线程
 */
public abstract class AutomaticQueue<T> extends Thread {
    private final Object lock = new Object();

    private Queue<T> queue = new LinkedList<T>();

    private boolean closed;

    protected int interval = 0;

    @Override
    public void run() {
        init();
        while (!closed) {
            try {
                T item = listen();
                closed = onPoll(item);
                if (interval > 0) {
                    Thread.sleep(interval);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 排队
     * @param item
     */
    public void offer(T item) {
        synchronized (lock) {
            queue.offer(item);
            lock.notifyAll();
        }
    }

    protected abstract boolean onPoll(T item) throws Exception;

    protected void init() {

    }

    private T listen() throws InterruptedException {
        if (queue.isEmpty()) {
            synchronized (lock) {
                lock.wait();
            }
        }
        if (queue.isEmpty()) {
            return null;
        }
        return queue.poll();
    }
}
