/*
File: Bishop.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;

import edu.kingsu.SoftwareEngineering.Chess.Types.pieceType;

/**
 * Class for the Bishop piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class Bishop extends Piece{
	/**
	 * Only constructor requires the setting of team (color)
	 * @param team which side is this piece going to be on
	 */
	public Bishop(boolean team){
		super(team,pieceType.bishop);
	}

	/**
	 * Converts the Bishop to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" Bishop";
	}

	/**
	 * Calculates the Piece's value to the AI player
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return relative value to the AI
	 */
	@Override
	public int pieceValue(Board board,int position){
		int score=300;
		ArrayList<Integer> moves=getLegalMoves(board,position);
		score+=moves.size()*10;
		return score;
	}

	/**
	 * Returns all legal moves that this piece is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @author Christopher Bury
	 * @return ArrayList of SlowMove objects that are legal
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position){
		ArrayList<SlowMove> legalMoves=new ArrayList<>();

		legalMoves.addAll(pieceLineMoveCheckObj(board,position,1,1));
		legalMoves.addAll(pieceLineMoveCheckObj(board,position,-1,-1));
		legalMoves.addAll(pieceLineMoveCheckObj(board,position,-1,1));
		legalMoves.addAll(pieceLineMoveCheckObj(board,position,1,-1));

		return legalMoves;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for Bishop
	 * Just translates the old <code>getLegalMovesObj</code> method right now
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,int position){
		long enemies=board.alliedPieceMask(!team);
		long blanks=~(enemies | board.alliedPieceMask(team));
		return diagLineCheck(enemies,blanks,position);
	}

	/**
	 * Placeholder for the incomplete method
	 * @param o the Piece to be compared.
	 * @return 0
	 */
	@Override
	public int compareTo(Piece o){
		return 0;
	}
}