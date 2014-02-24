import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.*;
import java.rmi.Naming;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.List;
import java.util.ArrayList;

public class QuizServerSetupClient {

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

			System.out.println("What is the name of the quiz?");
			String name = System.console().readLine();
			Quiz myQuiz = quizService.getQuizStub(name);
			System.out.println("I got returned a quiz with id=" + myQuiz.getId() + " and name=" + myQuiz.getName());
			
			// Now get the questions answers and options ... need to strip out into new method
			List<Question> myQuestions = new ArrayList<Question>();
			String[][] options = { {"A","London"}, {"B","Paris"}, {"C", "Berlin"}, {"D","Cardiff"}  };
			Question newQuestion = new QuestionImpl(1, "What is the capital of France","B", options);
			myQuestions.add(newQuestion);

			newQuestion = new QuestionImpl(2, "What is the capital of England","A", options);
			myQuestions.add(newQuestion);

			newQuestion = new QuestionImpl(3, "What is the capital of Germany","C", options);
			myQuestions.add(newQuestion);
		
			newQuestion = new QuestionImpl(4, "What is the capital of Wales","D", options);
			myQuestions.add(newQuestion);
		
			myQuiz.setQuestions(myQuestions);
			quizService.addQuiz(myQuiz);
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {		
		QuizServerSetupClient script = new QuizServerSetupClient();
		script.launch();
	}
}
