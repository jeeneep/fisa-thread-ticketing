package fisa.ticketing.common;

public class ThreadUtil {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 유지
            System.err.println("스레드 슬립 중 오류 발생");
        }
    }
}