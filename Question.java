/**
 * Each question is stored with it's associated answer and up to 4 option.
 * <p/>
 * Questions have an integer id starting from 1
 */
public interface Question {
    /**
     * Returns the ID of the question.
     *
     * @return id of the question.
     */
    int getId();

    /**
     * Returns the question text.
     *
     * @return questionText.
     */
    String getQuestion();

	/**
     * Sets the question text.
     *
     * @param question the question text to be added
     */
    void setQuestion(String question);
	
    /**
     * Returns the answer value.
     *
     * @return answer for this question, which will be A, B, C or D.
     */
    String getAnswer();
	
	/**
     * Sets the answer text.
     *
     * @param answer the answer text to be added, will be A, B, C or D
     */
    void setAnswer(String answer);

	/**
     * Returns the possible answers for this question.
     *
     * @return the possible answers for this question, which will be a string matrix e.g. A, Paris.
     */
	String[][] getOptions();
	
	/**
     * Prints available options for this question.
     *
     */
	void printOptions();
	
	/**
     * Sets the answer options for this question.
     *
     * @param options a matrix containing A, option 1: B, option 2 etc
     */
    void setOptions(String[][] options);
	
	/**
     * Checks the answer provided and responds with a true or false.
     *
	 * @param suppliedAnswer is the answer to be checked
     * @return true or false, depending on whether a correct answer has been provided.
     */
	boolean checkAnswer(String suppliedAnswer);
}