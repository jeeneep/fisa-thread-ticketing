package fisa.ticketing.exception;

/**
 * 이미 예약된 좌석에 접근했을 때 발생하는 사용자 정의 예외입니다.
 */
public class AlreadyReservedException extends RuntimeException {
    public AlreadyReservedException(String message) {
        super(message);
    }
}