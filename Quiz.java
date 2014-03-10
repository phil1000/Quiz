import java.util.List;
/**
 * An interface to represent a quiz containing a number of questions
 * this quiz will be stored within a linked list and so also contains 
 * getNext and setNext methods
 * <p/>
 * A Quiz has a unique ID, a name and a set of questions
 */
public interface Quiz {
    /**
     * Returns the id of the Quiz.
     *
     * @return id of the Quiz.
     */
    int getId();
	
	/**
     * Returns the address of nextQuiz.
     *
     * @return nextQuiz
     */
    Quiz getNext();
	
	/**
     * Set a pointer to next Quiz in list.
     *
     * @param nextQuiz
     */
    void setNext(Quiz nextQuiz);
	
	/**
     * Returns the address of priorQuiz.
     *
     * @return priorQuiz
     */
    Quiz getPrior();
	
	/**
     * Set a pointer to prior Quiz in list.
     *
     * @param priorQuiz
     */
    void setPrior(Quiz priorQuiz);
	
	/**
     * Returns the name of the Quiz.
     *
     * @return quizName
     */
	String getName();

    /**
     * Return the quiz questions, options and answers.
     *
     * @return questions
     */
    List<Question> getQuestions();
	
	 /**
     * Set the quiz questions and answers.
     * 
     * @param questions to be added to the quiz
     */
	void setQuestions(List<Question> questions);
	/**
     * Gets the winner of the quiz
     *
	 * @return Player who has the highest score for this quiz
     */
	Player getWinner();
	/**
     * Gets the active number of players for the passed quiz id
     *
	 * @return playerCount
     */
	int getNumberofActivePlayers();
	/**
     * reduces player count by 1
     *
     */
	void reducePlayerCount();
	/**
     * increases player count by 1
     *
     */
	void increasePlayerCount();
	/**
     * updates the current leading scorer for this quiz
     *
     * @param newWinner with the new highest score
     */
	void updateWinner(Player newWinner);
}