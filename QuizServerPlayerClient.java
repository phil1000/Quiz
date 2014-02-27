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
		
		List<Quiz> availableQuizzes = quizService.getAvailableQuizzes();
		if (availableQuizzes==null) {
			System.out.println("There are no quizzes to play, sorry");
			return null;
		}
		
		System.out.println("Here is a list of available quizzes");
		Collections.sort(availableQuizzes, new SortbyId());
		for (Quiz q : availableQuizzes) {
			System.out.println(q.getId() + ":" + q.getName());
		}
		
		System.out.println("Please enter id of quiz you want to play e.g. 1, 2 ...");
		int response = Integer.parseInt(System.console().readLine());
		
		// check whether the id provided relates to an existing quiz
		for (Quiz q : availableQuizzes) {
			if (response==q.getId()) {
				quizSelected = q;
				break;
			}
		}
		
		if (quizSelected!=null) return quizSelected;
		else {
			System.out.println("Invalid Selection");
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
			System.out.println(q.getQuestion());
			System.out.println("The options are:");
			String[][] options = q.getOptions();
			for (int i=0;i<options.length;i++) {
				for (int j=0;j<options[i].length;j++) {
					System.out.print(options[i][j] + ":");
				}
				System.out.println("");
			}
			
			System.out.println("Please enter one of: A, B, C or D");
			String response = System.console().readLine();
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
	
	class SortbyId implements Comparator<Quiz> {
		@Override
		public int compare(Quiz a, Quiz b) {
			if ( a.getId() > b.getId() ) return 1;
			if ( a.getId() < b.getId() ) return -1;
			else return 0;
		}
	}
	
	public static void main(String[] args) {		
		QuizServerPlayerClient script = new QuizServerPlayerClient();
		script.launch();
	}
}
