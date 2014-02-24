import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * an implementation of the quiz service.
 */
public interface QuizService extends Remote {
	/**
	 * Returns the same string passed as a parameter
	 * @param s a string
	 * @return the same string passed as parameter
	 */
	 public Quiz getQuizStub(String name) throws RemoteException;
	 public void addQuiz(Quiz newQuiz) throws RemoteException;
}
