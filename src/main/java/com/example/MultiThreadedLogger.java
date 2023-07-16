import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedLogger {
    private static volatile int counter = 0;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.submit(() -> logToFile(250, 1));
        executor.submit(() -> logToFile(500, 2));
        executor.submit(() -> logToFile(1000, 3));

        executor.shutdown();
    }

    private static void logToFile(int delay, int threadNumber) {
        while (counter < 240) {
            try {
                Thread.sleep(delay);
                synchronized (lock) {
                    if (counter < 240) {
                        counter++;
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
                            writer.write("Thread " + threadNumber + ", Time: " + LocalDateTime.now() + ", Counter: " + counter + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

