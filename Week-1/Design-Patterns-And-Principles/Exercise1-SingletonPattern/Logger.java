import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {


    private static volatile Logger instance;

  
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int logCount;

   
    private Logger() {
        this.logCount = 0;
        System.out.println("[Logger] Logger instance created. (You should see this only ONCE!)");
    }

    public static Logger getInstance() {
        if (instance == null) {                    
            synchronized (Logger.class) {          
                if (instance == null) {            
                    instance = new Logger();
                }
            }
        }
        return instance;
    }


    public void info(String message) {
        log("INFO ", message);
    }

    public void warn(String message) {
        log("WARN ", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    private void log(String level, String message) {
        logCount++;
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] [Instance@%s] %s%n",
                timestamp, level, Integer.toHexString(System.identityHashCode(this)), message);
    }

    public int getLogCount() {
        return logCount;
    }

    /** Returns the memory address of this instance (for verification in tests). */
    public String getInstanceAddress() {
        return Integer.toHexString(System.identityHashCode(this));
    }
}
