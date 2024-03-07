package edu.kingsu.SoftwareEngineering.Chess;

import java.util.*;
import java.lang.*;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * AI helper class, holds static helper methods used by the AI
 * @author Group 4
 * @version 1
 * @see Player
 * @see AIPlayer
 */
public class AiHelper implements Runnable{
	private final int maxDepth;


	public AiHelper(int maxDepth){
		this.maxDepth=maxDepth;
	}

	/**
	 * Implement a simple evaluation function to assess the board's position
	 * Assign a score to the board based on factors like piece values, position, and control of the center.
	 * Positive scores favor White, and negative scores favor Black.
	 * @param board Board state to get score of
	 * @param pMax         The maximizing player object
	 * @param pMin         The minimizing player object
	 * @return Positive scores favor White, and negative scores favor Black.
	 */
	public static int evaluate(Board board,Player pMax, Player pMin){
		if(pMax.isCheckmated(board)) return Integer.MIN_VALUE;
		if(pMin.isCheckmated(board)) return Integer.MAX_VALUE;
		int score=0;
		long mask;
		int piecePos;
		for(int i=0; i<PieceCode.PIECE_TYPES; ++i){//for each WHITE piece type
			mask=board.searchPiece(i);
			piecePos=Coords.maskToIndex(mask);//DO NOT SEARCH ALL SQUARES, WE HAVE A BITBOARD! USE IT!!!!!
			while(piecePos!=Coords.ERROR_INDEX){//get the nex index if there
				score+=PieceCode.pieceObj(i).pieceValue(board,piecePos);
				piecePos=Coords.maskToNextIndex(mask,piecePos);
			}
			++i;//for each BLACK piece
			mask=board.searchPiece(i);
			piecePos=Coords.maskToIndex(mask);//DO NOT SEARCH ALL SQUARES, WE HAVE A BITBOARD! USE IT!!!!!
			while(piecePos!=Coords.ERROR_INDEX){//get the nex index if there
				score-=PieceCode.pieceObj(i).pieceValue(board,piecePos);
				piecePos=Coords.maskToNextIndex(mask,piecePos);
			}
		}
		//System.out.println("\n\nScore: "+score+"\n" +  board);
		return score;
	}

	/**
	 * Performs the minimax algorithm and returns an optimal move.
	 * @param board        The board to analyze
	 * @param depth        The depth to search
	 * @param isMaximizing Are we maximizing or minimizing score? This depends on whether we're controlling white or black
	 * @param pMax         The maximizing player object
	 * @param pMin         The minimizing player object
	 * @param alpha        The bounding value that's set while maximizing score
	 * @param beta         The bounding value that's set while minimizing score
	 * @return The calculated optimal move paired with its evaluated score
	 */
	public static MoveScorePair minimax(Board board,int depth,boolean isMaximizing,Player pMax,Player pMin,int alpha,int beta){
		if(depth==0) return new MoveScorePair(Move.blank(),evaluate(board,pMax,pMin));

		Board movedBoard=new Board(Board.CLEAR);
		int bestMove=Move.blank();
		int bestValue=(isMaximizing) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		List<Integer> possibleMoves=(isMaximizing)? pMax.possibleMoves(board) : pMin.possibleMoves(board);

		for(int move: possibleMoves){
			movedBoard.loadState(board);
			movedBoard.makeMove(move);
			int currentValue=minimax(movedBoard,depth-1,!isMaximizing,pMax,pMin,alpha,beta).score();

			if(isMaximizing){
				if(currentValue>bestValue){
					bestValue=currentValue;
					bestMove=move;
				}
				alpha=Math.max(alpha,currentValue);
			}else{
				if(currentValue<bestValue){
					bestValue=currentValue;
					bestMove=move;
				}
				beta=Math.min(beta,currentValue);
			}
			if(beta<=alpha) break; // Prune the search tree
		}
		//System.out.println("AI score test: "+bestValue+" "+bestMove.piece());
		return new MoveScorePair(bestMove,bestValue);
	}

	/**
	 * Runs the AI player thread
	 */
	@Override
	public void run(){
	}
}
