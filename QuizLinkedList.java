import java.io.Serializable;
/**
 * A doubly linked and sorted list of Quizzes. Implements QuizList interface
 * <p/>
 */

public class QuizLinkedList implements QuizList, Serializable {
	
	private Quiz head;
	private Quiz tail;
	private int size;
	
	public QuizLinkedList() {
		head=null;
		tail=null;
		size=0;
	}
	
	@Override
	public void add(Quiz newQuiz) {
		if ( (head==null) && (tail==null) ) { // this caters for the adding of the first item to the list
			head=newQuiz;
			tail=newQuiz;
			size=1;
			return;
		}
		Quiz iterator=null;
		if (head.getId()>newQuiz.getId()) { // this if clause caters for the start of list needing to be replaced
			head.setPrior(newQuiz);
			newQuiz.setNext(head);
			head=newQuiz;
		} else {
			iterator=head;
			while (iterator.getNext()!=null) {
				if (iterator.getNext().getId()>newQuiz.getId()) { // need to slot the newItem in between iterator and its next node
					iterator.getNext().setPrior(newQuiz);
					newQuiz.setNext(iterator.getNext());
					iterator.setNext(newQuiz);
					newQuiz.setPrior(iterator);
					break;
				} else {
					iterator=iterator.getNext(); // get next item
				}
			}
			if (iterator.getNext()==null) {
				if (iterator.getId()<newQuiz.getId()) { // this clause deals with end of list being replaced
					newQuiz.setPrior(iterator);
					iterator.setNext(newQuiz);
					tail=newQuiz;
				} else { // the items slots into list in penultimate position
					iterator.getPrior().setNext(newQuiz);
					newQuiz.setPrior(iterator.getPrior());
					newQuiz.setNext(iterator);
					iterator.setPrior(newQuiz);
				}
			}
		}
		size++;
	}
	
	@Override
	public void delete(int id) {

		if (head.getId()==id) {
			if (size==1) {
				head=null;
				tail=null;
			} else {
				head=head.getNext();
				head.setPrior(null);
			}
			size--;
		} else {
			if (tail.getId()==id) {
				tail=tail.getPrior();
				tail.setNext(null);
				size--;
			} else {
				Quiz iterator = head;
				while (iterator.getNext()!=null) { 
					if (iterator.getNext().getId()==id) {
						iterator.setNext(iterator.getNext().getNext());
						iterator.getNext().setPrior(iterator);
						size--;
						break;
					} else {
						iterator=iterator.getNext();
					}
				}
			}
		}
	}
	
	@Override
	public Quiz get(int id) {
		if (head==null) return null;
		
		if (head.getId()==id) {
			return head;
		} else {
			if (tail.getId()==id) {
				return tail;
			} else {
				Quiz iterator = head;
				while (iterator.getNext()!=null) { 
					if (iterator.getNext().getId()==id) {
						return iterator.getNext();
					} else {
						iterator=iterator.getNext();
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public int getSize() {
		return this.size;
	}
	
	@Override
	public void print() {
		Quiz iterator = head;
		while (iterator.getNext()!=null) {
			System.out.println(iterator.getId() + ":" + iterator.getName());
			iterator=iterator.getNext();
		}
		System.out.println(iterator.getId() + ":" + iterator.getName()); // need to print this last item as loop breaks at prior item
	}
}