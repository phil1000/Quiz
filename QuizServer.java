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
	private int latestQuizId; 				// unique ids for quizzes
	private int latestPlayerId; 			// unique player ids
	private QuizList quizzes; 				// list of currently available quizzes
	private boolean addingQuiz=false; 		// these 3 fields are used within synchronised 
	private boolean addingQuizId=false; 	// to ensure read write consistency with player and quiz Ids
	private boolean addingPlayerId=false;	// and also the list of quizzes available
	
	public QuizServer() throws RemoteException {
		initialise(); //either open state last saved or initialise ids and quizlist on first startup
	}
	
	@Override
	public synchronized Quiz getQuizStub(String name) {
		while (addingQuizId) { // this is a guard block to stop two threads trying to get the same id
			try {
				System.out.println("can't update as someone else doing so");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		
		addingQuizId=true; // I now want to lock whilst updating the quiz, so tell everyone
		notifyAll();
		
		int id=latestQuizId;
		Quiz myQuiz = new QuizImpl(id, name);
		System.out.println("returning quiz stub" + myQuiz.getId() + ":" + myQuiz.getName());
		latestQuizId++;

		addingQuizId=false; // unlock and notify
		notifyAll();
		return myQuiz;
	}
	
	@Override
	public synchronized Player getPlayerStub(String name, Quiz quizSelected) {
		while (addingPlayerId) { // this is a guard block to stop two threads trying to get the same id
			try {
				System.out.println("can't update as someone else doing so");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		
		addingPlayerId=true; // I now want to lock whilst updating the player and id, so tell everyone
		notifyAll();
		
		int id=latestPlayerId;
		Player myPlayer = new PlayerImpl(id, name);
		System.out.println("returning player stub " + myPlayer.getId() + ":" + myPlayer.getName());
		latestPlayerId++;
		addingPlayerId=false; // unlock and notify
		notifyAll();
		
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
		
		Quiz myQuiz = quizzes.get(quizSelected.getId());
		if (myQuiz!=null) {
				myQuiz.increasePlayerCount(); //update the quiz to say that it now has a player ... the quiz can't be deleted if playercount>0
		}
		
		// flush(); Design decision to not save a new player to disc until they have finished a quiz and their score has been updated
		
		addingQuiz=false; // I now want to release the lock
		notifyAll();
		
		return myPlayer;
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
		
		addingQuiz=false; // I now want to release the lock
		notifyAll();
	}
	
	@Override
	public synchronized QuizList getAvailableQuizzes() {
		if (quizzes.getSize()==0) return null;
		
		while (addingQuiz) { // this is a guard block to wait until a quizlist has been updated before sending out list of quizzes
			try {
				System.out.println("quiz list being updated, just waiting before responding with available quizzes");
				wait(); // sleep until notified
			}
			catch (InterruptedException ex) {
				// nothing to do
			}
		}
		return quizzes;
	}
		 
	@Override
	public synchronized void updateQuiz(Player myPlayer, Quiz quizSelected) throws RemoteException {
		System.out.println("received player " + myPlayer.getId() + ":" + myPlayer.getName() + ", score=" + myPlayer.getScore());
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
		
		Quiz myQuiz = quizzes.get(quizSelected.getId());
		if (myQuiz!=null) {
				if (myQuiz.getWinner()!=null) {
					if (myPlayer.getScore()>myQuiz.getWinner().getScore()) {
						System.out.println("new winning player and score: " + myPlayer.getName() + "," + myPlayer.getScore());
						myQuiz.updateWinner(myPlayer);
					}
				} else {
					myQuiz.updateWinner(myPlayer); // first to play quiz so automatically become highest scorer
				}
				myQuiz.reducePlayerCount(); //player finished game so update quiz instance to reflect this
		}
		
		flush(); // Design decision update saved file each time a quiz is updated
		
		addingQuiz=false; // I now want to release the lock
		notifyAll();
	}
	
	@Override
	public synchronized Player closeQuiz(int id) {
		// check if quiz exists, return winner if it has one and then delete the quiz
		// I could have just inactivated the quiz and then let a setup client re-activate it before others
		// can play it again but I decided against it
		// Write contents to file each time a quiz is closed - could be overkill i.e.
		// I could have only written to file when all quizzes had been closed but then 
		// run the risk of losing quizzes if the server is shut down
		// for any reason

		Player myPlayer=null;
		
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
				
		Quiz myQuiz = quizzes.get(id);
		if (myQuiz!=null) {
			if (myQuiz.getNumberofActivePlayers()>0) {
				System.out.println("Can't close as the quiz is currently in play");
			} else {
				System.out.println("Closing quiz " + myQuiz.getId() + ":" + myQuiz.getName());
				myPlayer=myQuiz.getWinner();
				if (myPlayer!=null) {
					System.out.print("The winner was " + myPlayer.getName());
					System.out.print(" with a score of: " + myPlayer.getScore());
					System.out.println("");
				} else {
					System.out.println("This quiz was never played");
				}
				quizzes.delete(id);
				flush();
			}
		}
		
		addingQuiz=false; // I now want to release the lock
		notifyAll();
		
		return myPlayer;
	}
	
	public void initialise() {
		// either initialize if first time called or check for and populate based on prior quizzes
		if (!new File(FILENAME).exists()) {
            quizzes = new QuizLinkedList();
			latestQuizId=1; // first time through so initialise the quiz id seeds to 1
			latestPlayerId=1; // ditto for player
        } else {
			ObjectInputStream d = null;			
            try { 
				d = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(FILENAME)));
                quizzes = (QuizList) d.readObject();
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