package disruptor.quickstart;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer bb){
        //1. ringbuffer get available sequence
        long nextSequence = ringBuffer.next();
        try {
            //2. use sequence get OrderEvent
            OrderEvent orderEvent = ringBuffer.get(nextSequence);
            orderEvent.setValue(bb.getLong(0));
        }finally {
            ringBuffer.publish(nextSequence);
        }
    }
}
