package com.alphagfx.kliander;

public class Creature extends Subject implements IMovable {

    public void run() {
        System.out.println(this.getHealth());
        this.setHealth(50);
        System.out.println(getHealth());
    }

    @Override
    public void move() {

    }

    public static void main(String[] args) {
        Creature c = new Creature();
        c.run();
    }
}
