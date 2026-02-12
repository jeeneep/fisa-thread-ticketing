package fisa.ticketing.manager;

import fisa.ticketing.model.Seat;
import java.util.ArrayList;
import java.util.List;

/**
 * 제한 시간 대기(3초) 및 최종 결과 리포트 기능을 포함한 매니저 클래스입니다.
 */
public class TicketingManager {
    private final List<Seat> seatList = new ArrayList<>();
    
    // 시각화용 ANSI 색상 코드
    public static final String PURPLE = "\u001B[35m"; 
    public static final String GRAY = "\u001B[90m";   
    public static final String YELLOW = "\u001B[33m"; 
    public static final String RED = "\u001B[31m";   
    public static final String RESET = "\u001B[0m";

    public TicketingManager(int totalSeats) {
        for (int i = 1; i <= totalSeats; i++) {
            seatList.add(new Seat(i));
        }
    }

    /**
     * 좌석 예약을 시도하며, 실패 시 최대 3초간 취소표를 기다립니다.
     */
    public synchronized boolean reserveWithTimeout(int seatIndex, String userName) {
        Seat targetSeat = seatList.get(seatIndex);

        if (!targetSeat.isReserved()) {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            targetSeat.setReserved(userName);
            System.out.println(String.format("%s[성공] %s 님이 %d번 좌석을 차지했습니다!%s", PURPLE, userName, targetSeat.getSeatNumber(), RESET));
            printSeatMap();
            return true;
        } 

        try {
            System.out.println(String.format("%s  [대기] %s 님, %d번 좌석 3초 대기 시작...%s", YELLOW, userName, targetSeat.getSeatNumber(), RESET));
            
            // 3초 동안만 락을 풀고 대기실(Wait Set)에서 알림을 기다림
            wait(3000); 

            if (!targetSeat.isReserved()) {
                targetSeat.setReserved(userName);
                System.out.println(String.format("%s[성공] %s 님이 대기 끝에 %d번 좌석을 겟!%s", PURPLE, userName, targetSeat.getSeatNumber(), RESET));
                printSeatMap();
                return true;
            } else {
                System.out.println(String.format("%s  [실패] %s 님, %d번 좌석 대기 시간 초과(3초). 다른 자리를 찾습니다.%s", RED, userName, targetSeat.getSeatNumber(), RESET));
                return false; 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /** 예약을 취소하고 대기 중인 스레드들을 깨움 */
    public synchronized void cancelReservation(int seatIndex) {
        Seat seat = seatList.get(seatIndex);
        if (seat.isReserved()) {
            String prevUser = seat.getUser();
            seat.cancel();
            System.out.println(String.format("%s\n[취소] %s 님이 %d번 좌석을 포기했습니다. (다시 포도알 생성!)%s", YELLOW, prevUser, seat.getSeatNumber(), RESET));
            notifyAll(); 
        }
    }

    /** 현재 전체 좌석 상황 시각화 */
    public synchronized void printSeatMap() {
        System.out.println("\n========= 현재 포도알 상황 =========");
        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i).isReserved()) System.out.print(GRAY + "[X] " + RESET);
            else System.out.print(PURPLE + "[●] " + RESET);
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println("==================================\n");
    }

    /**
     * 모든 티켓팅 작업이 끝난 후 최종 예약 현황을 요약 출력합니다.
     */
    public void printFinalStatus() {
        long reservedCount = seatList.stream().filter(Seat::isReserved).count();
        int totalSeats = seatList.size();

        System.out.println("\n" + YELLOW + "━━━━━━━ 티켓팅 최종 리포트 ━━━━━━━" + RESET);
        System.out.println(String.format("총 좌석 수: %d", totalSeats));
        System.out.println(String.format("예약된 좌석: %d", reservedCount));
        System.out.println(String.format("잔여 좌석: %d", totalSeats - reservedCount));
        System.out.println(String.format("매진 여부: %s", (reservedCount == totalSeats) ? "SOLD OUT! 🍇" : "잔여석 있음"));
        
        System.out.println("\n[상세 좌석 정보]");
        for (Seat seat : seatList) {
            String status = seat.isReserved() ? GRAY + "예약됨 (" + seat.getUser() + ")" : PURPLE + "빈 좌석";
            System.out.println(String.format("좌석 %d: %s%s", seat.getSeatNumber(), status, RESET));
        }
    }
}