package org.checkinindza.Model;

public class Player {
    private int money;
    private int points;

    public Player(int money, int points) {
        this.money = money;
        this.points = points;
    }

    public int getMoney() {
        return this.money;
    }

    public int getPoints() {
        return this.points;
    }
}