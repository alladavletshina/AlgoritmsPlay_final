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

            int start = isLeftArmyTarget ? 0 : row.size() - 1;
            int end = isLeftArmyTarget ? row.size() : -1;
            int step = isLeftArmyTarget ? 1 : -1;

            for (int i = start; i != end; i += step) {
                Unit unit = row.get(i);

                if (unit == null || !unit.isAlive()) {
                    continue;
                }

                int nextIndex = i + step;
                if (nextIndex < 0 || nextIndex >= row.size() || row.get(nextIndex) == null) {
                    suitableUnits.add(unit);
                }
            }
        }

        return suitableUnits;
    }
}
