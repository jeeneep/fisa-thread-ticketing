package fisa.ticketing;

import fisa.ticketing.manager.TicketingManager;
import fisa.ticketing.thread.UserAction;

/**
 * [디벨롭 버전] 제한 시간 대기 및 재시도 로직을 포함한 티켓팅 시뮬레이션 메인 클래스입니다.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 1. 시뮬레이션 환경 설정
        int totalSeats = 30; 
        int userCount = 30; 
        
        TicketingManager manager = new TicketingManager(totalSeats);

        // 2. 사용자 스레드 준비
        Thread[] users = new Thread[userCount];
        for (int i = 0; i < userCount; i++) {
            // UserAction 내부의 while 루프 덕분에 모든 사용자가 결국 좌석을 얻거나 
            // 로직에 따라 행동하게 됩니다.
            users[i] = new Thread(new UserAction(manager, "User-" + (i + 1), totalSeats));
        }

        System.out.println("🔥 [전쟁 선포] 포도알 수확 작전이 시작되었습니다! 🔥");
        System.out.println("정보: 좌석 " + totalSeats + "개 / 대기 인원 " + userCount + "명");
        System.out.println("규칙: 5초 대기 후 실패 시 다른 좌석으로 이동합니다.\n");

        // 3. 스레드 가동
        for (Thread t : users) {
            t.start();
        }
        
        // 4. 모든 사용자의 활동(예약 및 취소 시뮬레이션)이 완전히 끝날 때까지 대기
        // join()이 없으면 메인 스레드가 먼저 종료되어 결과를 확인할 수 없습니다.
        for (Thread t : users) {
            t.join();
        }

        // 5. 최종 리포트 출력
        System.out.println("\n" + TicketingManager.YELLOW + "========================================" + TicketingManager.RESET);
        manager.printFinalStatus();
        System.out.println("상태: 모든 스레드가 작업을 완료하고 안전하게 종료되었습니다.");
        System.out.println(TicketingManager.YELLOW + "========================================" + TicketingManager.RESET);
        System.out.println("시스템 종료");
    }
}