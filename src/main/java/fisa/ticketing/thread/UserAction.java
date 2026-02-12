package fisa.ticketing.thread;

import fisa.ticketing.manager.TicketingManager;
import fisa.ticketing.common.ThreadUtil;
import java.util.Random;

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
        ThreadUtil.sleep(random.nextInt(500));
        
        boolean isSuccess = false;
        
        // 예약에 성공할 때까지 계속 시도 (단, 대기 실패 시 다른 자리를 고름)
        while (!isSuccess) {
            int pick = random.nextInt(totalSeats);
            
            // 5초 대기 기능이 포함된 예약 시도
            isSuccess = manager.reserveWithTimeout(pick, userName);
            
            if (!isSuccess) {
                // 실패했다면 아주 잠시 쉬었다가 다른 좌석으로 재시도
                ThreadUtil.sleep(100); 
            }
        }

        // 예약 성공 후 20% 확률로 취소 시나리오 (취소표 발생 유도)
        if (random.nextInt(10) < 2) {
            ThreadUtil.sleep(2000); // 2초 뒤 취소
            int mySeat = -1; 
            // 실제 구현 시 유저가 앉은 좌석 번호를 추적해야 함 (여기선 간단히 로직만 표현)
            // manager.cancelReservation(pick); 
        }
    }
}