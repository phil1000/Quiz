/**
 * Each player is stored with their name and their score.
 * <p/>
 */
public interface Player {
    /**
     * Returns the Id of the player.
     *
     * @return the Id of the player.
     */
    int getId();

	 /**
     * Returns the Name of the player.
     *
     * @return name of the player.
     */
    String getName();
	/**
     * Updates the players score.
     *
     * @param score.
     */
    void updateScore(int score);
	
    /**
     * Returns the players score.
     *
     * @return score.
     */
    int getScore();
}