package fisa.ticketing.common;

/**
 * 스레드 관련 반복 코드를 줄이기 위한 유틸리티 클래스입니다.
 */
public class ThreadUtil {
	/**
     * 지정된 시간만큼 현재 스레드를 일시 정지시킵니다.
     * InterruptedException 예외 처리를 내부에서 수행하여 코드를 간결하게 만듭니다.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 비상 정지 신호 보존
        }
    }
}