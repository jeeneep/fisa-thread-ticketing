package fisa.ticketing.thread;

import fisa.ticketing.manager.TicketingManager;
import fisa.ticketing.common.ThreadUtil;
import fisa.ticketing.exception.AlreadyReservedException;
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
        // 접속 대기 시뮬레이션
        ThreadUtil.sleep(random.nextInt(500));

        int pick = random.nextInt(totalSeats);
        try {
            manager.reserve(pick, userName);
        } catch (AlreadyReservedException e) {
            // 이선좌 예외 발생 시 회색 로그 출력
            System.out.println(TicketingManager.GRAY + "  [실패] " + userName + " -> " + e.getMessage() + TicketingManager.RESET);
        }
    }
}