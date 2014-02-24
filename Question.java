/**
 * Each question is stored with it's associated answer and up to 4 option.
 * <p/>
 * Questions have a number between 1 and 5
 */
public interface Question {
    /**
     * Returns the ID of the question.
     *
     * @return the ID of the question.
     */
    int getId();

    /**
     * Returns the question text.
     *
     * @return question text.
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
     * Sets the answer options for this question.
     *
     * @param options a matrix containing A, option 1: B, option 2 etc
     */
    void setOptions(String[][] options);
	
	/**
     * Checks the answer provided and responds with a true or false.
     *
     * @return true or false, depending on whether a correct answer has been provided.
     */
	boolean checkAnswer(String suppliedAnswer);
}