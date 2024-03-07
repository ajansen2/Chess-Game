package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class to describe a human player
 * @author Christopher Bury
 * @author Group 4
 * @version 1
 * @see Player
 * @see AIPlayer
 */
public class HumanPlayer extends Player{
	private BoardUI boardUI;
	private GameSetupUI setupUI;

	/**
	 * Main constructor, loads all required fields<br/><b>Only</b> use this constructor
	 * @param team    The team/color this player plays for
	 * @param ui      The BoardUI view instance that will provide input and output
	 * @param setupUi The other view instance
	 */
	public HumanPlayer(boolean team,BoardUI ui,GameSetupUI setupUi){
		super(team);
		this.boardUI=ui;
		this.setupUI=setupUi;
	}

	/** Tells the caller that this is a human and not an AI */
	@Override
	public boolean isAI(){
		return false;
	}

	/** Placeholder function since there is nothing to be done when a human player is removed */
	@Override
	public void onPlayerRemove(){
		//Does nothing
	}

	/** Sets the board to be possible to interact with, and notifies the user of their turn starting */
	@Override
	public void onTurnStart(){
		//System.out.println("Human player started turn");
		boardUI.setCanClick(true);
		setupUI.setPgnInputInteractable(true);
	}

	/** Disable interactions since the turn is over, notify the user of end of turn */
	@Override
	public void onTurnEnd(){
		//System.out.println("Human player ended turn");
		boardUI.setCanClick(false);
		setupUI.setPgnInputInteractable(false);
	}
}
