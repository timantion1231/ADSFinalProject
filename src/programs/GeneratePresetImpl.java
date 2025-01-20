package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.HashSet;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        if (unitList == null || unitList.isEmpty()) {
            throw new IllegalArgumentException("Unit list cannot be null or empty.");
        }

        Army computerArmy = new Army();
        int totalPoints = 0;
        Random random = new Random();
        HashSet<String> occupiedPositions = new HashSet<>();

        unitList.sort((u1, u2) -> {
            double efficiency1 = (u1.getBaseAttack() / (double) u1.getCost()) + (u1.getHealth() / (double) u1.getCost());
            double efficiency2 = (u2.getBaseAttack() / (double) u2.getCost()) + (u2.getHealth() / (double) u2.getCost());
            return Double.compare(efficiency2, efficiency1);
        });

        for (Unit unit : unitList) {
            if (unit == null) continue;

            int availableUnits = Math.min(11, (maxPoints - totalPoints) / unit.getCost());

            for (int i = 0; i < availableUnits; i++) {
                int x, y;
                String position;

                do {
                    x = random.nextInt(3);
                    y = random.nextInt(21);
                    position = x + "," + y;
                } while (occupiedPositions.contains(position));

                occupiedPositions.add(position);

                Unit newUnit = new Unit(
                        unit.getName() + " " + (computerArmy.getUnits().size() + 1),
                        unit.getUnitType(),
                        unit.getBaseAttack(),
                        unit.getHealth(),
                        unit.getCost(),
                        unit.getAttackType(),
                        new HashMap<>(),
                        new HashMap<>(),
                        x,
                        y
                );

                computerArmy.getUnits().add(newUnit);
                totalPoints += unit.getCost();
            }

            if (totalPoints >= maxPoints) break;
        }

        computerArmy.setPoints(totalPoints);
        return computerArmy;
    }
}
