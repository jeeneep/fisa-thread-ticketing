package fisa.ticketing.model;

public class Seat {
    private final int seatNumber;
    private boolean isReserved = false;
    private String user = null;

    public Seat(int seatNumber) { this.seatNumber = seatNumber; }
    
    public void setReserved(String user) {
        this.isReserved = true;
        this.user = user;
    }

    public boolean isReserved() { return isReserved; }
    public int getSeatNumber() { return seatNumber; }
    public String getUser() { return user; }
}