/*
File: Move.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * <p>Static methods to handle the encoding and decoding of moves using our faster, more efficient methods.
 * Makes code readable while using bit tricks for speed.</p>
 * <p>Our system uses a single integer which encodes an integer index where the piece starts, ends, and some bits to
 * encode if this is a special move like pawn promotion. Castling is encoded as moving the king 2 spaces left or right.</p>
 * There is no error checking, if you supply bad data, undefined behavior can happen. Error checking is too expensive for the AI code
 * Structure: 4Bits{PieceCode} 6Bits{EndPosition} 6Bits{StartPosition}<br/>
 * Special Code bits: 5{EnPassant} 4{Pawn Promotion} 3{Castling} 2{Capture} 1{Move or direction}<br/>
 * @author Dalton Herrewynen
 * @version 2
 */
public abstract class Move{//class can't be instantiated, but it has static helper classes
	/** Mask constants, these can be altered if the program suddenly gets a larger board */
	public static final int
			SPECIAL_MASK=0b00000000111111110000000000000000,
			CODE_MASK=0b00000000000000001111000000000000,//Converting these to 32 bits because JVM should handle native ints with one fewer operations than shorts
			END_MASK=0b00000000000000000000111111000000,
			START_MASK=0b00000000000000000000000000111111;
	/** Constants to record how many bits are used for moves, and piece code, and special codes */
	public static final int positionBits=6, pieceCodeBits=4;
	/** Code for a blank or unset Move */
	public static final int blankMove=0;//other codes are strategically placed for masking,
	/** Code for the default promotion, normal move or capture (all the same right now, may change later) */
	public static final int normalMove=0b00001, capture=0b00010;//encode move and capture bits
	/** Code for either a Queen or King side castle move */
	public static final int qSideCastle=0b00100, kSideCastle=0b00101;//castling has a special bit and then add one to tell you which side
	/** Code for promotions (Chris needs this for PGN stuff */
	public static final int pawnPromote=0b01000;//flag the promotion bit, and either the move or capture bit to tell me which one it is
	/** Code for when a pawn En-Passant captures another pawn */
	public static final int EnPassantCapture=0b10010;//flag the EnPassant bit and the capture bit
	/** Code for when a pawn moves 2 spaces on its first move */
	public static final int pawnDoubleMove=0b10001;//flag the EnPassant bit and the move bit

	/**
	 * Extracts a starting position bit mask from an encoded move
	 * @param move The integer which encodes a move
	 * @return The bitmask with the square where the piece started
	 */
	public static long getStartMask(int move){
		move&=START_MASK;//mask out JUST the bits I want for starting
		return Coords.indexToMask(move);//no need to shift, already have the bits I want
	}

	/**
	 * Extracts the ending position bit mask from an encoded move
	 * @param move The integer which encodes a move
	 * @return The bitmask with the square where the piece ended
	 */
	public static long getEndMask(int move){
		move&=END_MASK;//mask out JUST the bits I want for ending
		move=(move >>> positionBits);//shift away the starting move
		return Coords.indexToMask(move);
	}

	/**
	 * Extracts a starting position bit mask from an encoded move
	 * @param move The long which encodes a move
	 * @return The square where the piece started
	 */
	public static int getStartIndex(int move){
		return move & START_MASK;//mask out JUST the bits I want for starting
	}

	/**
	 * Extracts the ending position bit mask from an encoded move
	 * @param move The integer which encodes a move
	 * @return The square where the piece ended
	 */
	public static int getEndIndex(int move){
		move&=END_MASK;//mask out JUST the bits I want for ending
		return move >>> positionBits;//shift over so the result LSB lines up with the index LSB
	}

	/**
	 * Extracts the extra data encoding a piece code, pawn promotions will have a piece code other than the pawn which
	 * made the move. Ignore this for non-pawns, expected behavior for non-pawns is to return the blank code.
	 * @param move The integer which encodes a move
	 * @return An integer piece code
	 */
	public static int getPieceCode(int move){
		int code=move & CODE_MASK;//mask out the piece code bits
		return (code >>> (2*positionBits));//Shift the code to the LSB position, JVM *SHOULD* convert this into a constant for **SPEED**
	}

	/**
	 * Decodes the special move code content, things like castling are encoded here, please match the code to the constants
	 * defined in this class.
	 * @param move the encoded integer
	 * @return The special code integer
	 */
	public static int getSpecialCode(int move){
		int code=move & SPECIAL_MASK;
		return (code >>> (2*positionBits+pieceCodeBits));
	}

	/**
	 * Generates an encoded normal move Long from the given data, encodes moves, captures, and promotion
	 * For castling the starting coordinate must match the king that can castle and the special code must be a castling code
	 * @param special   If not a normal move, encode which one (like castling)
	 * @param pieceCode The code of the piece to place on the end square, pawn promotion sets a new piece code
	 * @param start     The square to start from
	 * @param end       The square to end on
	 * @return An integer which encodes a move with minimal space and time to decode
	 */
	public static int encode(int special,int pieceCode,int start,int end){
		special=special << (positionBits*2+pieceCodeBits);//make room for piece code, start, and end squares
		pieceCode=pieceCode << (positionBits*2);//shift over by 2 moves, make room for start and end moves
		end=end << positionBits;//shift over by 1 move, make room for start move
		return special | start | end | pieceCode;//combine them into one single integer with bitwise or operations
	}

	/**
	 * Shorthand to generate a basic move, promotion, or capture that is not a castle move
	 * Internally calls <code>Move.encode()</code> with normal move code
	 * @param pieceCode The code of the piece to place on the end square, pawn promotion sets a new piece code
	 * @param start     The square to start from
	 * @param end       The square to end on
	 * @return An integer which encodes a move with minimal space and time to decode
	 */
	public static int encodeNormal(int pieceCode,int start,int end){
		return encode(normalMove,pieceCode,start,end);
	}

	/**
	 * Generates a castling move from just the team and the castling code
	 * @param castleCode Valid options are the kSideCastle or qSideCastle
	 * @param team       WHITE or BLACK
	 * @return A move encoded as an integer, or a blank move if there was a failure
	 */
	public static int encodeCastle(int castleCode,boolean team){
		int posY, kingCode;
		if(team==WHITE){
			posY=0;
			kingCode=PieceCode.KingW;
		}else{
			posY=BOARD_SIZE-1;//move everything up to top of board if BLACK
			kingCode=PieceCode.KingB;
		}
		return switch(castleCode){
			case qSideCastle ->
					encode(qSideCastle,kingCode,Coords.XYToIndex(Board.KingXCoord,posY),Coords.XYToIndex(Board.KingXCoord-2,posY));//king position and the rook at far left of board, top or bottom
			case kSideCastle ->
					encode(kSideCastle,kingCode,Coords.XYToIndex(Board.KingXCoord,posY),Coords.XYToIndex(Board.KingXCoord+2,posY));//king position and the rook at far right of board, top or bottom
			default -> blank();//if a fault, flag blank
		};
	}

	/**
	 * Generates a blank or unset move which has the blank special code
	 * Methods that handle moves should not apply moves with the blank flag as they are blank
	 * @return a zeroed out move with the blankMove tag
	 */
	public static int blank(){
		return encode(blankMove,0,0,0);
	}

	/**
	 * Extracts and converts the encoded piece code into a pretty printed piece name (wrote this too many times in Junit)
	 * @param move the move to decode
	 * @return Pretty printed piece name
	 */
	public static String getPieceName(int move){
		return PieceCode.decodePieceName(getPieceCode(move));
	}

	/**
	 * Checks if this is a capture move
	 * @param move The move integer to check
	 * @return True if capture bit is set, False otherwise
	 */
	public static boolean isCapture(int move){
		return 0!=(getSpecialCode(move) & capture);//yoda code that masks out the capture bit for any capture type
	}

	/**
	 * Checks if this is a pawn promotion move (capture or straight move)
	 * @param move The move integer to check
	 * @return True if this is a pawn promotion, False otherwise
	 */
	public static boolean isPawnPromotion(int move){
		return 0!=(getSpecialCode(move) & pawnPromote);//yoda code that masks out any and all pawn promotions (both move and capture)
	}

	/**
	 * Shorthand for checking if a move is blank or if it's valid
	 * @param move The encoded move integer to check
	 * @return True if a blank move, False if a move was encoded
	 */
	public static boolean isBlank(int move){
		return getSpecialCode(move)==blankMove;
	}

	/**
	 * Prints human-readable info about the move
	 * @param move the move to decode
	 * @return a String with all the encoded information in human-readable form
	 */
	public static String describe(int move){//TODO update to reflect bit masking search
		String res="Move{Special: ";
		res+=switch(getSpecialCode(move)){
			case blankMove -> "Blank Move";
			case kSideCastle -> "King Side Castle";
			case qSideCastle -> "Queen Side Castle";
			case EnPassantCapture -> "EnPassant";
			case pawnDoubleMove -> "Pawn double move on start";
			case capture -> "capture";
			default -> "Normal Move";
		};
		res+=", ";
		res+="Piece: "+PieceCode.decodePieceName(getPieceCode(move))+", ";
		res+="Start"+new Coords(getStartIndex(move))+", ";
		res+="End"+new Coords(getEndIndex(move))+"}";
		return res;
	}
}
