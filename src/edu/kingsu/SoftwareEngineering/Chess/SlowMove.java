/*
File: SLowMove.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * This is a record that represents a singular immutable move.
 * Not to be used anymore since the creation of objects is dreadfully slow
 * @param start          Starting coordinate
 * @param end            Destination coordinate
 * @param piece          The char representation of the piece that made the move
 * @param type           The type of move as noted by the moveTypes enum
 * @param capturedPiece  If a piece was captured, which one
 * @param numberOfChecks How many checks the King is under
 * @param PGNNotation    String storing the algebraic notation of the move
 * @param winner         Stores if there was a winner or if neither player wins (yet)
 * @param gameover       Stores if the game was over and the reason for that end
 * @author Dalton Herrewynen
 * @author Group 4
 * @version 1
 * @see Coords
 * @deprecated Please switch to using our new, more efficient Move class which encodes integers
 */
public record SlowMove(Coords start,Coords end,char piece,moveTypes type,char capturedPiece,
                       int numberOfChecks,String PGNNotation,Winner winner,causeOfGameover gameover){
	public SlowMove{
		if(start==null) throw new IllegalArgumentException("The starting coordinate object must not be null.");
		if(start.isSet()==UNSET) throw new IllegalArgumentException("The starting coordinate object must be set.");
		if(end==null) throw new IllegalArgumentException("The ending coordinate object must not be null.");
		if(end.isSet()==UNSET) throw new IllegalArgumentException("The ending coordinate object must be set.");
		start=start.copy();//force a copy to prevent problems with pass-by-reference
		end=end.copy();
	}

	/**
	 * Construct a minimally populated SlowMove object for basic moves
	 * @param startPos Starting coordinate
	 * @param endPos   Ending coordinate
	 * @param piece    The piece code
	 */
	public SlowMove(Coords startPos,Coords endPos,char piece){
		this(startPos,endPos,piece,moveTypes.justMove);
	}

	/**
	 * Construct a minimally populated SlowMove object for basic moves
	 * @param startPos Starting coordinate
	 * @param endPos   Ending coordinate
	 * @param piece    The piece Object
	 */
	public SlowMove(Coords startPos,Coords endPos,Piece piece){
		this(startPos,endPos,piece.getChar());
	}

	/**
	 * Construct a minimally populated SlowMove object with forced move type
	 * Chained to force a copy of the Coords objects
	 * @param startPos Starting coordinate
	 * @param endPos   Ending coordinate
	 * @param piece    The piece code
	 * @param moveType The desired moveType
	 */
	public SlowMove(Coords startPos,Coords endPos,char piece,moveTypes moveType){
		this(startPos,endPos,piece,moveType,
				' ',0,"",Winner.none,causeOfGameover.none);
	}

	/**
	 * Construct a minimally populated SlowMove object with forced move type
	 * Chained to force a copy of the Coords objects
	 * @param startPos   Starting coordinate
	 * @param endPos     Ending coordinate
	 * @param piece      The piece code
	 * @param moveType   The desired moveType
	 * @param enemyPiece The captured piece
	 */
	public SlowMove(Coords startPos,Coords endPos,char piece,moveTypes moveType,char enemyPiece){
		this(startPos,endPos,piece,moveType,
				enemyPiece,0,"",Winner.none,causeOfGameover.none);
	}

	/**
	 * Construct a mostly populated SlowMove object
	 * @param startPos   Starting coordinate
	 * @param endPos     Ending coordinate
	 * @param piece      The piece Object
	 * @param enemyPiece The captured piece code
	 */
	public SlowMove(Coords startPos,Coords endPos,char piece,char enemyPiece){
		this(startPos,endPos,piece,moveTypes.capture,enemyPiece,
				0,"",Winner.none,causeOfGameover.none);
	}

	/**
	 * Copys a move with new Algebraic notation
	 * Created specifically because of one method in the AlgNotationHelper file
	 * @param m        The original SlowMove
	 * @param notation The new algebraic notation
	 */
	public SlowMove(SlowMove m,String notation){
		this(m.start,m.end,m.piece,m.type,m.capturedPiece,m.numberOfChecks,notation,m.winner,m.gameover);
	}

	/**
	 * Copys a move with updated team
	 * Created specifically because of one method in the AlgNotationHelper file
	 * @param m      The original SlowMove
	 * @param winner The new winner
	 */
	public SlowMove(SlowMove m,Winner winner){
		this(m.start,m.end,m.piece,m.type,m.capturedPiece,m.numberOfChecks,m.PGNNotation,winner,m.gameover);
	}

	/**
	 * Copys a move with updated game end condition
	 * Created specifically because of one method in the AlgNotationHelper file
	 * @param m     The original SlowMove
	 * @param cause The new cause of game over
	 */
	public SlowMove(SlowMove m,causeOfGameover cause){
		this(m.start,m.end,m.piece,m.type,m.capturedPiece,m.numberOfChecks,m.PGNNotation,m.winner,cause);
	}

	/**
	 * Gets the team from the internal piece char
	 * @return WHITE or BLACK
	 */
	public boolean team(){
		return getTeamFromChar(piece());
	}

	/**
	 * Copy method
	 * @return new SlowMove record
	 */
	public SlowMove copy(){
		return new SlowMove(start,end,piece,type,capturedPiece,numberOfChecks,PGNNotation,winner,gameover);
	}

	/**
	 * Generates a king of queen side castle depending on supplied variables
	 * @param type Should be kingSideCastle or queenSideCastle
	 * @param team Which team? WHITE or BLACK
	 * @return A SlowMove record for the castle event
	 */
	public static SlowMove GenerateCastle(moveTypes type,boolean team){
		Coords kingPos=new Coords(4,0), dest;
		String notation="";
		if(team==BLACK) kingPos.addVector(0,BOARD_SIZE-1);//move to top if BLACK
		dest=new Coords(kingPos);
		if(type==moveTypes.queenSideCastle){
			dest.addVector(-2,0);//left for queen side
			notation="O-O-O";
		}else{
			dest.addVector(2,0);//right for king side
			notation="O-O";
		}
		return new SlowMove(kingPos,dest,setCaseFromTeam('k',team),type,' ',0,notation,Winner.none,causeOfGameover.none);
	}

	/**
	 * Translates this SlowMove into a fast Move integer
	 * @return integer encoded Move data
	 * @see Move
	 */
	public int encodeFASTMove(){
		int specialCode=switch(type){
			case justMove -> Move.normalMove;
			case capture -> Move.capture;
			case pawnPromote -> Move.pawnPromote;
			case enPassant -> Move.EnPassantCapture;
			case kingSideCastle -> Move.kSideCastle;
			case queenSideCastle -> Move.qSideCastle;
		};
		if((piece=='p' && start.getY()==1 && end.getY()==3) || (piece=='P' && start.getY()==6 && end.getY()==4)){//if double move on start
			specialCode=Move.pawnDoubleMove;
		}
		if(type==moveTypes.pawnPromote) return Move.encode(specialCode,PieceCode.encodeChar(capturedPiece),start.getIndex(),end.getIndex());
		return Move.encode(specialCode,PieceCode.encodeChar(piece),start.getIndex(),end.getIndex());
	}
}