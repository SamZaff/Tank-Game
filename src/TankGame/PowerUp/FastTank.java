package TankGame.PowerUp;

import TankGame.Tank;

public class FastTank extends PowerUp {

    @Override
    public void execute(Tank tk) {
        System.out.println("FastTankTest");
        tk.R = 3;
        super.execute(tk);
    }

    @Override
    public void end(Tank tk) {
        tk.R = 2;
        super.end(tk);
    }
}
