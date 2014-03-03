import java.util.List;
import java.util.ArrayList;

public class TestHarness {

	public void launch() {
		//testQuestionSetup();
		//Quiz myQuiz = testQuizSetup(1, "Capital Cities");
		Quiz myQuiz1 = testQuizSetup(1, "AAAA");
		Quiz myQuiz2 = testQuizSetup(2, "BBBB");
		Quiz myQuiz3 = testQuizSetup(3, "CCCC");
		Quiz myQuiz4 = testQuizSetup(4, "DDDD");
		Quiz myQuiz5 = testQuizSetup(5, "EEEE");
		
		QuizList myList = new QuizLinkedList(myQuiz2);
		myList.add(myQuiz1);
		myList.add(myQuiz3);
		myList.add(myQuiz5);
		myList.add(myQuiz4);
		
		//System.out.println("getting quiz 1" + myList.getQuiz(1).getName());
		//System.out.println("getting quiz 3" + myList.getQuiz(3).getName());
		//System.out.println("getting quiz 5" + myList.getQuiz(5).getName());
		
		System.out.println("printing a list before deletes, it has size="+myList.getSize());
		myList.print();
		myList.delete(1); // need to test deletion of 1, 2, 3, 4, 5
		System.out.println("printing a list after deleting 1, it has size="+myList.getSize());
		myList.print();
		myList.add(myQuiz1);
		System.out.println("printing a list after adding 5 back again, it has size="+myList.getSize());
		myList.print();
		printQuiz(myList.get(1));
	}
	
	public void printQuiz(Quiz myQuiz) {
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
	
	public Quiz testQuizSetup(int id, String name) {

		Quiz myQuiz = new QuizImpl(id, name);
		//System.out.println("Quiz id=" + myQuiz.getId() + " Name=" + myQuiz.getName());
		
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
		
		return myQuiz;
	}

	
	public void testQuestionSetup() {
		int id=1;
		String question = "What is capital of France";
		String answer = "B";
		String[][] options = { {"A","London"}, {"B","Paris"}, {"C", "Berlin"}, {"D","Cardiff"}  };
		String[][] optionsAvailable;
		
		Question myQuestion = new QuestionImpl(id, question, answer, options);
		System.out.print("Question: " + myQuestion.getQuestion());
		System.out.print(" Answer: " + myQuestion.getAnswer());
		System.out.println("");
		optionsAvailable = myQuestion.getOptions();
		for (int i=0;i<optionsAvailable.length;i++) {
			for (int j=0;j<optionsAvailable[i].length;j++) {
				System.out.print(optionsAvailable[i][j] + ":");
			}
			System.out.println("");
		}
		
		if (myQuestion.checkAnswer("B")) System.out.println("B is correct");
		
		if (myQuestion.checkAnswer("C")) System.out.println("C is correct");
		else System.out.println("C is not correct");
		
	}
	
	public static void main(String[] args) {
		TestHarness script = new TestHarness();
		script.launch();
	}
}