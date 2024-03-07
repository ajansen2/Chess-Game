/*
File: Board.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static edu.kingsu.SoftwareEngineering.Chess.PieceCode.*;
import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * This is a structure that represents the game board and its current state
 * @author Dalton Herrewynen
 * @version 2
 * @see PieceCode
 */
public class Board{
	/** These are ints to make use of fast Switch statements possible */
	public static final int CHESS960=2, CLEAR=1, DEFAULT=0;
	/** To reduce magic numbers */
	public static final int KingXCoord=4;
	/** Squares where pieces have not yet moved */
	private long unmoved;
	/** Squares with pawns vulnerable to EnPassant */
	private long EnPassant;
	/** Positions of each piece type */
	private long[] pieces=new long[PIECE_TYPES];

	/** This is a base list that Chess960 can use to randomize pieces */
	private static final int[] BACK_RANK_LIST=new int[]{RookW,RookW,BishopW,BishopW,KnightW,KnightW,QueenW,KingW};

	/**
	 * Creates a Board class from another Board.
	 * @param board The board to copy
	 */
	public Board(Board board){
		this.unmoved=board.unmoved;
		this.EnPassant=board.getEnPassant();
		System.arraycopy(board.pieces,0,this.pieces,0,PIECE_TYPES);
	}

	/**
	 * Creates a Board class from a BoardState.
	 * @param state The BoardState to copy
	 */
	public Board(BoardState state){
		this.unmoved=state.unmoved();
		this.EnPassant=state.EnPassant();
		System.arraycopy(state.pieces(),0,this.pieces,0,PIECE_TYPES);
	}

	/** Default constructor creates a default board */
	public Board(){
		this(DEFAULT);
	}

	/**
	 * The constructor which populates the Board by calling populate methods
	 * @param State Clear for blank board, Default for a chess game
	 */
	public Board(int State){
		switch(State){
			case CLEAR:
				populateBlankBoard();
				break;
			case CHESS960:
				Chess960Setup();
				break;
			case DEFAULT:
			default:
				populateGameBoard();
				break;
		}
	}

	/** Blanks out the board */
	private void populateBlankBoard(){
		unmoved=0;
		EnPassant=0;
		Arrays.fill(pieces,0);
	}

	/** Set up a default board with pieces ready for a normal game */
	private void populateGameBoard(){
		populateBlankBoard();//blank out the board first
		setSquare(RookW,Coords.XYToIndex(0,0));
		setSquare(KnightW,Coords.XYToIndex(1,0));
		setSquare(BishopW,Coords.XYToIndex(2,0));
		setSquare(QueenW,Coords.XYToIndex(3,0));
		setSquare(KingW,Coords.XYToIndex(4,0));
		setSquare(BishopW,Coords.XYToIndex(5,0));
		setSquare(KnightW,Coords.XYToIndex(6,0));
		setSquare(RookW,Coords.XYToIndex(7,0));
		for(int i=0; i<BOARD_SIZE; ++i){
			setSquare(PawnW,Coords.XYToIndex(i,1));
			setSquare(PawnB,Coords.XYToIndex(i,6));
		}
		setSquare(RookB,Coords.XYToIndex(0,7));
		setSquare(KnightB,Coords.XYToIndex(1,7));
		setSquare(BishopB,Coords.XYToIndex(2,7));
		setSquare(QueenB,Coords.XYToIndex(3,7));
		setSquare(KingB,Coords.XYToIndex(4,7));
		setSquare(BishopB,Coords.XYToIndex(5,7));
		setSquare(KnightB,Coords.XYToIndex(6,7));
		setSquare(RookB,Coords.XYToIndex(7,7));
		setAllNotMoved();//mark all pieces as not moved
	}

	/** Sets up the board for Chess960 a.k.a. Fisher-random Chess */
	private void Chess960Setup(){
		populateBlankBoard();//blank it out first
		Random random=new Random();

		//Pawn layout is the same as normal
		for(int i=0; i<BOARD_SIZE; ++i){
			setSquare(PawnW,Coords.XYToIndex(i,1));
			setSquare(PawnB,Coords.XYToIndex(i,6));
		}

		//We can call it here to make the king unable to castle (not entirely accurate but probably way easier to program)
		setAllNotMoved();

		int[] backOrdering=ChessOrdering960(random);

		for(int i=0; i<BOARD_SIZE; ++i){
			//Placing white pieces
			setSquare(backOrdering[i],Coords.XYToIndex(i,0));
		}
		for(int i=0; i<BOARD_SIZE; ++i){
			//Placing black pieces (offset piece code by 1)
			setSquare(backOrdering[i]+1,Coords.XYToIndex(i,7));
		}
	}

	/**
	 * Returns a random ordering for the back rank pieces
	 * @param r A random object
	 * @return The random ordering
	 */
	private int[] ChessOrdering960(Random r){
		//TODO I'll leave this for Dalton because he's probably got a trick up his sleeve to make this work nice
		int[] temp=new int[BOARD_SIZE];
		//Initializing array
		System.arraycopy(BACK_RANK_LIST,0,temp,0,BOARD_SIZE);
		do{//We have to shuffle at least once
			for(int i=BOARD_SIZE-1; i>0; --i){


				//Finding random piece to swap
				int randIndex=r.nextInt(i);
				int tempPiece=temp[i];

				//Swapping pieces
				temp[i]=temp[randIndex];
				temp[randIndex]=tempPiece;
			}
		}while(!isChess960Valid(temp));
		//Technically, the king also has to be between the rooks and there's a special way to castle,
		//but that will take WAY too much time to make. Just make the king unable to castle
		return temp;
	}

	/**
	 * Returns whether this Chess960 ordering is valid. Bishops must be on opposing diagonals
	 * @param pieceOrder The ordering
	 * @return Whether the ordering is valid
	 */
	private boolean isChess960Valid(int[] pieceOrder){
		int bishopIndex=-1;

		for(int i=0; i<BOARD_SIZE; ++i){
			//Checking if bishops are on opposite diagonals
			if(pieceOrder[i]==BishopW){
				if(bishopIndex==-1) bishopIndex=i;
				else return i%2!=bishopIndex%2;
			}
		}
		return false;
	}

	/** Sets all pieces on the board to think they've not been moved yet */
	public void setAllNotMoved(){
		unmoved=0;
		EnPassant=0;
		for(int i=0; i<PIECE_TYPES; ++i) unmoved|=pieces[i];//any piece will write a 1 to the has not moved place
	}

	/**
	 * Forces the given square(s) to be marked as not moved
	 * @param mask The square or squares to mark as not moved
	 */
	public void setHasNotMoved(long mask){
		unmoved|=mask;//if either has any square, flip to a 1 and mark not moved
		EnPassant=EnPassant & ~mask;//any bits in the mask should cancel the EnPassant
	}

	/**
	 * Gets all squares that have not been moved yet and are not blank
	 * @return long (64bit integer) bit mask
	 */
	public long getUnmoved(){
		long result=unmoved;
		for(int i=0; i<PIECE_TYPES; ++i){
			result&=pieces[i];
		}
		return result;
	}

	/**
	 * Checks if a piece has moved or not by its square index
	 * @param index The square to check
	 * @return True if square has piece that has not been moved, False otherwise.
	 */
	public boolean hasNotMoved(int index){
		return (unmoved & indexToMask(index))!=0;//use masking to check any square
	}

	/**
	 * Checks if a piece has moved or not by its coordinate object
	 * Wrapper for the <code>hasNotMoved(int index)</code> method
	 * @param coord The square position
	 * @return True if square has piece that has not been moved, False otherwise.
	 */
	public boolean hasNotMoved(Coords coord){
		return hasNotMoved(coord.getIndex());
	}

	/**
	 * Sets square(s) to a given piece and makes the piece(s) marked as moved
	 * @param code The piece code to set
	 * @param mask The mask representing all the squares to set
	 */
	public void setSquare(int code,long mask){
		mask=~mask;//make backwards for deletion of bits
		unmoved=unmoved & mask;
		EnPassant=0;//any move will cancel the EnPassant vulnerability
		for(int i=0; i<PIECE_TYPES; ++i){
			pieces[i]=pieces[i] & mask;//blank out the squares in the mask
		}
		if(code>=0 && code<PIECE_TYPES) pieces[code]|=~mask;//set this square to this code if the code is valid
	}

	/**
	 * Sets a square based to a piece code, makes the piece think it's been moved
	 * @param code  Which piece based on the code
	 * @param index Which square
	 */
	public void setSquare(int code,int index){
		setSquare(code,indexToMask(index));//jump straight to this one square
	}

	/**
	 * Sets a square based on the x and y coordinate values
	 * Calls the masked <code>setSquare(long mask)</code> method internally
	 * @param code The piece code to set
	 * @param x    X Coordinate
	 * @param y    Y Coordinate
	 */
	public void setSquare(int code,int x,int y){
		setSquare(code,Coords.XYToIndex(x,y));
	}

	/**
	 * Sets a square from a code and a coordinate object
	 * Calls the masked <code>setSquare(long mask)</code> method internally
	 * @param code   The piece code to set
	 * @param square The square to set this piece on
	 */
	public void setSquare(int code,Coords square){
		setSquare(code,square.getMask());
	}

	/**
	 * Sets a square from a code and a coordinate object
	 * Calls the masked <code>setSquare(long mask)</code> method internally
	 * @param piece  The piece object to set
	 * @param square The square to set this piece on
	 */
	public void setSquare(Piece piece,Coords square){
		setSquare(piece.pieceCode,square.getMask());
	}

	/**
	 * Get the code of the piece at a given square
	 * Because of bit masking it is possible to check several squares at once but not useful to us (yet)
	 * @param mask bitmask for the square to get
	 * @return Integer piece code
	 */
	public int getSquare(long mask){
		for(int i=0; i<PIECE_TYPES; ++i){
			if((pieces[i] & mask)!=0) return i;//see why I used final ints now
		}
		return Blank;
	}

	/**
	 * Gets the code of the piece at a given square
	 * Just a wrapper for <code>getSquare(long mask)</code>
	 * @param index the square to check
	 * @return Integer piece code
	 */
	public int getSquare(int index){
		return getSquare(indexToMask(index));
	}

	/**
	 * Gets the code of the piece at a given square
	 * Just a wrapper for <code>getSquare(long mask)</code>
	 * @param x X Coordinate
	 * @param y Y Coordinate
	 * @return Integer piece code
	 */
	public int getSquare(int x,int y){
		return getSquare(Coords.XYToIndex(x,y));
	}

	/**
	 * Gets the code of the piece at a given square
	 * Just a wrapper for <code>getSquare(long mask)</code>
	 * @param coord the square to check
	 * @return Integer Piece Code
	 */
	public int getSquare(Coords coord){
		return getSquare(coord.getMask());
	}

	/**
	 * Masks out which squares are vulnerable to en-passant rule, basic dumb getter
	 * @return 64 bit mask
	 */
	public long getEnPassant(){
		return EnPassant;
	}

	/**
	 * Sets squares as vulnerable to EnPassant
	 * @param mask The 64 bit mask of squares to set
	 */
	public void setEnPassant(long mask){
		EnPassant=mask;//override EnPassant mask with new mask
	}

	/**
	 * Gets a piece object reference at a given square
	 * Just a wrapper for <code>getSquare(long mask)</code>
	 * @param coord the square to check
	 * @return reference to a Piece Object
	 * @deprecated Moving to bit masking where possible for speed reasons
	 */
	@Deprecated
	public Piece getSquareObj(Coords coord){
		return PieceCode.pieceObj(getSquare(coord));
	}

	/**
	 * Gets a piece object reference at a given square
	 * Just a wrapper for <code>getSquare(long mask)</code>
	 * @param index index of the square to check
	 * @return reference to a Piece Object
	 * @deprecated Moving to bit masking where possible for speed reasons
	 */
	@Deprecated
	public Piece getSquareObj(int index){
		return PieceCode.pieceObj(getSquare(index));
	}

	/**
	 * Returns a bitmask of all the pieces that are on the board based on their code
	 * @param pieceCode The piece code to get
	 * @return long (64bit integer) bit mask
	 * @see PieceCode
	 */
	public long searchPiece(int pieceCode){
		return pieces[pieceCode];
	}

	/**
	 * Rotates the current Board object 180 degrees
	 * @return A copy of the Board object that is rotated 180 degrees
	 */
	public Board getRotatedBoard(){
		Board rotatedBoard=new Board(CLEAR);
		for(int i=0; i<PIECE_TYPES; ++i){//for each piece
			rotatedBoard.pieces[i]=reverseMask(pieces[i]);
		}
		rotatedBoard.unmoved=reverseMask(unmoved);
		return rotatedBoard;
	}

	/** Rotates the current Board object 180 degrees */
	public void rotateBoard(){
		for(int i=0; i<PIECE_TYPES; ++i){//for each piece
			pieces[i]=reverseMask(pieces[i]);
		}
		unmoved=reverseMask(unmoved);
	}

	/**
	 * Applies a move to the board, this version uses a move encoded as a single integer value (sorry Andrew, speed matters here)
	 * @param move The move data encoded into a single integer
	 * @see Move
	 */
	public void makeMove(int move){
		switch(Move.getSpecialCode(move)){//calculate what type of move this is
			case Move.blankMove://if a blank move tag is detected
				break;//do nothing, ignore blank moves
			case Move.kSideCastle://king side castling
				kingSideCastle(move);
				break;
			case Move.qSideCastle://queen side castling
				queenSideCastle(move);
				break;
			case Move.pawnDoubleMove://for double move, make the move, then set the en passant vulnerability
				setSquare(Blank,Move.getStartIndex(move));//blank out where we were
				setSquare(Move.getPieceCode(move),Move.getEndIndex(move));//move
				setEnPassant(indexToMask(Move.getEndIndex(move)));//record vulnerability
				break;
			case Move.EnPassantCapture://delete the square behind the pawn that moved because it captures that square
				setSquare(Blank,EnPassant | Move.getStartMask(move));//Delete the only piece that was vulnerable to EnPassant, and the pawn that does the capturing
				setSquare(Move.getPieceCode(move),Move.getEndIndex(move));//move the capturing pawn
				break;
			default://pawn promotion, capture, or normal move
				setSquare(Blank,Move.getStartIndex(move));//blank the square
				setSquare(Move.getPieceCode(move),Move.getEndIndex(move));//set to destination to the encoded piece code (pawn promotion will have other piece, normal moves have same piece)
		}
	}

	/**
	 * Handles the king side castling for both teams
	 * @param move the encoded move integer
	 */
	private void kingSideCastle(int move){//TODO change to using just bit masking
		int y=Coords.indexToY(Move.getStartIndex(move));
		if(y==0){//if on WHITE side of board
			setSquare(KingW,6,0);
			setSquare(RookW,5,0);
		}else{//if on BLACK side of board
			setSquare(KingB,6,7);
			setSquare(RookB,5,7);
		}
		setSquare(Blank,4,y);
		setSquare(Blank,7,y);
	}

	/**
	 * Handles the queen side castling for both teams
	 * @param move the encoded move integer
	 */
	private void queenSideCastle(int move){//TODO change to using just bit masking
		int y=Coords.indexToY(Move.getStartIndex(move));
		if(y==0){//if on WHITE side of board
			setSquare(KingW,2,0);
			setSquare(RookW,3,0);
		}else{//if on BLACK side of board
			setSquare(KingB,2,7);
			setSquare(RookB,3,7);
		}
		setSquare(Blank,4,y);
		setSquare(Blank,0,y);
	}

	/**
	 * Saves this board to state to a BoardState Structure
	 * @return The state of the Board as a BoardState
	 */
	public BoardState saveState(){
		return new BoardState(unmoved,EnPassant,Arrays.copyOf(pieces,PIECE_TYPES));
	}

	/**
	 * Loads the board state into this Board
	 * @param state The state of the Board as a BoardState
	 */
	public void loadState(BoardState state){
		unmoved=state.unmoved();
		EnPassant=state.EnPassant();
		pieces=Arrays.copyOf(state.pieces(),PIECE_TYPES);
	}

	/**
	 * Loads the board state into this Board
	 * @param state The state of the Board as a BoardState
	 */
	public void loadState(Board state){
		unmoved=state.unmoved;
		EnPassant=state.EnPassant;
		pieces=Arrays.copyOf(state.pieces,PIECE_TYPES);
	}

	/**
	 * Checks if a square is on the board
	 * @param x The row number
	 * @param y The column number
	 * @return True if square exists, False if not
	 */
	public static boolean isValidSquare(int x,int y){
		return Coords.isCoordValid(x,y);
	}

	/**
	 * Generates a mask for filtering one square at a time
	 * @param index which square to filter
	 * @return a long which is a mask with a single 1 in it
	 */
	public static long indexToMask(int index){
		return 1L << index;
	}

	/**
	 * Searches the board for a King of the given color WHITE or BLACK
	 * @param team Which king? WHITE or BLACK
	 * @return a Coords Object with the position of the King or UNSET if no King found
	 * @deprecated Please use the getPiecesSquares method for speed reasons
	 */
	@Deprecated
	public Coords getKingPosObj(boolean team){
		int kingCode;
		if(team==WHITE) kingCode=KingW;
		else kingCode=KingB;
		for(int i=0; i<TOTAL_SQUARES; ++i){
			if((pieces[kingCode] & indexToMask(i))!=0) return new Coords(i);//found the king
		}
		return new Coords();
	}

	/**
	 * Gets the mask of all squares with allied pieces on them
	 * @param team Which team to check
	 * @return long (64-bit integer) used as a bit mask
	 */
	public long alliedPieceMask(boolean team){
		long posMask=0;
		int i=(team==WHITE) ? 0 : 1;//skip one if BLACK
		for(; i<PIECE_TYPES; i+=2){//move up by 2 so we skip enemy pieces
			posMask|=pieces[i];//bitwise or will add all allied pieces masks
		}
		return posMask;
	}

	/**
	 * Returns all allied pieces, will need to remove or change later
	 * @param team Which team to check for
	 * @return ArrayList of Pieces and Coords
	 * @deprecated To be replaced with bit masking
	 */
	@Deprecated
	public ArrayList<PiecePosPair> alliedPiecesObj(boolean team){
		ArrayList<PiecePosPair> list=new ArrayList<>();
		for(int i=0; i<TOTAL_SQUARES; ++i){
			Piece piece=getSquareObj(i);
			if(piece!=null && piece.getTeam()==team){
				list.add(new PiecePosPair(getSquareObj(i),new Coords(i)));
			}
		}
		return list;
	}

	/**
	 * Returns all allied pieces, will need to remove or change later
	 * @param team       Which team to check for
	 * @param pieceType_ An additional filter for piece type
	 * @return ArrayList of Pieces and Coords
	 * @deprecated To be replaced with bit masking
	 */
	@Deprecated
	public ArrayList<PiecePosPair> alliedPiecesObj(boolean team,pieceType pieceType_){
		ArrayList<PiecePosPair> list=new ArrayList<>();
		for(int i=0; i<TOTAL_SQUARES; ++i){
			Piece piece=getSquareObj(i);
			if(piece!=null && piece.getTeam()==team && piece.getType()==pieceType_){
				list.add(new PiecePosPair(getSquareObj(i),new Coords(i)));
			}
		}
		return list;
	}

	/**
	 * Converts the board to ASCII art
	 * @return String of ASCII art
	 * @author Dalton Herrewynen
	 */
	public String toString(){
		StringBuilder output=new StringBuilder();
		output.append("   Black Side       Unmoved Pieces"//Header
				+"   EnPassant: "+Coords.orderedPair(Coords.maskToIndex(EnPassant))//En Passant Square
				+"\n=================<>=================");//top bar
		for(int y=0; y<BOARD_SIZE; ++y){
			output.append("\n|");//start the line with a bar
			for(int x=0; x<BOARD_SIZE; ++x){
				int piece=getSquare(x,Coords.reverseAxis(y));//reverse y-axis because board is stored bottom left origin but prints top left origin
				if(piece!=Blank){//print the piece's internal char representation
					output.append(PieceCode.decodeChar(piece)).append("|");
				}else{//blank squares are blank
					switch((y+x)%2){//using number trickery to generate a checkerboard pattern
						case 1://white squares
							output.append(" |");
							break;
						case 0://black squares
							output.append(".|");
							break;
					}
				}
			}
			output.append("<>|");
			for(int x=0; x<BOARD_SIZE; ++x){//print which squares are unmoved
				if(0!=(unmoved & (1L << Coords.XYToIndex(x,y)))) output.append("+|");//unmoved are marked with a thing
				else output.append(" |");
			}
			output.append("\n=================<>=================");//middle and bottom bars
		}
		output.append("\n   White Side       Unmoved Pieces");
		return output.toString();
	}
}
