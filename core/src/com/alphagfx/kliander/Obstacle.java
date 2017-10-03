//        Copyright Kliander Alphagfx
//
//        Licensed under the Apache License, Version 2.0 (the "License");
//        you may not use this file except in compliance with the License.
//        You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//        Unless required by applicable law or agreed to in writing, software
//        distributed under the License is distributed on an "AS IS" BASIS,
//        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//        See the License for the specific language governing permissions and
//        limitations under the License.

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
