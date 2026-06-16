public class LoggerTest {

   
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RESET  = "\u001B[0m";

    private static int passed = 0;
    private static int failed = 0;


    public static void main(String[] args) throws InterruptedException {

        printBanner("SINGLETON PATTERN — LOGGER TEST SUITE");

        test1_SingleInstanceCheck();
        test2_ReferenceEquality();
        test3_SharedState();
        test4_ThreadSafety();

        printSummary();
    }


    private static void test1_SingleInstanceCheck() {
        printSection("Test 1: Single Instance Check");

        Logger logger1 = Logger.getInstance();
        Logger logger2 = Logger.getInstance();
        Logger logger3 = Logger.getInstance();

        System.out.println("  logger1 address : " + logger1.getInstanceAddress());
        System.out.println("  logger2 address : " + logger2.getInstanceAddress());
        System.out.println("  logger3 address : " + logger3.getInstanceAddress());

        boolean allSame = (logger1 == logger2) && (logger2 == logger3);
        assert_true("All three getInstance() calls return the same object", allSame);
    }

    
    private static void test2_ReferenceEquality() {
        printSection("Test 2: Reference Equality (==)");

        Logger a = Logger.getInstance();
        Logger b = Logger.getInstance();

        assert_true("logger_a == logger_b  (same reference)", a == b);
        assert_true("logger_a.equals(logger_b)", a.equals(b));

        System.out.println("  Logging via logger_a …");
        a.info("Message from reference A");

        System.out.println("  Logging via logger_b …");
        b.warn("Message from reference B");
    }

   
    private static void test3_SharedState() {
        printSection("Test 3: Shared State (logCount)");

        // Reset by getting a fresh handle — still same instance
        Logger log = Logger.getInstance();
        int before = log.getLogCount();

        Logger.getInstance().info("Shared-state test message 1");
        Logger.getInstance().error("Shared-state test message 2");
        Logger.getInstance().debug("Shared-state test message 3");

        int after = log.getLogCount();
        System.out.printf("  logCount before: %d  |  after: %d%n", before, after);

        assert_true("logCount increased by 3 across different getInstance() calls",
                after == before + 3);
    }


    private static void test4_ThreadSafety() throws InterruptedException {
        printSection("Test 4: Thread Safety (10 concurrent threads)");

        final int THREAD_COUNT = 10;
        final Logger[] instances = new Logger[THREAD_COUNT];
        Thread[] threads = new Thread[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int idx = i;
            threads[i] = new Thread(() -> {
                instances[idx] = Logger.getInstance();
                instances[idx].info("Log from thread-" + idx);
            }, "thread-" + i);
        }

      
        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        // Verify every thread got the exact same instance
        boolean allSame = true;
        for (int i = 1; i < THREAD_COUNT; i++) {
            if (instances[i] != instances[0]) {
                allSame = false;
                System.out.printf("  %sFAIL:%s thread-%d got a DIFFERENT instance!%n", RED, RESET, i);
            }
        }
        assert_true("All 10 threads received the same Logger instance", allSame);
    }

    
    private static void assert_true(String description, boolean condition) {
        if (condition) {
            System.out.printf("  %s✔ PASS:%s %s%n", GREEN, RESET, description);
            passed++;
        } else {
            System.out.printf("  %s✘ FAIL:%s %s%n", RED, RESET, description);
            failed++;
        }
    }

    private static void printBanner(String title) {
        String line = "═".repeat(60);
        System.out.println(CYAN + "\n" + line);
        System.out.printf("  %s%n", title);
        System.out.println(line + RESET + "\n");
    }

    private static void printSection(String title) {
        System.out.println(YELLOW + "\n──── " + title + " ────" + RESET);
    }

    private static void printSummary() {
        System.out.println(CYAN + "\n" + "═".repeat(60));
        System.out.printf("  RESULTS — Passed: %s%d%s  |  Failed: %s%d%s%n",
                GREEN, passed, RESET, failed > 0 ? RED : GREEN, failed, RESET);
        System.out.println(CYAN + "═".repeat(60) + RESET + "\n");
    }
}
