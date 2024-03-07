/*
File: Types.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

/**
 * Class that holds all types and enums that are globally used.<br/>
 * This class can't be instantiated, but it has static helper methods
 * @author Dalton Herrewynen
 * @version 1
 */
public abstract class Types{//class can't be instantiated, but it has static helper methods
	/** The teams (or colors) that the pieces can belong to */
	public static final boolean WHITE=true, BLACK=false;// white is lowercase
	/** Shorthands I am sure will help readability later */
	public static final boolean SUCCESS=true, FAIL=false, SET=true, UNSET=false;
	/** Constants in used to clarify the size of the board */
	public static final int BOARD_SIZE=8, TOTAL_SQUARES=64;

	/** The types of move, special moves or just regular move */
	public enum moveTypes{
		justMove,capture,enPassant,kingSideCastle,queenSideCastle,pawnPromote
	}

	/** Stores whether the game is over and why the game ended */
	public enum causeOfGameover{
		none,checkmate,stalemate,repetition,forfeit
	}

	/** Which player (if any) is the winner if this is end of game */
	public enum Winner{
		none,white,black
	}

	/** List of all the piece types - used to accelerate switches */
	public enum pieceType{
		pawn,rook,knight,bishop,queen,king,blank;
		@Override
		public String toString(){
			//convert the name to lowercase and then capitalize the first letter(We should've been consistent with naming the pieces)
			return name().substring(0,1).toUpperCase()+name().substring(1).toLowerCase();
		}
	}

	/**
	 * Gets what type of piece object is in a position, does not cause Null-Pointer exceptions
	 * @param piece The Object to check
	 * @return The char code of the piece
	 */
	public static char getCharFromPieceObject(Piece piece){
		if(piece==null) return ' ';// nulls are blank
		return piece.getChar();// uses my other switches anyhow
	}

	/**
	 * Converts a piece type and team into a char representation
	 * @param type The type of piece
	 * @param team The team of this piece
	 * @return A char value for that piece
	 */
	public static char getCharFromPieceType(pieceType type,boolean team){
		char piece=switch(type){
			case bishop -> 'b';
			case king -> 'k';
			case knight -> 'n';
			case pawn -> 'p';
			case queen -> 'q';
			case rook -> 'r';
			case blank -> ' ';
		};
		return setCaseFromTeam(piece,team);
	}

	/**
	 * Converts a char into a pieceType
	 * @param piece The type of piece
	 * @return pieceType value for that char
	 */
	public static pieceType getPieceFromChar(char piece){
		pieceType type=switch(piece){// probably faster than converting cases with setCaseFromTeam()
			case 'b','B' -> pieceType.bishop;
			case 'k','K' -> pieceType.king;
			case 'n','N' -> pieceType.knight;
			case 'p','P' -> pieceType.pawn;
			case 'q','Q' -> pieceType.queen;
			case 'r','R' -> pieceType.rook;
			default -> pieceType.blank;// blank is the default
		};
		return type;
	}

	/**
	 * Sets the capitalization of the char to match the case for that team
	 * @param piece The char representation for the piece
	 * @param team  The team of the piece
	 * @return WHITE is lowercase, BLACK is uppercase
	 */
	public static char setCaseFromTeam(char piece,boolean team){
		piece=(char) (piece | 0b0000000000100000);//bit tricks to get a lowercase value, ASCII was designed well
		if(team==BLACK && piece!=' ') piece=(char) (piece & 0b0000000001011111);//if BLACK and not a blank space, go to uppercase
		//WHY DOES JAVA NEED 16 BITS PER CHAR???????????
		return piece;//This runs WAY FASTER than if statements I think.
	}

	/**
	 * Returns the team from a piece character
	 * @param piece The character representation of the piece
	 * @return WHITE if lowercase, BLACK if uppercase
	 */
	public static boolean getTeamFromChar(char piece){
		return (piece>='a' && piece<='z');
	}

	/**
	 * Gets a human-friendly string of the Team
	 * @param team The Team of the piece
	 * @return String representing the Team
	 */
	public static String getTeamString(boolean team){
		if(team==WHITE) return "White";
		return "Black";
	}

	/**
	 * Forces a char to uppercase using bitwise masking
	 * @param c To uppercase
	 * @return upper case version of c
	 */
	public static char charUppercase(char c){
		return (char) (c & 0b0000000001011111);
	}

	/**
	 * Checks if a line is empty (vertical or horizontal) between two points, not inclusive
	 * @param board The current board state
	 * @param start Starting coordinate
	 * @param end   Ending coordinate
	 * @return True if no pieces between those points, False if a piece is there
	 */
	public static boolean isLineEmpty(Board board,Coords start,Coords end){
		if(!start.getSet() || !end.getSet()) throw new RuntimeException("One of the coordinates has not been set");
		int dirX=0, dirY=0;//the direction to check
		Coords pos=new Coords(start);
		if(start.getY()==end.getY()){// if same y, check horizontal
			if(start.getX()<end.getX()) dirX=1;// move right
			else dirX=-1;// if end is left of start, move left
		}else{// for now assume that this means vertical line check
			if(start.getY()<end.getY()) dirY=1;// move up
			else dirY=-1;// if end is lower than start, move down
		}
		pos.addVector(dirX,dirY);// move ahead one space before checking to avoid including this square
		while(pos.getIndex()!=end.getIndex()){// loop until coordinates are the same
			if(board.getSquare(pos.getIndex())!=PieceCode.Blank) return false;// if there is a piece, return false
			pos.addVector(dirX,dirY);
		}
		return true;// if I get here then the check did not fail, return true
	}

	/**
	 * Unoptimized method to convert a long to its binary string
	 * @param mask The long to check
	 * @return A string on 0's and 1's
	 */
	public static String maskString(long mask){
		StringBuilder representation=new StringBuilder();
		for(int i=63; i>=0; --i){
			representation.append(((mask & (1L << i))==0) ? '0' : '1');// shift and test for equality with 0
		}
		return representation.toString();
	}

	/**
	 * Reverses a mask from front to back. Useful for rotations
	 * @param mask The mask to reverse
	 * @return Reversed version of mask
	 */
	public static long reverseMask(long mask){
		long rotated=0;
		for(int i=0; i<64; ++i){// always 64 bits so magic number is OK here
			rotated=rotated << 1;// move it left to expose a new right most bit
			rotated|=(mask >>> i) & 1L;// put the Ith bit on the right side of the new mask
		} // TODO speed test the >>> and >> operator
		return rotated;
	}
}