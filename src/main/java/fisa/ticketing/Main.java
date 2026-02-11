package fisa.ticketing;

import fisa.ticketing.manager.TicketingManager;
import fisa.ticketing.thread.UserAction;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int totalSeats = 10;
        int userCount = 30;
        TicketingManager manager = new TicketingManager(totalSeats);

        Thread[] users = new Thread[userCount];
        for (int i = 0; i < userCount; i++) {
            // UserAction 객체를 생성하여 스레드에 전달
            users[i] = new Thread(new UserAction(manager, "User-" + (i + 1), totalSeats));
        }

        System.out.println("티켓팅 시작!");
        for (Thread t : users) t.start();
        for (Thread t : users) t.join();

        manager.printFinalStatus();
        System.out.println("시스템 종료");
    }
}