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
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {

        Set<String> occupiedCells = new HashSet<>();
        int[][] distanceMatrix = new int[WIDTH][HEIGHT];
        Edge[][] pathMatrix = new Edge[WIDTH][HEIGHT];
        boolean[][] isVisited = new boolean[WIDTH][HEIGHT];
        Queue<EdgeDistance> queue = new LinkedList<>();

        for (int i = 0; i < distanceMatrix.length; i++) {
            Arrays.fill(distanceMatrix[i], Integer.MAX_VALUE);
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        distanceMatrix[startX][startY] = 0;
        queue.offer(new EdgeDistance(startX, startY, 0));

        for (Unit unit : existingUnitList) {
            if (unit != targetUnit && unit.isAlive() && unit != attackUnit) {
                String coordinates = unit.getxCoordinate() + "," + unit.getyCoordinate();
                occupiedCells.add(coordinates);
            }
        }

        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (isVisited[x][y]) {
                continue;
            }
            isVisited[x][y] = true;

            if (x == targetUnit.getxCoordinate() && y == targetUnit.getyCoordinate()) {
                break;
            }

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int newX = x + dx;
                    int newY = y + dy;

                    if (isValidForPosition(newX, newY, occupiedCells, targetUnit)) {
                        int newDistance = distanceMatrix[x][y] + 1;
                        if (newDistance < distanceMatrix[newX][newY]) {
                            distanceMatrix[newX][newY] = newDistance;
                            pathMatrix[newX][newY] = new Edge(x, y);
                            queue.offer(new EdgeDistance(newX, newY, newDistance));
                        }
                    }
                }
            }
        }

        return buildPathWay(targetUnit, startX, startY, pathMatrix);
    }

    private List<Edge> buildPathWay(Unit target, int startX, int startY, Edge[][] pathGrid) {
        List<Edge> path = new ArrayList<>();
        int currentX = target.getxCoordinate();
        int currentY = target.getyCoordinate();

        while (currentX != startX || currentY != startY) {
            if (pathGrid[currentX][currentY] != null) {
                path.add(pathGrid[currentX][currentY]);
                currentX = pathGrid[currentX][currentY].getX();
                currentY = pathGrid[currentX][currentY].getY();
            } else {
                break;
            }
        }

        path.add(new Edge(startX, startY));
        Collections.reverse(path);
        return path;
    }

    private boolean isValidForPosition(int x, int y, Set<String> occupiedCells, Unit target) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false;
        }
        String cellKey = x + "," + y;
        return !occupiedCells.contains(cellKey) &&
                !(x == target.getxCoordinate() && y == target.getyCoordinate());
    }

}