//  Copyright Kliander Alphagfx

package com.alphagfx.kliander;

public class Obstacle extends Subject {
    // transparency 100 is the same as air and 0 for solid blocks
    private int transparency;
    // will be used in pair with penetration
    private int hardness;

    public Obstacle() {

    }

    public Obstacle(int transparency, int hardness) {
        setTransparency(transparency);
        setHardness(hardness);
    }

    public void setTransparency(int transparency) {
        if (transparency <= 100 && transparency >= 0) {
            this.transparency = transparency;
        }
    }

    public int getTransparency() {
        return transparency;
    }

    public void setHardness(int hardness) {
        if (hardness <= 255 && hardness >= 0) {
            this.hardness = hardness;
        }
    }
}
