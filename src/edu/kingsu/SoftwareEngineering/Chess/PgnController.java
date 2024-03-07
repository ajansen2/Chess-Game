package edu.kingsu.SoftwareEngineering.Chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class controls saving and loading for PGN files
 * @version 0.1
 */
public class PgnController{
	private GameModel chessGame;
	private AlgNotationHelper algHelper;

	public PgnController(GameModel model,AlgNotationHelper algHelper){
		this.chessGame=model;
		this.algHelper=algHelper;
	}

	/**
	 * Load a file, set the game to resume from the point at which the PGN file ends
	 * @param pgnFile The file object containing PGN
	 */
	public void loadGame(File pgnFile){
		ArrayList<Integer> moves;
		try{
			moves=algHelper.getMovesFromPGN(pgnFile,new Board(Board.DEFAULT));
		}catch(FileNotFoundException e){
			System.out.println(e);
			return;
		}

		chessGame.newGame();
		for(int i=0; i<moves.size(); i++){
			if(moves.get(i)==Move.blank()) break;

			System.out.println("Int to notation: "+AlgNotationHelper.algNotationFromMove(moves.get(i), chessGame.getCurrentBoard(), chessGame.getCurrentPlayer().getTeam(), chessGame));

			chessGame.makeMove(moves.get(i));//TODO CHANGE THIS TO NEW SYSTEM
		}
	}

	/**
	 * Save the current game as a PGN file
	 * @param pgnPath WHere to save the file
	 */
	public void saveGame(String pgnPath){
		algHelper.createPgnFile(chessGame.getMoveList(),pgnPath+".pgn",chessGame);
	}
}