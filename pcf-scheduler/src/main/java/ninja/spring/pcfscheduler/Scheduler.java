package ninja.spring.pcfscheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;

public class Scheduler {

    public static void main(String[] args) throws UnknownHostException {
        System.out.printf("IP Address: %s, Thread: %s, Time: %s%n",
                InetAddress.getLocalHost(),
                Thread.currentThread().getName(),
                LocalTime.now());
    }
}
