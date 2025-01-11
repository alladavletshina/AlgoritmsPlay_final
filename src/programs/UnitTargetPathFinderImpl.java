package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attacker, Unit target, List<Unit> otherUnits) {
        int[][] distanceGrid = new int[WIDTH][HEIGHT];
        Edge[][] pathGrid = new Edge[WIDTH][HEIGHT];
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Set<String> occupiedCells = new HashSet<>();
        Queue<EdgeDistance> queue = new LinkedList<>();

        // Инициализация начального состояния грида
        for (int[] row : distanceGrid) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        int startX = attacker.getxCoordinate();
        int startY = attacker.getyCoordinate();
        distanceGrid[startX][startY] = 0;
        queue.offer(new EdgeDistance(startX, startY, 0));

        // Добавление занятых клеток от других юнитов
        for (Unit unit : otherUnits) {
            if (unit != attacker && unit != target && unit.isAlive()) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (visited[x][y]) {
                continue;
            }
            visited[x][y] = true;

            if (x == target.getxCoordinate() && y == target.getyCoordinate()) {
                break;
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int newX = x + dx;
                    int newY = y + dy;

                    if (isValid(newX, newY, occupiedCells, target)) {
                        int newDistance = distanceGrid[x][y] + 1;
                        if (newDistance < distanceGrid[newX][newY]) {
                            distanceGrid[newX][newY] = newDistance;
                            pathGrid[newX][newY] = new Edge(x, y);
                            queue.offer(new EdgeDistance(newX, newY, newDistance));
                        }
                    }
                }
            }
        }

        return buildPath(target, startX, startY, pathGrid);
    }

    private boolean isValid(int x, int y, Set<String> occupiedCells, Unit target) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false;
        }
        String cellKey = x + "," + y;
        return !occupiedCells.contains(cellKey)
                && !(x == target.getxCoordinate() && y == target.getyCoordinate());
    }

    private List<Edge> buildPath(Unit target, int startX, int startY, Edge[][] pathGrid) {
        List<Edge> path = new ArrayList<>();
        int x = target.getxCoordinate();
        int y = target.getyCoordinate();

        while (x != startX || y != startY) {
            if (pathGrid[x][y] != null) {
                path.add(pathGrid[x][y]);
                x = pathGrid[x][y].getX();
                y = pathGrid[x][y].getY();
            } else {
                break;
            }
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);
        return path;
    }
}