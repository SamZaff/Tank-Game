package TankGame.PowerUp;

import TankGame.Tank;

public class QuickFire extends PowerUp {
    @Override
    public void execute(Tank tk) {
        System.out.println("QuickFireTest");
        tk.bulletDelay = 16;
        tk.bulletCount = false;
        super.execute(tk);
    }
}
