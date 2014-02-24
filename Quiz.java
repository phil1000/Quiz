import java.util.List;
/**
 * A class to represent a quiz containing up to 5 questions
 * <p/>
 * A Quiz has a unique ID, a name and a set of questions
 */
public interface Quiz {
    /**
     * Returns the id of the Quiz.
     *
     * @return the id of the Quiz.
     */
    int getId();
	
	/**
     * Returns the name of the Quiz.
     *
     * @return name of the Quiz.
     */
	String getName();

    /**
     * Return the quiz questions, options and answers.
     *
     * @return questions.
     */
    List<Question> getQuestions();
	
	 /**
     * Set the quiz questions and answers.
     * 
     * @param question to be added to the quiz
     */
	void setQuestions(List<Question> questions);
}