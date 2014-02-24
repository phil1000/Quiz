import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.text.*;
import java.io.*;

/**
 * an implementation of the echo service.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService {

	public QuizServer() throws RemoteException {
		// nothing to initialise for this object, but need to 
		// explicitly provide a constructor that throws Remote Exception
	}
	
	@Override
	public String echo(String s) {
			System.out.println("received and about to return the string " + s); // just to show a connection has been made
			return s;
	}
	
	@Override
	public String dateTime() {
			
			System.out.println("received a request for the current time "); // just to show a connection has been made
			// return a date formatted like - Wed 2013.12.04 at 02:53:31 PM GMT
			Date rightNow = new Date( );
			SimpleDateFormat formattedDateTime = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
			return formattedDateTime.format(rightNow);
	}
	
	@Override
	public int calculator(int x, int y, CalculationTypes c) {
			int result=0;
			String resultString="";
			
			switch (c) {
				case ADD:
					resultString="add ";
					result=x+y;
					break;
				case SUBTRACT:
					resultString="subtract ";
					result=x-y;
					break;
				case MULTIPLY:
					resultString="multiply ";
					result=x*y;
					break;
				case DIVIDE:
					resultString="divide ";
					result=x/y;
					break;
				default:
					resultString="Invalid calculation type received ";
					break;
			}
			System.out.println("received a request to " + resultString + x + y); // just to show a connection has been made
			return result;
	}
	
	public String findFile(String fileName) {
		File catFile = new File("." + File.separator + fileName);
        BufferedReader in = null; // declare the buffered reader here so it can be accessed throughout the method
        String output = "";
		try {
            in = new BufferedReader(new FileReader(catFile)); // typical to use a FileReader in conjunction with BufferedReader for performance
			String line;
			while ((line = in.readLine()) != null) {
				output=output + line;
			}        
			in.close(); // close that file
		} catch (FileNotFoundException ex) {
			System.out.println("File " + fileName + " not found");
		}  catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return output;
	}
}