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
	private List<Quiz> quizzes;
	private List<Player> players;
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
	public void addQuiz(Quiz myQuiz) {
		if (myQuiz==null) {
			throw new NullPointerException("quiz name is null");
		}
		quizzes.add(myQuiz);
	}
	
	@Override
	public List<Quiz> getAvailableQuizzes() {
		if (quizzes.isEmpty()) return null;
		else return quizzes;
	}
		 
	@Override
	public Player closeQuiz(Quiz quizToBeClosed) {
		// check if quiz exists, return winner if it has one and then close quiz
		// write contents to file each time a quiz is closed - could be overkill i.e.
		// I could have only written to file when all quizzes had been closed but I didn't want to shutdown
		// the server at that point as someone else might want to setup a new quiz and so if I didn't write
		// to file after each quiz then I run the risk of closed but not saved quizzes if the server is shut down
		// for any reason
		Player myPlayer = null;
		ListIterator<Quiz> q = quizzes.listIterator();
		while (q.hasNext()) {
				Quiz currentQuiz = q.next();
				if (currentQuiz.getId() == quizToBeClosed.getId()) {
					q.remove();
					myPlayer = new PlayerImpl(1, "phil");
					myPlayer.updateScore(1000);
					break;
				}
		}
		
		flush();
		return myPlayer;
	}
	
	public void initialise() {
		// either initialize if first time called or check for and populate based on prior quizzes
		if (!new File(FILENAME).exists()) {
            quizzes = new ArrayList<Quiz>();
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
                quizzes = (List<Quiz>) d.readObject();
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