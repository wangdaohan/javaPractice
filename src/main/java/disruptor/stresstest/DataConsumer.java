package disruptor.stresstest;

import com.lmax.disruptor.EventHandler;

public class DataConsumer implements EventHandler<Data> {

    private long startTime;
    private  int i;
    public DataConsumer(){
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(Data data, long l, boolean b) throws Exception {
        System.out.println("received data:"+data.getName());
        i++;
        if (i == Constants.EVENT_NUM_HM) {
            long endTime = System.currentTimeMillis();
            System.out.println("Disruptor costtime = "+ (endTime - startTime)+" ms");
            //ArrayBlockingQueue:cost time = 22886ms
            //Disruptor costtime = 7922 ms
        }
    }
}
