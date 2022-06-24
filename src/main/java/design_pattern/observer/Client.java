package design_pattern.observer;


/**
 * 观察者模式：observer
 *
 * 实际应用：
 *    天气预报项目需求，具体要求如下：
 *       1）气象站可以将每天测量到的温度，湿度，气压等以公告的形式发布出去（比如发布到自己网站或第三方）
 *       2） 需要设计开放型API， 便于其他第三方也能接入气象站获取数据
 *       2） 可以动态加入第三方接收数据
 *       3）提供温度，气压，湿度的接口
 *       4）测量数据更新时，要能实时的通知第三方
 *
 *      观察者模式：
 *        1）气象局： subject
 *             Subject:
 *               registerObserver
 *               removeObserver
 *               notifyObserver
 *        2) 用户/第三方网络： Observer
 *             Observer:
 *                update
 */
public class Client {
    public static void main(String[] args) {
        WeathreSubject weathreSubject = new WeathreSubject();


        Observer o1 = new SinaObserver();
        weathreSubject.registerObserver(o1);

        weathreSubject.setData(50,100,30);
        weathreSubject.notifyObserver();
    }
}
