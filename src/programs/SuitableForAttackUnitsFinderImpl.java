package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            if (row.isEmpty()) continue;

            if (isLeftArmyTarget) {
                for (int i = row.size() - 1; i >= 0; i--) {
                    if (i == row.size() - 1 || row.get(i + 1) == null) {
                        suitableUnits.add(row.get(i));
                    }
                }
            } else {
                for (int i = 0; i < row.size(); i++) {
                    if (i == 0 || row.get(i - 1) == null) {
                        suitableUnits.add(row.get(i));
                    }
                }
            }
        }

        return suitableUnits;
    }
}