import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class QuizImpl implements Quiz, Serializable {

	private int id;
	private String name;
	private Quiz next;
	private Quiz prior;
	private List<Question> questions;
	
	public QuizImpl(int id, String name) {
		this.name=name;
		this.id=id;
		this.next=null;
		this.prior=null;
	}
	
    @Override
	public int getId() {
		return id;
	}
	
	@Override
	public Quiz getNext() {
		return next;
	}
	
	@Override
	public void setNext(Quiz next) {
		this.next=next;
	}
	
	@Override
	public Quiz getPrior() {
		return prior;
	}
	
	@Override
	public void setPrior(Quiz prior) {
		this.prior=prior;
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