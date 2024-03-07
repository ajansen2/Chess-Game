package edu.kingsu.SoftwareEngineering.Chess.Adi;

import java.util.*;

public abstract class Piece{
	//Every piece will be having a position on the board
	protected final int piecePosition;

	//Every piece will be either black or white. So we will have a varibale of type alliance which
	//we will declare
	protected final Alliance pieceAlliance;

	//comstructor to set the values
	Piece(final int piecePosition,final Alliance pieceAlliance){
		this.pieceAlliance=pieceAlliance;
		this.piecePosition=piecePosition;
	}

	//getter method for the alliance
	public Alliance getPieceAlliance(){
		return pieceAlliance;
	}

	//Calculating legal moves of the piece
	//returns a list of legal moves
	public abstract Collection<Move> calculateLegalMove(final Board board);
}
