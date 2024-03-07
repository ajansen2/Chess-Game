/*
File: PieceCode.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */

package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class to handle all operations related to piece codes, may not be needed for long if everything starts using piece codes and not objects.<br/>
 * This class can't be instantiated, but it has static helper methods
 * @author Dalton Herrewynen
 * @version 1
 * @see Board
 * @see Piece
 */
public abstract class PieceCode{
	/**
	 * Piece codes to make arrays easier to use, Pieces named with color abbreviation,
	 * positions chosen so a bit flip can flip teams, even WHITE, odd BLACK, Blank is <b>ANY</b> invalid number
	 */
	public static final int PawnW=0, PawnB=1, RookW=2, RookB=3, KnightW=4, KnightB=5, BishopW=6, BishopB=7,
			QueenW=8, QueenB=9, KingW=10, KingB=11, PIECE_TYPES=12, Blank=PIECE_TYPES;//Blank is ANY invalid code
	/** Used with the Piece codes to form a sort of table */
	private static final Piece[] PIECE_OBJECT={new Pawn(WHITE),new Pawn(BLACK),new Rook(WHITE),new Rook(BLACK),
			new Knight(WHITE),new Knight(BLACK),new Bishop(WHITE),new Bishop(BLACK),new Queen(WHITE),new Queen(BLACK),
			new King(WHITE),new King(BLACK)};

	/**
	 * Encode the piece type and team into a piece code
	 * @param type The type of Piece
	 * @param team Which color WHITE or BLACK?
	 * @return Integer Piece code
	 */
	public static int encodePieceType(pieceType type,boolean team){
		int code=switch(type){
			case pawn -> PawnW;
			case rook -> RookW;
			case knight -> KnightW;
			case bishop -> BishopW;
			case queen -> QueenW;
			case king -> KingW;
			default -> Blank;
		};
		if(team==WHITE || code==Blank) return code;
		else return code+1;//add 1 to the piece to change from white to black
	}

	/**
	 * Decode the piece type and team into a piece type
	 * @param code The type of Piece
	 * @return Piece type enum
	 */
	public static pieceType decodePieceType(int code){
		pieceType type=switch(code){
			case PawnW, PawnB-> pieceType.pawn;
			case RookW, RookB-> pieceType.rook;
			case KnightW,KnightB -> pieceType.knight;
			case BishopW, BishopB-> pieceType.bishop;
			case QueenW, QueenB-> pieceType.queen;
			case KingW, KingB-> pieceType.king;
			default -> pieceType.blank;
		};
		return type;
	}

	/**
	 * Gets team from the piece code
	 * @param code Piece code
	 * @return WHITE or BLACK
	 */
	public static boolean decodeTeam(int code){
		return (code%2==0)==WHITE;//the ==WHITE makes it type/value agnostic
	}

	/**
	 * Gets the Piece Char from the internal Piece Code
	 * @param code Integer piece code
	 * @return The Char representation
	 */
	public static char decodeChar(int code){
		if(code<0 || code>=PIECE_TYPES) return ' ';//blank if not a piece
		return PIECE_OBJECT[code].getChar();//TODO speed test this against a switch
	}

	public static Piece pieceObj(int code){
		if(code<0 || code>=PIECE_TYPES) return null;
		return PIECE_OBJECT[code];
	}

	/**
	 * Encodes a char into an integer Piece code
	 * @param letter Letter to encode
	 * @return Integer Piece Code
	 */
	public static int encodeChar(char letter){
		boolean team=getTeamFromChar(letter);
		pieceType type=getPieceFromChar(letter);
		return encodePieceType(type,team);
	}

	/**
	 * Gets the string representation without a null pointer exception
	 * @param code The piece code to look up
	 * @return The pretty printed String
	 */
	public static String decodePieceName(int code){
		if(code<0 || code>=PIECE_TYPES) return "Blank";
		return PIECE_OBJECT[code].toString();
	}
}