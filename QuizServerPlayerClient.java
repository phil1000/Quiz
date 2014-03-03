import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

public class QuizServerPlayerClient {
	
	public void launch() {
		
		// 1. if no security manager running, launch one
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			// 2. find a reference to remote server object
			Remote myService = Naming.lookup("//127.0.0.1:1099/quizServer");
			QuizService quizService = (QuizService) myService; // in order to use methods, need to downcast them to the right type
			
			//3. Now call the remote methods
			Quiz quizSelected = selectQuiz(quizService); // select a quiz to play
			if (quizSelected != null) {
				playQuiz(quizService, quizSelected); // play the quiz
			}
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public Quiz selectQuiz(QuizService quizService) throws RemoteException {

		Quiz quizSelected=null;
		
		QuizList availableQuizzes = quizService.getAvailableQuizzes();
		if (availableQuizzes==null) {
			System.out.println("No quizzes found");
			return null;
		}
		
		System.out.println("The available quizzes are as follows, please enter id of the quiz you want to play");
		availableQuizzes.print();

		int response = Integer.parseInt(System.console().readLine());
		
		Quiz myQuiz=availableQuizzes.get(response);
		if (myQuiz!=null) {
			return myQuiz;
		} else {
			System.out.println("Sorry but you have entered an invalid quiz id");
			return null;
		}
	}
	
	public void playQuiz(QuizService quizService, Quiz quizSelected) throws RemoteException {
		// need to work out how to get a player Id
		int score=0;
		
		System.out.println("What's your name?");
		String name = System.console().readLine();
		Player myPlayer = new PlayerImpl(1, name);

		List<Question> myQuestions = quizSelected.getQuestions();

		for (Question q : myQuestions) {
			System.out.println("Question" + q.getId() + ". " + q.getQuestion());
			System.out.println("The options are:");
			q.printOptions();
			
			System.out.println("Please enter one of: A, B, C or D");
			String response = System.console().readLine();
			response = response.substring(0, 1); // just take first character
			if ( q.checkAnswer(response.toUpperCase()) ) {
				System.out.println("Correct");
				score++;
			} else {
				System.out.println("Incorrect. The right answer was " + q.getAnswer());
			}
		}
		
		System.out.println("Quiz is completed and your final score is " + score);	
		myPlayer.updateScore(score);
		//quizService.updateQuiz(myPlayer, quizSelected);
	}
	
	public static void main(String[] args) {		
		QuizServerPlayerClient script = new QuizServerPlayerClient();
		script.launch();
	}
}
