/**
 * Each player is stored with their name and their score.
 * <p/>
 */
public interface Player {
    /**
     * Returns the Name of the player.
     *
     * @return the Name of the player.
     */
    int getId();

	 /**
     * Returns the Id of the player.
     *
     * @return Id of the player.
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