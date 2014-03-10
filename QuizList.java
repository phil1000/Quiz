/**
 * An interface to represent a list of quizzes
 * <p/>
 */
public interface QuizList {
	/**
     * Add a Quiz to the list
     *
     * @param newQuiz to be added.
     */
	void add(Quiz newQuiz);
	/**
     * Delete a Quiz from the list
     *
     * @param id of the Quiz to be deleted.
     */
	void delete(int id);
	
	/**
     * Returns a Quiz for the given Id.
     *
	 * @param id of the Quiz to be selected
     * @return Quiz selected.
     */
	Quiz get(int id);
	/**
     * Returns the current size of the list.
     *
     * @return size of the list.
     */
	int getSize();
	/**
     * print the list.
     */
	void print();
}