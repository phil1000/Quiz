import java.io.Serializable;

public class QuestionImpl implements Question, Serializable {
	
	private int id;
	private String question;
	private String answer;
	private String[][] options = new String[4][2]; // I can have a maximum of four options A->D, each with textual description
	
	public QuestionImpl(int id) {
		this.id = id;
	}
	
	public QuestionImpl(int id, String question, String answer, String[][] options) {
		this(id);
		this.setQuestion(question);
		this.setAnswer(answer);
		this.setOptions(options);
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public String getQuestion() {
		return this.question;
	}
	
	@Override
	public void setQuestion(String question) {
		this.question = question;
	}
	
	@Override
    public String getAnswer() {
		return this.answer;
	}
	
	@Override
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	@Override
	public String[][] getOptions() {
		return this.options;
	}

	@Override
	public void setOptions(String[][] options) {
		for (int i=0;i<options.length;i++) {
			for (int j=0;j<options[i].length;j++) {
			this.options[i][j]=options[i][j];
			}
		}
	}
	
	@Override
	public boolean checkAnswer(String suppliedAnswer) {
		if (suppliedAnswer.equals(this.answer)) return true;
		else return false;
	}
}