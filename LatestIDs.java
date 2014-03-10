import java.io.Serializable;
 public class LatestIDs implements Serializable {
	//this class has public attributes as it is just used as a serializable class to store/retrieve
	// the latest quiz and player ids
	public int latestQuizId;
	public int latestPlayerId;
}