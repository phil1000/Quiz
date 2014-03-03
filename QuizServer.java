import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * an implementation of the quiz service.
 */
public class QuizServer extends UnicastRemoteObject implements QuizService {

	private static final String FILENAME = "quizzes.txt";
	private int latestQuizId;
	private int latestPlayerId;
	private QuizList quizzes;
	private List<Player> players;
	private boolean addingQuiz=false;
	
	//private List<QuizPlayerIntersect> quizPlayerIntersects;
	
	public QuizServer() throws RemoteException {
		initialise();
	}
	
	@Override
	public Quiz getQuizStub(String name) {
		int id=latestQuizId;
		Quiz myQuiz = new QuizImpl(id, name);
		System.out.println("returning " + myQuiz.getId() + ":" + myQuiz.getName());
		latestQuizId++;
		return myQuiz;
	}
	
	@Override
	public synchronized void addQuiz(Quiz myQuiz) {
		
		if (myQuiz==null) {
			throw new NullPointerException("quiz name is null");
		}
		
		while (addingQuiz) { // this is a guard block to stop multiple setup clients updating quizlist at same time
			try {
				System.out.println("can't update as someone else doing so");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		
		addingQuiz=true; // I now want to lock whilst updating the quiz, so tell everyone
		notifyAll();
		
		quizzes.add(myQuiz);
		flush(); // Design decision update saved file each time a quiz is added
		
		System.out.println("STarting thread sleep");
		try { 
			Thread.sleep(10000); 
		} catch (InterruptedException ex) {
			// nothing to do
		}
		System.out.println("Ending thread sleep");
		addingQuiz=false; // I now want to release the lock
		notifyAll();
	}
	
	@Override
	public synchronized QuizList getAvailableQuizzes() {
		if (quizzes.getSize()==0) return null;
		
		while (addingQuiz) { // this is a guard block to wait until a quizlist has been updated before sending out list of quizzes
			try {
				System.out.println("quiz list being updated, just waiting before responding");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		return quizzes;
	}
		 
	@Override
	public synchronized Player closeQuiz(int id) {
		// check if quiz exists, return winner if it has one and then delete the quiz
		// I could have just inactivated the quiz and then let a setup client re-activate it before others
		// can play it again but I decided against it
		// write contents to file each time a quiz is closed - could be overkill i.e.
		// I could have only written to file when all quizzes had been closed but I didn't want to shutdown
		// the server at that point as someone else might want to setup a new quiz and so if I didn't write
		// to file after each quiz then I run the risk of closed but not saved quizzes if the server is shut down
		// for any reason

		while (addingQuiz) { // this is a guard block to stop deleting from quizlist while someone is updating it
			try {
				System.out.println("can't update as someone else doing so");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		
		addingQuiz=true; // I now want to lock whilst deleting the quiz, so tell everyone
		notifyAll();
				
		Player myPlayer=null;
		if (quizzes.get(id)!=null) {
			// just a holder for getting the high score for quiz 1
			myPlayer = new PlayerImpl(1, "phil");
			myPlayer.updateScore(1000);
			quizzes.delete(id);
		}
		
		flush();
		
		try { 
			Thread.sleep(10000); 
		} catch (InterruptedException ex) {
			// nothing to do
		}
		
		addingQuiz=false; // I now want to release the lock
		notifyAll();
		
		return myPlayer;
	}
	
	public void initialise() {
		// either initialize if first time called or check for and populate based on prior quizzes
		if (!new File(FILENAME).exists()) {
            quizzes = new QuizLinkedList();
			players = new ArrayList<Player>();
			//quizPlayerIntersects = new ArrayList<QuizPlayerIntersect>();
			latestQuizId=1; // first time through so initialise the quiz id seeds to 1
			latestPlayerId=1; // ditto for player
        } else {
			ObjectInputStream d = null;			
            try { 
				d = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(FILENAME)));
                quizzes = (QuizList) d.readObject();
				players = (List<Player>) d.readObject();
				//quizPlayerIntersects = (List<MeetingContactIntersect>) d.readObject();
				LatestIDs storedIds = (LatestIDs) d.readObject();
				this.latestQuizId=storedIds.latestQuizId++; // set the meeting id seeds to the value stored previously
				this.latestPlayerId=storedIds.latestPlayerId++;
				d.close(); 
            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("On read error " + ex);
            } finally {
				try {
					if (d != null) {
						d.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}	
		}
	}
	
    public void flush() {
		ObjectOutputStream encode = null;
		LatestIDs storedIds = new LatestIDs();
		storedIds.latestQuizId=this.latestQuizId;
		storedIds.latestPlayerId=this.latestPlayerId;
		try {
				encode = new ObjectOutputStream(
					new BufferedOutputStream(
                        new FileOutputStream(FILENAME)));
        } catch (FileNotFoundException ex) {
            System.err.println("encoding... " + ex);
		} catch (IOException ex) {
            ex.printStackTrace();
        }    

        try {
            encode.writeObject(quizzes);
            encode.writeObject(players);
			//encode.writeObject(quizPlayerIntersects);
			encode.writeObject(storedIds);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            encode.close();
        } catch (IOException ex2) {
            ex2.printStackTrace();
        }
	}
}