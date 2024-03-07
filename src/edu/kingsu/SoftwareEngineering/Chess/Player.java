/*
File: Player.java
Copyright (C) 2023 Dalton Herrewynen, Christopher Bury. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary authors.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.*;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class to describe a player
 * @author Christopher Bury
 * @author Dalton Herrewynen
 * @author Group 4
 * @version 2
 */
public abstract class Player{
	private final boolean team;
	private final int kingCode;//which king is ours
	private final King kingPiece;//the object pointer

	/**
	 * Constructor that loads required data into this class
	 * @param team The team this player plays for
	 */
	public Player(boolean team){
		this.team=team;
		kingCode=(team==WHITE) ? PieceCode.KingW : PieceCode.KingB;//which king is ours
		kingPiece=(King) PieceCode.pieceObj(kingCode);
	}

	/**
	 * Basic getter method to get the player team
	 * @return Team WHITE or BLACK
	 */
	public boolean getTeam(){
		return team;
	}

	/**
	 * Is this an AI player?
	 * @return Whether we're an AI player or not
	 */
	public abstract boolean isAI();

	/** This is called whenever the player object is destroyed */
	public abstract void onPlayerRemove();

	/**
	 * Gets a list of possible moves that the player can do before filtering for moves that put the king in check
	 * @param b The board
	 * @return A list of all possible moves
	 */
	public ArrayList<Integer> possibleMovesBeforeCheck(Board b){
		ArrayList<Integer> moves=new ArrayList<>(20);
		for(int piece=(team) ? 0 : 1; piece<PieceCode.PIECE_TYPES; piece+=2){//get all pieces for our team by integer code
			int pieceIndex=Coords.maskToIndex(b.searchPiece(piece));//locate the piece
			while(pieceIndex!=Coords.ERROR_INDEX){//while there is a piece to find
				moves.addAll(PieceCode.pieceObj(piece).getLegalMoves(b,pieceIndex));
				pieceIndex=Coords.maskToNextIndex(b.searchPiece(piece),pieceIndex);
			}
		}
		return moves;
	}

	/**
	 * Returns a list of all our legal moves after figuring out whether the king is in check or not
	 * @param b The current board
	 * @return A list of possible moves
	 */
	public ArrayList<Integer> possibleMoves(Board b){
		ArrayList<Integer> possMoves_=possibleMovesBeforeCheck(b);
		//You can't be checkmated if there's no king to checkmate
		if(Coords.maskToIndex(b.searchPiece(kingCode))==Coords.ERROR_INDEX) return possMoves_;//return blank if no King present

		ArrayList<Integer> temp=new ArrayList<>();

		//For each possible move, are we still in check after said move is complete?
		for(int possMove: possMoves_){
			Board newBoard=new Board(b);
			newBoard.makeMove(possMove);
			if(!kingPiece.isInCheck(newBoard,Coords.maskToIndex(newBoard.searchPiece(kingCode)))) temp.add(possMove);
		}
		return temp;
	}

	/**
	 * Returns whether we're stalemated, meaning that we have no legal moves and we're not checkmated
	 * @param b The board state
	 * @return True if stalemated, False if legal moves are found
	 */
	public boolean isStalemated(Board b){
		if(possibleMoves(b).isEmpty()) return !isCheckmated(b);
		return false;
	}

	/**
	 * Returns whether this player is checkmated
	 * TODO convert to use bit masking methods for fast searching
	 * @param b Te current board
	 * @return Whether the player is checkmated
	 */
	public boolean isCheckmated(Board b){
		final int kingIndex=Coords.maskToIndex(b.searchPiece(kingCode));//locate the king
		if(kingIndex==Coords.ERROR_INDEX || !kingPiece.isInCheck(b,kingIndex)) return false;//You can't be checkmated if there's no king to checkmate, and We're not checkmated if not currently in check

		//For each possible move, are we still in check after said move is complete?
		List<Integer> possMoves=this.possibleMoves(b);
		Board newBoard=new Board(b);
		for(int possMove: possMoves){
			newBoard.loadState(b);//only ask for memory once
			newBoard.makeMove(possMove);
			if(!kingPiece.isInCheck(newBoard,Coords.maskToIndex(newBoard.searchPiece(kingCode)))) return false;//if this move ended the check
		}
		return true;
	}

	/**
	 * Returns whether this player is in check
	 * @param b Te current board
	 * @return Whether the player is in check
	 */
	public boolean isInCheck(Board b){
		if(b.searchPiece(kingCode)==0) return false;//You can't be in check if there's no King to check
		return kingPiece.isInCheck(b,Coords.maskToIndex(b.searchPiece(kingCode)));
	}

	/**
	 * Returns a mask of squares our pieces can attack. Used for the enemy king
	 * @param b    The board
	 * @param team The team of pieces to check
	 * @return Mask of squares we can capture on
	 */
	public static long attackingMask(Board b,boolean team){
		long attackSquares=0;
		for(int piece=(team) ? PieceCode.PawnW : PieceCode.PawnB; piece<PieceCode.PIECE_TYPES; piece+=2){//get all pieces for our team by integer code
			int pieceIndex=Coords.maskToIndex(b.searchPiece(piece));//locate the piece
			while(pieceIndex!=Coords.ERROR_INDEX){//while there is a piece to find
				ArrayList<Integer> pieceMoves;
				//Check the moves of the pieces without calculating king checks
				//Calculating checks leads to an infinite loop here
				/*if(piece==PieceCode.KingW || piece==PieceCode.KingB){
					pieceMoves=((King) PieceCode.pieceObj(piece)).getLegalMovesNoSquareChecks(b,pieceIndex);
				}else{
					pieceMoves=PieceCode.pieceObj(piece).getAttackingMoves(b,pieceIndex);
				}*/
				pieceMoves=switch(piece){
					//Kings don't do checks because it can cause infinite loop (according to Chris)
					case PieceCode.KingW,PieceCode.KingB -> ((King) PieceCode.pieceObj(piece)).getLegalMovesNoSquareChecks(b,pieceIndex);
					default -> PieceCode.pieceObj(piece).getAttackingMoves(b,pieceIndex);
				};

				for(int move: pieceMoves){
					int endIndex=Move.getEndIndex(move);
					if(!Move.isBlank(move) && Coords.isIndexValid(endIndex)){//if valid index and move was set
						attackSquares|=(1L << endIndex);//add this index to the mask of attacking squares
						//Tutorial test for Ka4 mystery check
						/*if(endIndex==Coords.XYToIndex(0,3)){
							System.out.println("In Player class. a4 check is given by "+Move.getPieceName(move));
							System.out.println("a4 check pawn is located at "+new Coords(Move.getStartIndex(move)));
							int a5Piece=b.getSquare(0,4);
							System.out.println("a5 piece "+PieceCode.decodePieceName(a5Piece));
						}*/
					}
				}
				pieceIndex=Coords.maskToNextIndex(b.searchPiece(piece),pieceIndex);//iterate to the next piece
			}
		}
		return attackSquares;
	}

	/** This is called on the player when their turn is started */
	public abstract void onTurnStart();

	/** This is called on the player when their turn is finished */
	public abstract void onTurnEnd();

	/** Generates a String with this player's color that is human-readable */
	@Override
	public String toString(){
		if(team==WHITE) return "White Player";
		else return "Black Player";
	}
}
