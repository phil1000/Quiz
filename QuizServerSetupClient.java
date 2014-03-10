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

/**
 * This program launches the quiz setup client
 * it takes no input parameters
 * <p/>
 */
 
public class QuizServerSetupClient {

	private static final int MAX_QUESTION_COUNT=2; // could make this a variable set by user but decided against
	private static final int MAX_OPTION_COUNT=4;
	
	public void launch() {
	
		// first of all check what they want to do i.e.
		// option 1, setup a new quiz, option 2 close an existing quiz or just finish if neither 1 or 2 is chosen as it would be an invalid option
		
		int response = 0;
		
		System.out.println("Please enter either 1 to ADD a new Quiz or 2 to CLOSE an existing quiz?");
		
		try {
			response = Integer.parseInt(System.console().readLine());
			if ( (response!=1) && (response!=2) ) {
				System.out.println("Invalid response, you need to choose either 1 or 2, bye for now");
				return;
			}
		} catch (NumberFormatException nfe) {
			// but it throws an exception if the String doesn't look
			// like any integer it recognizes
			System.out.println("That's not a number! Try again.");
			return;
		}
		
		// 1. if no security manager running, launch one
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			// 2. find a reference to remote server object
			Remote myService = Naming.lookup("//127.0.0.1:1099/quizServer");
			QuizService quizService = (QuizService) myService; // in order to use methods, need to downcast them to the right type
			
			//3. Now call the remote methods
			if (response==1) {
				addNewQuiz(quizService); // does all the work adding a quiz
			} else {
				closeQuiz(quizService); // does all the work closing a quiz
			}
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public void addNewQuiz(QuizService quizService) throws RemoteException {
		System.out.println("What is the name of the quiz?");
		String name = System.console().readLine();
		Quiz myQuiz = quizService.getQuizStub(name); // this gets a unique quiz Id and a quiz instance that we populate and then return to server
		System.out.println("I got returned a quiz with id=" + myQuiz.getId() + " and name=" + myQuiz.getName());
			
		// Now get the questions answers and options 
		List<Question> myQuestions = new ArrayList<Question>();
		getQuestions(myQuestions);
		//Add questions to Quiz and send to QuizServer
		myQuiz.setQuestions(myQuestions);
		quizService.addQuiz(myQuiz);
	}
	
	public void closeQuiz(QuizService quizService) throws RemoteException {

		boolean validResponse=false;
		int response=0;
		
		QuizList availableQuizzes = quizService.getAvailableQuizzes();
		if (availableQuizzes==null) {
			System.out.println("No quizzes found");
			return;
		}
		
		System.out.println("The available quizzes are as follows, please enter id of quiz to close e.g. 1, 2 etc");
		availableQuizzes.print();
		
		System.out.println("Please enter id of quiz to close e.g. 1, 2 etc");
		
		while (!validResponse) {
			try {
				response = Integer.parseInt(System.console().readLine());
				validResponse=true;
			} catch (NumberFormatException nfe) {
				// but it throws an exception if the String doesn't look
				// like any integer it recognizes
				System.out.println("That's not a number! Try again.");
			}
		}
		
		if (availableQuizzes.get(response)!=null) {
			Player winner = quizService.closeQuiz(response);
			if (winner!=null) {
				System.out.println("The winner was " + winner.getName());
				System.out.println(" and their score was " + winner.getScore());
			} else {
				System.out.println("There were no players for that game or quiz has already been deleted");
			}
		} else {
			System.out.println("Sorry but you have entered an invalid quiz id");
		}

	}
	
	public void getQuestions(List<Question> myQuestions) {
		 
		int questionCount = 1;
		int maxQuestions = 0;
		boolean validQuestionCount=false;
		
		String answerOption;
		// I have limited each question to having 4 possible answers, prefixed by A, B, C or D
		String[][] options = { {"A",""}, {"B",""}, {"C",""}, {"D",""} }; 
					
		System.out.println("In this quiz you enter a question, four possible answers prefixed by A->D ");
		System.out.println("and then the correct answer, which should be either A, B, C or D");
		
		System.out.println("How many questions do you want in your quiz?");
		
		while (!validQuestionCount) {
			try {
				maxQuestions = Integer.parseInt(System.console().readLine());
				validQuestionCount=true;
			} catch (NumberFormatException nfe) {
				// but it throws an exception if the String doesn't look
				// like any integer it recognizes
				System.out.println("That's not a number! Try again.");
			}
		}
			
		while (questionCount<=maxQuestions) {

			System.out.println("");
			System.out.println("Please enter Question:" + questionCount + " e.g. what is the capital of Slovakia");
			String questionText = System.console().readLine();
			
			System.out.println("Now enter the potential answer options");
			
			for (int i=0; i<MAX_OPTION_COUNT;i++) {
				System.out.println("Please enter answer option " + options[i][0] + " e.g. London");
				
				answerOption = System.console().readLine();
				options[i][1] = answerOption.toUpperCase();
			}
			
			System.out.println("Please enter the correct answer for question:" + questionText + " i.e. enter C if Bratislava was option C");
			String correctAnswer = getAndFormatValidAnswer(System.console().readLine());
			Question newQuestion = new QuestionImpl(questionCount, questionText, correctAnswer, options);
			myQuestions.add(newQuestion);
			
			questionCount++;
		}
	}
	
	private String getAndFormatValidAnswer(String correctAnswer) {

			boolean validAnswer=false;
			while (validAnswer==false) {
				correctAnswer = correctAnswer.substring(0, 1); // just take first character and check if it is A->D
				correctAnswer = correctAnswer.toUpperCase(); // upper case it
				if ( (correctAnswer.equals("A")) 	|| 
						(correctAnswer.equals("B"))		||
							(correctAnswer.equals("C"))	||
								(correctAnswer.equals("D")) ) {
					validAnswer=true;
					break;
				}
				System.out.println("Invalid answer, please enter A->D");
				correctAnswer=System.console().readLine();
			}
			return correctAnswer;
	}
	
	public static void main(String[] args) {		
		QuizServerSetupClient script = new QuizServerSetupClient();
		script.launch();
	}
}
