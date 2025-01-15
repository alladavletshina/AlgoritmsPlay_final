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

Сложность O(min(n,m)⋅(n+m)log(n+m)) обычно лучше, чем O(n2Logn), особенно когда n и m сильно различаются.

