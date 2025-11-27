package worldMap;

public class MapMatrix {
    private final MapCell[][] matrix;
    private final int rows;
    private final int cols;

    public MapMatrix(String dimensions) {
        String[] coordinates = dimensions.split("x");
        rows = Integer.parseInt(coordinates[0]);
        cols = Integer.parseInt(coordinates[1]);
        matrix = new MapCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = new MapCell();
            }
        }
    }

    public MapCell[][] getMatrix() {
        return matrix;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public MapCell getCell(int x, int y)  {
        return matrix[x][y];
    }
}
