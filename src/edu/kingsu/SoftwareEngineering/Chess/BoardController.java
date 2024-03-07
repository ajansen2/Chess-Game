/*
Copyright (C) 2023 Christopher Bury. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

import java.util.ArrayList;

/**
 * Controls the displayed Board
 * @author Christopher Bury
 * @version 1
 * @see Board
 */
public class BoardController implements BoardListener{
	private BoardUI boardUI;
	private GameModel model;
	private GameSetupUI setupUI;

	private Coords clickedPos;
	private ArrayList<Integer> moves;
	private boolean hintShowing=false;
	private Integer tempPromoMove;

	/**
	 * Constructs the controller with for the given model
	 * @param model The given model
	 */
	public BoardController(GameModel model){
		this.model=model;
		// this.boardUI=boardUI;
	}

	/**
	 * Sets which UI object the controller connects to
	 * @param ui Reference to the BoardUI object
	 */
	public void setBoardUI(BoardUI ui){
		this.boardUI=ui;
	}

	/**
	 * Sets which UI object the controller connects to
	 * @param ui Reference to the GameSetupUI object
	 */
	public void setSetupUI(GameSetupUI ui){
		this.setupUI=ui;
	}

	/**
	 * This is called when the tile is clicked. It handles logic for highlighting pieces
	 * @param coords The coordinates of the clicked tile
	 */
	public void onTileClicked(Coords coords){
		Board b=model.getCurrentBoard();
		Piece p=b.getSquareObj(coords);
		Integer moveToMake=null;

		//Hide hint when tile is clicked
		if(hintShowing){
			hintShowing=false;
			boardUI.unHiglightAll();
			boardUI.unstarAll();
		}

		if(moves!=null){
			//Tiles are currently highlighted; we check if we clicked on one
			for(Integer m: moves){
				if(m==null) continue;
				if(Move.getEndIndex(m)==coords.getIndex()){
					if(model.isTutorial()){
						//If we're in the tutorial, the move is only valid if we clicked on the starred tile
						Integer tutorialMove=TutorialHelper.getMove(model,model.getCurrentPlayer().getTeam());
						if(Move.getEndIndex(tutorialMove)==coords.getIndex()){
							moveToMake=m;
						}
					}else{
						//No tutorial means no further restrictions
						moveToMake=m;
					}
					break;
				}
			}

			if(moveToMake!=null){
				Coords endCoords=new Coords(Move.getEndIndex(moveToMake));
				//if piece=pawn && (pawn.pos=7 || pawn pos=0)
				if(moveToMake!=0 && Move.isPawnPromotion(moveToMake)){
					tempPromoMove=moveToMake;
					//TODO display promotion screen

					//All this does is ensure that the dialog actually goes somewhere on the screen
					//The dialog being below the screen isn't useful
					if(endCoords.getY()==0){
						endCoords=new Coords(endCoords.getX(),2);
					}

					//System.out.println("Pawn promo");
					setupUI.handlePawnPromotion2(endCoords);
					//return;
				}
			}

			//Regardless if a move is made, unhiglight everything
			boardUI.unHiglightAll();
			boardUI.unstarAll();
			moves=null;
			clickedPos=null;

			if(moveToMake==null && model.isTutorial()){
				tutorialStarHint();
			}
		}else{
			//No tiles are highlighted; highlight things if it's an allied piece
			if(model.getCurrentPlayer()==null) System.out.println("Null player!");
			if(p!=null && p.getTeam()==model.getCurrentPlayer().getTeam()){
				moves=p.getLegalMovesAfterChecks(b,coords.getIndex());

				clickedPos=coords;
				System.out.println(p+" has "+moves.size()+" legal moves");

				for(Integer m: moves){
					if(m==null || Move.isBlank(m)) continue;
					//boardTiles.get(index).highlightTile();
					boardUI.highlightTile(new Coords(Move.getEndIndex(m)));
				}

				//Highlight tile that's clicked on green
				boardUI.highlightTileGreen(coords);

				if(model.isTutorial()){
					//If we click on the starred piece, star the end move and unstar old square
					Integer m=TutorialHelper.getMove(model,p.getTeam());

					System.out.println("Start: "+new Coords(Move.getStartIndex(m)));
					System.out.println("Clicked: "+clickedPos);
					System.out.println("End: "+new Coords(Move.getEndIndex(m)));
					if(Move.getStartIndex(m)==clickedPos.getIndex()){
						boardUI.unstarAll();
						boardUI.starTile(new Coords(Move.getEndIndex(m)));
					}
				}
				//test
				//boardUI.starTile(coords);
			}
		}

		if(moveToMake!=null && tempPromoMove==null){
			model.makeMove(moveToMake);
			moveToMake=null;
			//setupUI.checkPromotingPawns();
		}
	}

	/**
	 * Sends a PGN move to the model
	 * @param move The move we want to make
	 * @return Whether the move is legal
	 */
	public boolean pgnMoveInput(String move){
		try{
			return model.makeMove(move);
		}catch(Exception e){
			return false;
		}
	}

	public void promotionPiecePicked(pieceType type){
		if(tempPromoMove==null) return;
		int pieceCode=0;
		int m=Move.encode(Move.normalMove,
				PieceCode.encodePieceType(type,model.getCurrentPlayer().getTeam()),
				Move.getStartIndex(tempPromoMove),Move.getEndIndex(tempPromoMove));

		model.makeMove(m);
		tempPromoMove=null;
	}

	/** Shows a hint on the board */
	public void showHint(){
		Player pMax=model.getPlayerFromTeam(WHITE);
		Player pMin=model.getPlayerFromTeam(BLACK);
		boolean team=model.getCurrentPlayer().getTeam();
		hintShowing=true;

		new Thread(()->{
			// AI move calculation logic here

			MoveScorePair aiMove=AiHelper.minimax(model.getCurrentBoard(),4,team,pMax,pMin,Integer.MIN_VALUE,Integer.MAX_VALUE);
			//System.out.println(aiMove);
			int m=aiMove.move();
			boardUI.highlightTileGreen(new Coords(Move.getStartIndex(m)));
			boardUI.highlightTile(new Coords(Move.getEndIndex(m)));
		}).start();
	}

	/** Places a star hint on this tile for the tutorial mode */
	public void tutorialStarHint(){
		Player p=model.getCurrentPlayer();
		Integer m=TutorialHelper.getMove(model,p.getTeam());

		if(m!=null) boardUI.starTile(new Coords(Move.getStartIndex(m)));
		else model.toggleTutorial(false);//We're out of tutorial moves
	}

	/** Makes this tile update when the board changes */
	@Override
	public void onBoardChanged(Board newBoard){
		//Show hint if we're playing as white and tutorial enabled
		if(model.isTutorial() && model.getCurrentPlayer().getTeam()==WHITE){
			boardUI.unHiglightAll();
			boardUI.unstarAll();
			//showHint();
			tutorialStarHint();

			//The first blurb shows up in response to the intro blurb being closed
			//For everything else, show the move count blurb each turn
			if(model.getMoveCount()!=0) TutorialHelper.showTutorialBlurb(model.getMoveCount());
		}

		if(model.getAlwaysShowHints() && !model.getCurrentPlayer().isAI()){
			//If we're not an AI and always showing hints is enabled, show one!
			showHint();
		}

	}
}