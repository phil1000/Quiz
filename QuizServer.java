import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * an implementation of the quiz service.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService {

	public QuizServer() throws RemoteException {
		// nothing to initialise for this object, but need to 
		// explicitly provide a constructor that throws Remote Exception
	}
	
	@Override
	public Quiz getQuizStub(String name) {
		int id=1;
		Quiz myQuiz = new QuizImpl(id, name);
		System.out.println("returning " + myQuiz.getId() + ":" + myQuiz.getName());
		return myQuiz;
	}
	
	@Override
	public void addQuiz(Quiz myQuiz) {
		System.out.println("Quiz Number " + myQuiz.getId() + ":" + myQuiz.getName());
		
		List<Question> myQuestions = myQuiz.getQuestions();
		
		for (Question q : myQuestions) {
			System.out.println(q.getQuestion());
			System.out.println("The options are:");
			String[][] options = q.getOptions();
			for (int i=0;i<options.length;i++) {
				for (int j=0;j<options[i].length;j++) {
					System.out.print(options[i][j] + ":");
				}
				System.out.println("");
			}
			System.out.println("The answer is " + q.getAnswer());
		}
	}
}