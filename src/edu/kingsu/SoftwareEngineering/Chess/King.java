/*
File: King.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;
import java.util.HashSet;

import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class for the King piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class King extends Piece{
	/** Generate all the possible directions a king can move */
	private static final int[][] offset={{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,-1},{1,-1},{-1,1}};
	/** Masks for checking Queen and King side castling */
	private static final long
			qSideCastleMask=0b0000000000000000000000000000000000000000000000000000000000001110L,
			kSideCastleMask=0b0000000000000000000000000000000000000000000000000000000001100000L;

	/**
	 * Only constructor requires the setting of team (color)
	 * @param team which side is this piece going to be on
	 */
	public King(boolean team){
		super(team,pieceType.king);
	}

	/**
	 * Converts the King to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" King";
	}

	/**
	 * Calculates the King's value to the AI player
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return relative value to the AI
	 */
	@Override
	public int pieceValue(final Board board,final int position){
		return Integer.MAX_VALUE;
	}

	/**
	 * Returns all legal moves that this King is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of SlowMove objects that are legal
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position){
		ArrayList<SlowMove> temp=new ArrayList<>();
		long threatSquares=Player.attackingMask(board,!team);

		//Check each direction
		for(int[] dir: offset){
			SlowMove dirMove=pieceSingleMoveCheckObj(board,position,dir[0],dir[1]);
			if(dirMove==null || dirMove.end()==null) continue;//skip empty moves or empty destinations
			//We can only move the king to squares that enemy pieces don't control
			if(0!=(threatSquares & (1L << dirMove.end().getIndex()))) temp.add(dirMove);
		}
		//Castling moves
		if(board.hasNotMoved(position) && !isInCheck(board,position.getIndex())){//no castling if moved or in check
			//Checking the queenside
			Coords qSideRookPos=new Coords(0,position.getY());//make this work for both kings
			Coords kSideRookPos=new Coords(BOARD_SIZE-1,position.getY());//make this work for both kings any size board
			Piece rook=board.getSquareObj(qSideRookPos);
			if(rook!=null && rook.getType()==pieceType.rook &&
					board.hasNotMoved(qSideRookPos) && isLineEmpty(board,position,qSideRookPos)){
				temp.add(SlowMove.GenerateCastle(moveTypes.queenSideCastle,team));//We can castle Queen side
			}
			//Checking the kingside
			rook=board.getSquareObj(kSideRookPos);
			if(rook!=null && rook.getType()==pieceType.rook &&
					board.hasNotMoved(kSideRookPos) && isLineEmpty(board,position,kSideRookPos)){
				temp.add(SlowMove.GenerateCastle(moveTypes.kingSideCastle,team));//We can castle King side
			}
		}
		return temp;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for King
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,int position){
		ArrayList<Integer> moves=new ArrayList<>();
		long threatSquares=Player.attackingMask(board,!team);
		long enemies=board.alliedPieceMask(!team);
		long blanks=~(enemies | board.alliedPieceMask(team));

		//Check each direction
		for(int[] dir: offset){
			int destIndex=Coords.shiftIndex(position,dir[0],dir[1]);
			if(!Coords.isShiftValid(position,dir[0],dir[1]) || 0!=(threatSquares & (1L << destIndex)))
				continue;//We can only move the King to squares that exist and won't cause a check
			if(0!=(blanks & (1L << destIndex))) moves.add(Move.encodeNormal(this.pieceCode,position,destIndex));//if blank, then just move
			else if(0!=(enemies & (1L << destIndex))) moves.add(Move.encode(Move.capture,this.pieceCode,position,destIndex));//Only capture the other team
		}
		//Castling moves
		if(board.hasNotMoved(position) && !isInCheck(board,position)){//no castling if moved or in check
			//Checking the queenside
			int rookPos=Coords.XYToIndex(0,Coords.indexToY(position));//get the rook for this side
			if(PieceCode.Blank==board.getSquare(Coords.shiftMask(qSideCastleMask,0,Coords.indexToY(position)))//use shifting of the mask to make it work for WHITE and BLACK
					&& board.hasNotMoved(rookPos)){//Must have blank line between the King and Rook and both must be unmoved
				moves.add(Move.encodeCastle(Move.qSideCastle, team));//We can castle Queen side
			}
			//Checking the kingside
			rookPos=Coords.XYToIndex(BOARD_SIZE-1,Coords.indexToY(position));//get the other rook for this side
			if(PieceCode.Blank==board.getSquare(Coords.shiftMask(kSideCastleMask,0,Coords.indexToY(position)))
					&& board.hasNotMoved(rookPos)){//Must have blank line between the King and Rook and both must be unmoved
				moves.add(Move.encodeCastle(Move.kSideCastle, team));//We can castle King side
			}
		}
		return moves;
	}

	/**
	 * Returns all moves that this King is capable of making, disregarding enemy controlled squares
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of integers encoding moves
	 */
	public ArrayList<Integer> getLegalMovesNoSquareChecks(Board board,int position){
		ArrayList<Integer> moves=new ArrayList<>();
		//Check each direction
		for(int[] dir: offset){
			int dirMove=pieceSingleMoveCheck(board,position,dir[0],dir[1]);//TODO speed test against a look up table
			moves.add(dirMove);
		}
		return moves;
	}

	/**
	 * Checks if King is in check
	 * @param board    The current Board object
	 * @param position Where this King is located on the board
	 * @return True if the King is in check, False otherwise
	 */
	public boolean isInCheck(Board board,int position){
		//Check if any enemy pieces can can attack the King's position
		for(int piece=(!team) ? 0 : 1; piece<PieceCode.KingW; piece+=2){//get all pieces for other team by integer code
			int pieceIndex=Coords.maskToIndex(board.searchPiece(piece));//locate the piece
			while(pieceIndex!=Coords.ERROR_INDEX){//while there is a piece to find
				ArrayList<Integer> enemyMoves=PieceCode.pieceObj(piece).getLegalMoves(board,pieceIndex);//Get the legal moves from the enemy piece
				//todo Filter out pieces via bit masking
				for(int enemyMove: enemyMoves){//Check if any enemy move ends at the King's position
					if(Move.getEndIndex(enemyMove)==position) return true; //King is in check
				}
				pieceIndex=Coords.maskToNextIndex(board.searchPiece(piece),pieceIndex);//iterator
			}
		}
		return false;//King is not in check
	}

	/**
	 * Placeholder for the incomplete method
	 * @param o the piece to be compared.
	 * @return 0
	 */
	@Override
	public int compareTo(Piece o){
		return 0;
	}
}
