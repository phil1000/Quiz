import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * A class to serve multiple and concurrent quiz creaters and players
 */
public interface QuizService extends Remote {
	/**
	 * returns a Quiz stub for a creator to add questions, options and answers
	 * @param name of the quiz
	 * @return Quiz instance with an allocated unique id
	 */
	 public Quiz getQuizStub(String name) throws RemoteException;
	 /**
	 * Add a new quiz
	 * @param Quiz to be added
	 */
	 public void addQuiz(Quiz newQuiz) throws RemoteException;
	 /**
	 * Close a currently open quiz
	 * @param id of the quiz to be closed
	 * @return Player who won the quiz
	 */
	 public Player closeQuiz(int id) throws RemoteException;
	 /**
	 * returns a Player stub
	 * @param name of the player
	 * @return Player instance with an allocated unique id
	 */
	 public Player getPlayerStub(String name, Quiz quizSelected) throws RemoteException;
	 /**
	 * Update an existing quiz with new player details
	 * @param Player who's just played the quiz
	 * @param Quiz just played
	 */
	 public void updateQuiz(Player myPlayer, Quiz quizSelected) throws RemoteException;
	 /**
	 * get a list of currently available quizzes
	 * @return linked list of available quizzes
	 */
	 public QuizList getAvailableQuizzes() throws RemoteException;
}
