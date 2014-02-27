import java.io.Serializable;

public class PlayerImpl implements Player, Serializable {

	private int id;
	private String name;
	private int score;
	
	public PlayerImpl(int id, String name) {
		this.id=id;
		this.name=name;
	}
	
	@Override
	public void updateScore(int score) {
		this.score = score;
	}
	
	@Override
    public String getName() {
		return this.name;
	}
	
	@Override
    public int getId() {
		return this.id;
	}
	
	@Override
    public int getScore() {
		return this.score;
	}
}