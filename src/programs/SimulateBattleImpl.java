package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {

    private final PrintBattleLog printBattleLog;

    public SimulateBattleImpl() {
        this.printBattleLog = new PrintBattleLog() {
            @Override
            public void printBattleLog(Unit attackingUnit, Unit target) {
                System.out.println(attackingUnit.getName() + " attacks " + target.getName());
            }
        };
    }

    public SimulateBattleImpl(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        if (playerArmy == null || computerArmy == null) {
            throw new IllegalArgumentException("Armies cannot be null.");
        }

        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            playerUnits.sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));
            computerUnits.sort((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()));

            for (Unit playerUnit : new ArrayList<>(playerUnits)) {
                if (playerUnit == null || !playerUnit.isAlive()) continue;

                Unit target = playerUnit.getProgram().attack();
                if (target != null) {
                    printBattleLog.printBattleLog(playerUnit, target);
                    target.getHealth();
                    if (!target.isAlive()) {
                        computerUnits.remove(target);
                    }
                }
            }

            for (Unit computerUnit : new ArrayList<>(computerUnits)) {
                if (computerUnit == null || !computerUnit.isAlive()) continue;

                Unit target = computerUnit.getProgram().attack();
                if (target != null) {
                    printBattleLog.printBattleLog(computerUnit, target);
                    target.getHealth();
                    if (!target.isAlive()) {
                        playerUnits.remove(target);
                    }
                }
            }

            playerUnits.removeIf(unit -> unit == null || !unit.isAlive());
            computerUnits.removeIf(unit -> unit == null || !unit.isAlive());
        }

        if (playerUnits.isEmpty()) {
            System.out.println("Computer army wins!");
        } else {
            System.out.println("Player army wins!");
        }
    }
}
