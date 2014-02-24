import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * an implementation of the echo service.
 */
public interface QuizService extends Remote {
	/**
	 * Returns the same string passed as a parameter
	 * @param s a string
	 * @return the same string passed as parameter
	 */
	 public String echo(String s) throws RemoteException; // do i actually need public?
	 public String dateTime() throws RemoteException;
	 public int calculator(int x, int y, CalculationTypes c) throws RemoteException;
	 public String findFile(String fileName) throws RemoteException;
}
