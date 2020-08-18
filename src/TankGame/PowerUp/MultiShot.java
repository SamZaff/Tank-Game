package TankGame.PowerUp;

import TankGame.Tank;

public class MultiShot extends PowerUp{

    @Override
    public void execute(Tank tk) {
        tk.bulletCount = true;
        tk.bulletDelay = 33;
        System.out.println("MultiShotTest");
        super.execute(tk);
    }
}
