package direction;

/**
 * Represents the four cardinal directions used for movement on a 2D grid.
 * Each direction stores its corresponding step on the X and Y axes.
 */
public enum Direction {

    /**
     * Moves upward on the Y axis.
     */
    UP(0, 1),

    /**
     * Moves rightward on the X axis.
     */
    RIGHT(1, 0),

    /**
     * Moves downward on the Y axis.
     */
    DOWN(0, -1),

    /**
     * Moves leftward on the X axis.
     */
    LEFT(-1, 0);

    /** The horizontal component of the direction. */
    private final int dirX;

    /** The vertical component of the direction. */
    private final int dirY;

    /**
     * Creates a direction with the specified X and Y movement values.
     *
     * @param dirX the movement along the X axis
     * @param dirY the movement along the Y axis
     */
    Direction(final int dirX, final int dirY) {
        this.dirX = dirX;
        this.dirY = dirY;
    }

    /**
     * Returns the horizontal component of the direction.
     *
     * @return the X-axis movement value
     */
    public int getDirX() {
        return dirX;
    }

    /**
     * Returns the vertical component of the direction.
     *
     * @return the Y-axis movement value
     */
    public int getDirY() {
        return dirY;
    }
}
