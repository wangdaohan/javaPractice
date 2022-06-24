package design_pattern.observer;

public class SinaObserver implements Observer {

    private float temperature;
    private float pressure;
    private float humidity;


    @Override
    public void update(float temperature, float presurre, float humidity) {
        this.temperature = temperature;
        this.pressure = presurre;
        this.humidity = humidity;
        display();
    }

    public void display(){
        System.out.println("****Today temp:"+ temperature);
        System.out.println("****Today pressure:"+ pressure);
        System.out.println("****Today humidity:"+ humidity);
    }
}
