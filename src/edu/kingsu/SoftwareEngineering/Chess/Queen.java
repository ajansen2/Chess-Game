/*
File: Queen.java
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
 * Class for the Queen piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class Queen extends Piece{
	/**
	 * Only constructor requires the setting of team (color)
	 * @param team which side is this piece going to be on
	 */
	public Queen(boolean team){
		super(team,pieceType.queen);
	}

	/**
	 * Converts the Queen to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" Queen";
	}

	/**
	 * Placeholder for the incomplete method
	 * @param board    The current Board state
	 * @param position Where the piece is on the board
	 * @return The relative score for the AI
	 */
	//TODO Complete pieceValue()
	@Override
	public int pieceValue(Board board,int position){
		int score=1500;
		ArrayList<Integer> moves=getLegalMoves(board,position);
		score+=moves.size()*15;
		return score;
	}

	/**
	 * Returns all legal moves that this piece is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of SlowMove objects that are legal
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position){
		ArrayList<SlowMove> temp=new ArrayList<>();

		//Bishop moves
		temp.addAll(pieceLineMoveCheckObj(board,position,1,1));
		temp.addAll(pieceLineMoveCheckObj(board,position,-1,-1));
		temp.addAll(pieceLineMoveCheckObj(board,position,-1,1));
		temp.addAll(pieceLineMoveCheckObj(board,position,1,-1));

		//Rook moves
		temp.addAll(pieceLineMoveCheckObj(board,position,1,0));
		temp.addAll(pieceLineMoveCheckObj(board,position,-1,0));
		temp.addAll(pieceLineMoveCheckObj(board,position,0,1));
		temp.addAll(pieceLineMoveCheckObj(board,position,0,-1));

		return temp;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for Queen
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,int position){
		ArrayList<Integer> moves=new ArrayList<>();
		long enemies=board.alliedPieceMask(!team);
		long blanks=~(enemies | board.alliedPieceMask(team));//add the enemies and friends together, invert to get blanks

		//Bishop moves
		moves.addAll(diagLineCheck(enemies,blanks,position));

		//Rook moves
		moves.addAll(HVLineCheck(enemies,blanks,position));

		return moves;
	}

	/**
	 * Placeholder for the incomplete method
	 * @param o the piece to be compared.
	 * @return 0
	 */
	//TODO Complete compareTo()
	@Override
	public int compareTo(Piece o){
		return 0;
	}
}
