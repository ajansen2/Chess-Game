/*
File: Rook.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;

import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class for the Rook piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class Rook extends Piece{
	/**
	 * Constructs the Rook and sets the team (color, team, etc.)
	 * @param team The Team Enum containing one of the possible teams
	 */
	public Rook(boolean team){
		super(team,pieceType.rook);
	}

	/**
	 * Converts the Rook to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" Rook";
	}

	/**
	 * Calculates the relative value of this piece, used for AI
	 * @param board    The board this Rook is on
	 * @param position Where the Rook is on the board
	 * @return Relative value for AI
	 */
	//TODO finish this
	@Override
	public int pieceValue(final Board board,final int position){
		ArrayList<Integer> moves=getLegalMoves(board,position);
		int score=500;
		score+=moves.size()*10;
		return score;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for Rook
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,int position){
		long enemies=board.alliedPieceMask(!team);
		long blanks=~(enemies | board.alliedPieceMask(team));//add the enemies and friends together, invert to get blanks
		return HVLineCheck(enemies,blanks,position);
	}

	/**
	 * Generates all legal moves that this Rook can do
	 * @param board    The board this Rook is on
	 * @param position Where the Rook is on the board
	 * @return The list of legal moves
	 * @author Christopher Bury
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(final Board board,final Coords position){
		ArrayList<SlowMove> temp=new ArrayList<>();
		temp.addAll(pieceLineMoveCheckObj(board,position,1,0));
		temp.addAll(pieceLineMoveCheckObj(board,position,-1,0));
		temp.addAll(pieceLineMoveCheckObj(board,position,0,1));
		temp.addAll(pieceLineMoveCheckObj(board,position,0,-1));
		return temp;
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