/*
File: Knight.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */

package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
/**
 * Class for the Knight piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class Knight extends Piece{
	/** All the offsets that the Knight can move too */
	private static final int[][] offset={//0 is x, 1 is y
			{-2,1},{-1,2},{-2,-1},{-1,-2},//left half
			{2,1},{1,2},{2,-1},{1,-2}};   //right half

	/**
	 * Only constructor requires the setting of team (color)
	 * @param team which side is this piece going to be on
	 */
	public Knight(boolean team){
		super(team,pieceType.knight);
	}

	/**
	 * Converts the Knight to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" Knight";
	}

	/**
	 * Calculates the Knight's value to the AI player
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return relative value to the AI
	 */
	@Override
	public int pieceValue(Board board,int position){//TODO Dalton speed test against a matrix and bit mask based algorithm
		int score=300, xPos=Coords.indexToX(position), yPos=Coords.indexToY(position);
		if(xPos==0 || xPos==7){
			if(yPos==0 || yPos==7) score-=75;//Steep penalty for knights in the corners
			else score-=50;//Smaller penalty for knights at the edge
		}
		return score;
	}

	/**
	 * Returns all legal moves that this Knight is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of SlowMove objects that are legal
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position){
		ArrayList<SlowMove> moves=new ArrayList<>();
		Coords destCand;//destination candidate
		Piece destSquare;
		//apply each offset, check if it's legal
		for(int[] ints: offset){
			destCand=new Coords(position);
			if(!destCand.addVector(ints[0],ints[1])) continue;//skip if the move would go off the board
			destSquare=board.getSquareObj(destCand);//if move was on the board, then check what's there
			if(destSquare==null){//regular move
				moves.add(new SlowMove(position,destCand,this.getChar(),moveTypes.justMove));
			}else if(destSquare.getTeam()!=this.getTeam()){//capture
				moves.add(new SlowMove(position,destCand,this.getChar(),destSquare.getChar()));
			}
		}
		return moves;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for Knight
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,final int position){
		ArrayList<Integer> moves=new ArrayList<>();
		long enemies=board.alliedPieceMask(!team);
		long blanks=~(enemies | board.alliedPieceMask(team));
		//Check each direction
		for(int[] dir: offset){
			int destIndex=Coords.shiftIndex(position,dir[0],dir[1]);
			if(!Coords.isShiftValid(position,dir[0],dir[1])) continue;//We can only move the knight to squares that exist
			if(0!=(blanks & (1L << destIndex))) moves.add(Move.encodeNormal(this.pieceCode,position,destIndex));//if blank, then just move
			else if(0!=(enemies & (1L << destIndex))) moves.add(Move.encode(Move.capture,this.pieceCode,position,destIndex));//Only capture the other team
		}
		return moves;
	}

	/**
	 * @param o the object to be compared.
	 * @return 0
	 */
	@Override
	public int compareTo(Piece o){
		return 0;
	}
}