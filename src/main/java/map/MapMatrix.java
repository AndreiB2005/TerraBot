package map;

/**
 * Represents the world map as a matrix of MapCell objects.
 * Provides methods to access individual cells and check map boundaries.
 */
public class MapMatrix {

    /** 2D array storing the map cells. */
    private final MapCell[][] matrix;

    /** Number of rows in the map. */
    private final int rows;

    /** Number of columns in the map. */
    private final int cols;

    /**
     * Constructs a MapMatrix with given dimensions in the format "rowsxcols".
     * Initializes all cells in the matrix.
     *
     * @param dimensions a string in the format "rowsxcols", e.g., "5x10"
     */
    public MapMatrix(final String dimensions) {
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

    /**
     * @return the 2D array of map cells
     */
    public MapCell[][] getMatrix() {
        return matrix;
    }

    /**
     * @return the number of rows in the map
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the number of columns in the map
     */
    public int getCols() {
        return cols;
    }

    /**
     * Retrieves the MapCell at the specified coordinates.
     *
     * @param x the column index
     * @param y the row index
     * @return the MapCell at (x, y)
     */
    public MapCell getCell(final int x, final int y)  {
        return matrix[x][y];
    }

    /**
     * Checks if the specified coordinates are within the map boundaries.
     *
     * @param posX the column index
     * @param posY the row index
     * @return true if (posX, posY) is inside the map, false otherwise
     */
    public boolean isInsideMap(final int posX, final int posY) {
        return posX >= 0 && posX < cols && posY >= 0 && posY < rows;
    }
}
