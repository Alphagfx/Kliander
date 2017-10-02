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
