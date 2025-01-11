package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army armyOfPlayer, Army armyOfComputer) throws InterruptedException {
        List<Unit> playerUnits = new ArrayList<>(armyOfPlayer.getUnits());
        List<Unit> computerUnits = new ArrayList<>(armyOfComputer.getUnits());

        int roundNumber = 1;

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            unitAttack(playerUnits, computerUnits);
            unitAttack(computerUnits, playerUnits);

            System.out.println("Round " + roundNumber + " is over!");
            System.out.println("Player army has " + playerUnits.size() + " units");
            System.out.println("Computer army has " + computerUnits.size() + " units");
            System.out.println();

            roundNumber++;
        }

        System.out.println("Battle is over!");
    }

    private void unitAttack(List<Unit> attackers, List<Unit> defenders) throws InterruptedException {

        Collections.sort(attackers, new Comparator<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                return Integer.compare(o2.getBaseAttack(), o1.getBaseAttack());
            }
        });

        for (Unit attacker : attackers) {
            if (attacker.isAlive()) {
                Unit target = attacker.getProgram().attack();
                if (target != null && target.isAlive()) {
                    this.printBattleLog.printBattleLog(attacker, target);
                }
            }
        }

        defenders.removeIf(unit -> !unit.isAlive());

    }
}