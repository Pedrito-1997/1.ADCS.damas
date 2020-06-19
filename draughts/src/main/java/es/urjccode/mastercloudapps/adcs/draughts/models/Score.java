package es.urjccode.mastercloudapps.adcs.draughts.models;

public class Score {

    private int scoreValue;

	public Score() {
		this.reset();
	}

	public void reset() {
		this.scoreValue = 0;
	}

	public int getValueState() {
		return this.scoreValue;
	}

}
