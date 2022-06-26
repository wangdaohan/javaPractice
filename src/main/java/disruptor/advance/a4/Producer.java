package disruptor.advance.a4;

import com.lmax.disruptor.RingBuffer;

public class Producer {
    private RingBuffer<Order> ringBuffer;
    public Producer(RingBuffer<Order> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {
        long nextSequence = ringBuffer.next();
        try{
            Order order = ringBuffer.get(nextSequence);
            order.setId(uuid);
        } finally {
            ringBuffer.publish(nextSequence);
        }
    }
}
