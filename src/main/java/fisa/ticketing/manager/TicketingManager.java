package fisa.ticketing.manager;

import fisa.ticketing.model.Seat;
import fisa.ticketing.exception.AlreadyReservedException;
import java.util.ArrayList;
import java.util.List;

public class TicketingManager {
    private final List<Seat> seatList = new ArrayList<>();
    
    // 포도알 색상 (Purple) / 이선좌 색상 (Gray)
    public static final String PURPLE = "\u001B[35m"; 
    public static final String GRAY = "\u001B[90m";   
    public static final String RESET = "\u001B[0m";

    public TicketingManager(int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            seatList.add(new Seat(i));
        }
        System.out.println("시스템: " + totalSeats + "개의 포도알이 생성되었습니다. 🍇");
    }

    public synchronized boolean reserve(int seatIndex, String userName) {
        Seat targetSeat = seatList.get(seatIndex);

        if (!targetSeat.isReserved()) {
            // 예약을 진행하는 찰나의 시간 (0.05초)
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            
            targetSeat.setReserved(userName);
            System.out.println(String.format("%s[성공] %s 님이 %d번 좌석을 겟!%s", PURPLE, userName, targetSeat.getSeatNumber(), RESET));
            printSeatMap(); // 성공 시 포도알 현황 출력
            return true;
        } else {
            // 이미 예약된 경우 예외를 던져서 UserAction이 잡게 함
            throw new AlreadyReservedException(String.format("%d번은 이선좌(이미 선택된 좌석)입니다.", targetSeat.getSeatNumber()));
        }
    }

    public synchronized void printSeatMap() {
        System.out.println("\n========= 현재 포도알 상황 =========");
        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i).isReserved()) {
                System.out.print(GRAY + "[X] " + RESET);
            } else {
                System.out.print(PURPLE + "[●] " + RESET);
            }
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("==================================\n");
    }

    public void printFinalStatus() {
        long count = seatList.stream().filter(Seat::isReserved).count();
        System.out.println("\n[최종 결과] 남은 포도알을 모두 수확했습니다. 총 예약 수: " + count);
    }
}