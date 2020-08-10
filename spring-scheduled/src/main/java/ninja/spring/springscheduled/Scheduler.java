package ninja.spring.springscheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;

@Component
public class Scheduler {

    @Scheduled(cron = "0 * * * * *")
    private void runScheduler() throws UnknownHostException {
        System.out.printf("IP Address: %s, Thread: %s, Time: %s%n",
                InetAddress.getLocalHost(),
                Thread.currentThread().getName(),
                LocalTime.now());
    }
}
