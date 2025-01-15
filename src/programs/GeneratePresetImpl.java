package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int MAP_WIDTH = 3;
    private static final int MAP_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitsAvailable, int maxPoints) {
        Army armyOfComputer = new Army();
        Set<Integer> occupiedCoordinates = new HashSet<>();

        List<UnitWithEfficiency> unitsWithEfficiency = createUnitsWithEfficiency(unitsAvailable);

        sortUnitsByEfficiency(unitsWithEfficiency);

        int usedPoints = 0;
        Map<String, Integer> unitTypeCount = new HashMap<>();

        for (UnitWithEfficiency unitWithEfficiency : unitsWithEfficiency) {
            Unit unit = unitWithEfficiency.getUnit();
            String unitType = unit.getUnitType();
            int unitCost = unit.getCost();

            while (usedPoints + unitCost <= maxPoints &&
                    unitTypeCount.getOrDefault(unitType, 0) < MAX_UNITS_PER_TYPE) {

                int[] coordinates = findFreePoint(occupiedCoordinates);

                if (coordinates == null) {
                    break;
                }

                String uniqueUnitName = unitType + " " + unitTypeCount.getOrDefault(unitType, 0);
                Unit newUnit = new Unit(
                        uniqueUnitName,
                        unit.getUnitType(),
                        unit.getHealth(),
                        unit.getBaseAttack(),
                        unit.getCost(),
                        unit.getAttackType(),
                        unit.getAttackBonuses(),
                        unit.getDefenceBonuses(),
                        coordinates[0],
                        coordinates[1]
                );

                armyOfComputer.getUnits().add(newUnit);
                int key = coordinates[0] * MAP_HEIGHT + coordinates[1];
                occupiedCoordinates.add(key);
                usedPoints += unitCost;
                unitTypeCount.merge(unitType, 1, Integer::sum); // Увеличиваем счетчик типа юнита
            }
        }

        return armyOfComputer;
    }

    private int[] findFreePoint(Set<Integer> occupiedCoordinates) {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                int key = x * MAP_HEIGHT + y;
                if (!occupiedCoordinates.contains(key)) {
                    return new int[]{x, y};
                }
            }
        }
        return null;
    }

    private List<UnitWithEfficiency> createUnitsWithEfficiency(List<Unit> unitsAvailable) {
        List<UnitWithEfficiency> result = new ArrayList<>();
        for (Unit unit : unitsAvailable) {
            result.add(new UnitWithEfficiency(unit));
        }
        return result;
    }

    private void sortUnitsByEfficiency(List<UnitWithEfficiency> unitsWithEfficiency) {
        Comparator<UnitWithEfficiency> byAttackEfficiency = Comparator.comparingDouble(UnitWithEfficiency::getAttackEfficiency).reversed();
        Comparator<UnitWithEfficiency> byHealthEfficiency = Comparator.comparingDouble(UnitWithEfficiency::getHealthEfficiency).reversed();

        unitsWithEfficiency.sort(byAttackEfficiency.thenComparing(byHealthEfficiency));
    }

    private static class UnitWithEfficiency {
        private Unit unit;
        private double attackEfficiency;
        private double healthEfficiency;

        public UnitWithEfficiency(Unit unit) {
            this.unit = unit;
            this.attackEfficiency = (double) unit.getBaseAttack() / unit.getCost();
            this.healthEfficiency = (double) unit.getHealth() / unit.getCost();
        }

        public Unit getUnit() {
            return unit;
        }

        public double getAttackEfficiency() {
            return attackEfficiency;
        }

        public double getHealthEfficiency() {
            return healthEfficiency;
        }
    }
}