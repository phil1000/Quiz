import java.util.List;
/**
 * An interface to represent a quiz containing up to 5 questions
 * this quiz will be stored within a linked list and so also contains 
 * getNext and setNext methods
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
     * Returns the address of nextQuiz.
     *
     * @return pointer to next Quiz.
     */
    Quiz getNext();
	
	/**
     * Set a pointer to next Quiz in list.
     *
     * @param pointer to next Quiz.
     */
    void setNext(Quiz nextQuiz);
	
	/**
     * Returns the address of priorQuiz.
     *
     * @return pointer to prior Quiz.
     */
    Quiz getPrior();
	
	/**
     * Set a pointer to prior Quiz in list.
     *
     * @param pointer to prior Quiz.
     */
    void setPrior(Quiz priorQuiz);
	
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