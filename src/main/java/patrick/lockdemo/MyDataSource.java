package patrick.lockdemo;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自己实现的数据库连接池
 */
public class MyDataSource {

    private LinkedList<Connection> pool = new LinkedList();
    private static final int INIT_CONNECTIONS = 10;

    private static final String DRIVER_CLASS = "";
    private static final String user = "";
    private static final String password = "";
    private static final String url = "";

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    static {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
    }

    public MyDataSource(){
        for(int i=0;i<INIT_CONNECTIONS; i++) {
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                pool.addLast(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnect(){
        Connection result = null;
        //synchronized (pool) {
        lock.lock();
        try{
            while (pool.size() <= 0) {
                //等待
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(!pool.isEmpty()){
                result = pool.removeFirst();
            }
            return result;
        }finally {
            lock.unlock();
        }


    }

    public void release(Connection conn){
        if(conn != null){
            //synchronized (pool){
            lock.lock();
            try{
                   pool.addLast(conn);
                   //notifyAll();
                condition.signal();
            }finally {
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {

    }
}
