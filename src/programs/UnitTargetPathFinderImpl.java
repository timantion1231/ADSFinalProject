package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        if (attackUnit == null || targetUnit == null || existingUnitList == null) {
            throw new IllegalArgumentException("Units and existingUnitList cannot be null.");
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        boolean[][] obstacles = new boolean[WIDTH][HEIGHT];
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                obstacles[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        return aStarSearch(startX, startY, targetX, targetY, obstacles);
    }

    private List<Edge> aStarSearch(int startX, int startY, int targetX, int targetY, boolean[][] obstacles) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));
        Node[][] nodes = new Node[WIDTH][HEIGHT];

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, targetX, targetY), null);
        openSet.add(startNode);
        nodes[startX][startY] = startNode;

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == targetX && current.y == targetY) {
                return buildPath(current);
            }

            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int newX = current.x + direction[0];
                int newY = current.y + direction[1];

                if (isValidMove(newX, newY, obstacles)) {
                    int newGCost = current.gCost + 1;
                    Node neighbor = nodes[newX][newY];

                    if (neighbor == null) {
                        neighbor = new Node(newX, newY, newGCost, heuristic(newX, newY, targetX, targetY), current);
                        nodes[newX][newY] = neighbor;
                        openSet.add(neighbor);
                    } else if (newGCost < neighbor.gCost) {
                        neighbor.gCost = newGCost;
                        neighbor.fCost = newGCost + neighbor.hCost;
                        neighbor.parent = current;

                        openSet.remove(neighbor);
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    private boolean isValidMove(int x, int y, boolean[][] obstacles) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT && !obstacles[x][y];
    }

    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private List<Edge> buildPath(Node node) {
        List<Edge> path = new ArrayList<>();
        while (node != null) {
            path.add(new Edge(node.x, node.y));
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        int x, y;
        int gCost, hCost, fCost;
        Node parent;

        Node(int x, int y, int gCost, int hCost, Node parent) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
            this.parent = parent;
        }
    }
}