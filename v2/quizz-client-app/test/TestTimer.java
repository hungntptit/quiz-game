
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class TestTimer {

    static int interval;
    static Timer timer;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input seconds => : ");
        int interval = Integer.parseInt(sc.nextLine());
        Instant startTime = Instant.now();
        Instant endTime = startTime.plus(interval, ChronoUnit.SECONDS);
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Duration duration = Duration.between(Instant.now(), endTime);
                long hours = duration.toHours();
                long minutes = duration.toMinutes();
                long seconds = duration.getSeconds() % 60;
                System.out.println(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }
        }, delay, period);
    }

}
