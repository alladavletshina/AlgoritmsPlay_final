package programs;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitsUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            for (Unit unit : row) {
                if (unit == null || !unit.isAlive()) {
                    continue; // Пропускаем мертвые или отсутствующие юниты
                }

                // Проверяем, является ли юнит крайним справа или слева в своем ряду
                if ((isLeftArmyTarget && suitAttackComputer(unit, row))
                        || (!isLeftArmyTarget && suitAttackPlayer(unit, row))) {
                    suitsUnits.add(unit); // Добавляем подходящий юнит в список
                }
            }
        }

        return suitsUnits;
    }

    private boolean suitAttackComputer(Unit unit, List<Unit> row) {
        int unitIndex = row.indexOf(unit);
        for (int i = unitIndex + 1; i < row.size(); ++i) {
            if (row.get(i) != null) {
                return false; // Найден следующий живой юнит, значит текущий не самый правый
            }
        }
        return true; // Все последующие позиции пусты, значит текущий юнит действительно самый правый
    }

    private boolean suitAttackPlayer(Unit unit, List<Unit> row) {
        int unitIndex = row.indexOf(unit);
        for (int i = unitIndex - 1; i >= 0; --i) {
            if (row.get(i) != null) {
                return false; // Найден предыдущий живой юнит, значит текущий не самый левый
            }
        }
        return true; // Все предыдущие позиции пусты, значит текущий юнит действительно самый левый
    }
}