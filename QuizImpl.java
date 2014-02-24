import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class QuizImpl implements Quiz, Serializable {

	private int id;
	private String name;
	private List<Question> questions;
	
	public QuizImpl(int id, String name) {
		this.name=name;
		this.id=id;
	}
	
    @Override
	public int getId() {
		return id;
	}
	
    @Override
	public String getName() {
		return name;
	}
	
	@Override
    public List<Question> getQuestions() {
		return questions;
	}
	
	@Override
    public void setQuestions(List<Question> questions) {
		this.questions=questions;
	}
}