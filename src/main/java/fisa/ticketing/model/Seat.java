package fisa.ticketing.model;

/**
 * 공유 자원인 좌석 정보를 담는 클래스입니다.
 */
public class Seat {
    private final int seatNumber;
    private boolean isReserved = false;
    private String user = null;

    public Seat(int seatNumber) { this.seatNumber = seatNumber; }
    
    /**
     * 좌석을 예약 상태로 변경합니다.
     */
    public void setReserved(String user) {
        this.isReserved = true;
        this.user = user;
    }

    /**
     * 좌석 예약을 취소하고 초기화합니다.
     */
    public void cancel() {
        this.isReserved = false;
        this.user = null;
    }

    public boolean isReserved() { return isReserved; }
    public int getSeatNumber() { return seatNumber; }
    public String getUser() { return user; }
}