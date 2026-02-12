package fisa.ticketing.thread;

import fisa.ticketing.manager.TicketingManager;
import fisa.ticketing.common.ThreadUtil;
import java.util.Random;

/**
 * 사용자의 예약 시도 및 변심(취소) 과정을 시뮬레이션합니다.
 */
public class UserAction implements Runnable {
    private final TicketingManager manager;
    private final String userName;
    private final int totalSeats;
    private final Random random = new Random();

    public UserAction(TicketingManager manager, String userName, int totalSeats) {
        this.manager = manager;
        this.userName = userName;
        this.totalSeats = totalSeats;
    }

    @Override
    public void run() {
        // 1. 접속 대기 (0~0.5초)
        ThreadUtil.sleep(random.nextInt(500));
        
        boolean isSuccess = false;
        int pick = -1; // 예약에 성공한 좌석 번호를 기억하기 위한 변수
        
        // 2. 예약 성공할 때까지 무한 반복 (대기 시간 초과 시 다른 좌석 선택)
        while (!isSuccess) {
            pick = random.nextInt(totalSeats);
            isSuccess = manager.reserveWithTimeout(pick, userName);
            
            if (!isSuccess) {
                // 대기에 실패(10초 초과 등)했다면 0.1초 쉬고 다시 루프를 돕니다.
                ThreadUtil.sleep(100); 
            }
        }

        // 3. 예약 성공 후 20% 확률로 취소 발생
        if (random.nextInt(10) < 2) {
            // "아... 갈까 말까?" 고민하는 대기 시간 2초
            ThreadUtil.sleep(2000); 

            manager.cancelReservation(pick);
        }
    }
}