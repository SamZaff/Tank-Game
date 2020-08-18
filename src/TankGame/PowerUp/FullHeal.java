package TankGame.PowerUp;

import TankGame.Tank;

public class FullHeal extends PowerUp {

    @Override
    public void execute(Tank tk) {
        System.out.println("FullHealTest");
        tk.healthBar = 14;
        super.execute(tk);
    }
}
