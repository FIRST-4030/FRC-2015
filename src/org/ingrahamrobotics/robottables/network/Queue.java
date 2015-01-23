package org.ingrahamrobotics.robottables.network;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.ingrahamrobotics.robottables.Message;

public class Queue implements IO.ListenEvents {

    private static final int QUEUE_SIZE = 100;
    private static final int QUEUE_WARN_SIZE = QUEUE_SIZE / 2;

    private final BlockingQueue<Message> queue;
    private final QueueEvents handler;

    public Queue(QueueEvents handler) {
        this.handler = handler;
        this.queue = new ArrayBlockingQueue<Message>(QUEUE_SIZE);
    }

    public void put(Message msg) {
        int size = queue.size();
        if (!queue.offer(msg)) {
            while (queue.size() > QUEUE_WARN_SIZE) {
                queue.poll();
            }
            if (handler != null) {
                handler.queueError(size, true, QUEUE_WARN_SIZE);
            }
        } else if (size > QUEUE_WARN_SIZE && handler != null) {
            handler.queueError(size, false, QUEUE_WARN_SIZE);
        }
    }

    public Message get() {
        Message msg = null;
        while (msg == null) {
            try {
                msg = queue.take();
            } catch (InterruptedException ex) {
            }
        }
        return msg;
    }

    public void recv(String data) {
        try {
            Message msg = new Message(data);
            put(msg);
        } catch (IllegalArgumentException ex) {
            System.err.println("Unable to parse message: " + ex.toString() + "\n\t" + data);
        }
    }

    public void error(String err) {
        System.err.println("Err: " + err);
    }

    public interface QueueEvents {

        public void queueError(int size, boolean draining, int targetSize);
    }
}
