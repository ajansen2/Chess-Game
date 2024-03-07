package edu.kingsu.SoftwareEngineering.Chess;

import java.util.*;
import java.lang.*;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * AI player, a player that uses algorithms to calculate the best move
 * @author Group 4
 * @version 1
 * @see Player
 * @see HumanPlayer
 */
public class AIPlayer extends Player{
	private final int maxDepth;
	private final int[] DEPTH_SETTINGS=new int[]{4,10,30};

	private GameModel model;
	private GameSetupUI setupUI;
	private Thread moveThread;
	private Thread[] workers;
	private int[] bestMoves, bestScores;

	/**
	 * Default constructor tells the AI who to play for
	 * @param team     WHITE or BLACK team
	 * @param maxDepth How many turns to look into the future
	 * @param model    The Game Model
	 */
	public AIPlayer(boolean team,int maxDepth,GameModel model,GameSetupUI setupUI){
		super(team);
		if(team==WHITE && Main.AIPowerWhite!=0) this.maxDepth=Main.AIPowerWhite;//YOU GUYS FORCED MY HAND, This is so if you waste time on images and other non-marked things, my piece moves will still make the AI FAST
		else if(team==BLACK && Main.AIPowerBlack!=0)this.maxDepth=Main.AIPowerBlack;
		else this.maxDepth=maxDepth;
		this.model=model;
		this.setupUI=setupUI;
		System.out.println("AI level: "+this.maxDepth);
	}

	/** Tells the calling party that this is an AI player and not a human */
	@Override
	public boolean isAI(){
		return true;
	}

	/**
	 * Returns the search depth on this AI player. Higher equals harder to beat
	 * @return The search depth
	 */
	public int getSearchDepth(){
		return maxDepth;
	}

	/**
	 * In this method, my idea is to use the minmax algorithm to make the move. it is a recursive
	 * algorithm that will look at all possible moves and find out which one has the highest value
	 * or lowest if its maximizing player.
	 * @param board current state of board
	 * @return returns the best possible move considering the current state of board
	 */
	public int makeMove(Board board){
		Player pMax=model.getPlayerFromTeam(WHITE),pMin=model.getPlayerFromTeam(BLACK);
		List<Integer> possMovesList=possibleMoves(board);
		int bestMove=Move.blank();

		int bestScore=0;
		for(int m: possMovesList){
			Board moveBoard=new Board(board);
			moveBoard.makeMove(m);
			int score=AiHelper.minimax(moveBoard,DEPTH_SETTINGS[maxDepth],getTeam(),pMax,pMin,Integer.MIN_VALUE,Integer.MAX_VALUE).score();

			if(getTeam() && score>bestScore){
				bestScore=score;
				bestMove=m;
			}else if(!getTeam() && score<bestScore){
				bestScore=score;
				bestMove=m;
			}
		}
		return bestMove;
	}

	/** Actions to do every time the player starts a turn. Starts the threads and begins the move search */
	@Override
	public void onTurnStart(){
		//System.out.println("AI player started turn");
		singleThread();
		//SlowMove move=makeMove(model.getCurrentBoard());
		//model.makeMove(move);
	}

	/** for single threaded AI */
	private void singleThread(){
		Player pMax=model.getPlayerFromTeam(WHITE),pMin=model.getPlayerFromTeam(BLACK);
		moveThread=new Thread(()->{
			long startTime=System.currentTimeMillis();
			final long delay_ms=1000,minDelay=600;
			int alpha=Integer.MIN_VALUE;
			int beta=Integer.MAX_VALUE;
			try{
				if(model.isTutorial()){
					int m=TutorialHelper.getMove(model,getTeam());
					while(System.currentTimeMillis()-startTime<minDelay || System.currentTimeMillis()<startTime){//IDK if a roll-over can happen but meh
						Thread.sleep(100);//THIS IS THE DELAY
						System.out.println("Ai Thread waiting min delay, waiting for "+(System.currentTimeMillis()-startTime)+"ms / "+delay_ms+"ms before exit");
					}
					model.makeMove(m);
				}else{
					// AI move calculation logic here
					System.out.println("AI runs");
					MoveScorePair aiMove=AiHelper.minimax(model.getCurrentBoard(),maxDepth,getTeam(),pMax,pMin,alpha,beta);
					System.out.println(Move.describe(aiMove.move()));
					while(System.currentTimeMillis()-startTime<minDelay || System.currentTimeMillis()<startTime){//IDK if a roll-over can happen but meh
						Thread.sleep(100);//THIS IS THE DELAY
						System.out.println("Ai Thread waiting min delay, waiting for "+(System.currentTimeMillis()-startTime)+"ms / "+delay_ms+"ms before exit");
					}
					model.makeMove(aiMove.move());
					// Update GUI after AI makes a move
					//updateGUI();
				}
				while(System.currentTimeMillis()-startTime<delay_ms || System.currentTimeMillis()<startTime){//IDK if a roll-over can happen but meh
					Thread.sleep(100);//THIS IS THE DELAY
					System.out.println("Ai Thread done, waiting for "+(System.currentTimeMillis()-startTime)+"ms / "+delay_ms+"ms before exit");
				}
			}catch(Exception e){
				// Cleanup logic
				System.out.println("Interrupted");
			}
			System.out.println("Done");
		});
		setupUI.showAIDecisionSpinner();
		moveThread.start();//this is right, but it causes race condition because no mutex
	}

	/** Clean up and notify the end of turn */
	@Override
	public void onTurnEnd(){
		//System.out.println("AI player ended turn");
		if(!model.getCurrentPlayer().isAI()){
		setupUI.hideAIDecisionSpinner();
		killMoveThread();
		}
	}

	/** Call this when the AI player is removed from play */
	@Override
	public void onPlayerRemove(){
		killMoveThread();
	}

	/** Stop searching */
	public void killMoveThread(){
		System.out.println("Joining threads");
		if(moveThread!=null) moveThread.interrupt();
		try{
			assert moveThread!=null;
			if(moveThread!=null) moveThread.join();
		}catch(InterruptedException e){
			System.out.println("Exception while Joining: "+e);
		}
		System.out.println("Joined threads");
	}
}