**Алгоритмическая сложность метода generate будет O(nlogn).**

    1.1. Создание списка unitsWithEfficiency равно O(n)
    
    private List<UnitWithEfficiency> createUnitsWithEfficiency(List<Unit> unitsAvailable) {
        List<UnitWithEfficiency> result = new ArrayList<>();
        for (Unit unit : unitsAvailable) {
            result.add(new UnitWithEfficiency(unit));
        }
        return result;
    }
    
    1.2. Сортировка списка unitsWithEfficiency равно O(nLogn)
    
    private void sortUnitsByEfficiency(List<UnitWithEfficiency> unitsWithEfficiency) {
        Comparator<UnitWithEfficiency> byAttackEfficiency = Comparator.comparingDouble(UnitWithEfficiency::getAttackEfficiency).reversed();
        Comparator<UnitWithEfficiency> byHealthEfficiency = Comparator.comparingDouble(UnitWithEfficiency::getHealthEfficiency).reversed();
        
        unitsWithEfficiency.sort(byAttackEfficiency.thenComparing(byHealthEfficiency));
    }
    
    1.3. Основной цикл добавления юнитов в армию равно O(n * C1)
    
    for (UnitWithEfficiency unitWithEfficiency : unitsWithEfficiency) {
        ...
    }

Итоговая оценка сложности: O(n)+O(nlogn)+O(n⋅C1) = O(nlogn)


**Алгоритмическая сложность метода simulate будет O(min(n,m)⋅(n+m)log(n+m)).**

2.1. Инициализация списков равно O(n + m)

List<Unit> playerUnits = new ArrayList<>(armyOfPlayer.getUnits());
List<Unit> computerUnits = new ArrayList<>(armyOfComputer.getUnits());

2.2. Метод unitAttack равно O(klogK + l) - временная сложность одного раунда равна O((n + m)log(n + m))

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

Итоговая оценка сложности: O(min(n,m)⋅(n+m)log(n+m))

Сложность O(min(n,m)⋅(n+m)log(n+m)) обычно лучше, чем O(n2Logn), особенно когда n и m сильно различаются.


**Алгоритмическая сложность метода getTargetPath будет O(W x H).**

3.1. Создание структур данных (HashSet).
Заполнение матриц и инициализация начальных значений.
Сложность - O (W * H) 

3.2. Заполнение множества занятых клеток:
Проход по списку юнитов для добавления координат в множество.
Сложность -O (U)

3.3. Основной цикл BFS:
Обработка каждого узла в очереди до тех пор, пока не будет достигнут целевой юнит.
Сложность - O (W * H)

3.4. Построение пути:
Реконструкция пути от целевого юнита к исходному через матрицу путей.
Сложность - O (W * H)

Итоговая оценка сложности: O(W * H + U + W * H + W * H) 
