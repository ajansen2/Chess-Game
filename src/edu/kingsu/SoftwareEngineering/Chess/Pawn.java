/*
File: Pawn.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import java.util.ArrayList;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class for the Pawn piece
 * @author Dalton Herrewynen
 * @version 3
 * @see Piece
 */
public class Pawn extends Piece{
	/** Bit masks used for fast detection of pawns that can be promoted */
	public static long
			WHITE_Promotion_mask=0b0000000011111111000000000000000000000000000000000000000000000000L,
			BLACK_Promotion_mask=0b0000000000000000000000000000000000000000000000001111111100000000L;

	/**
	 * Constructs the Pawn and sets the team (color, team, etc.)
	 * @param team The Team Enum containing one of the possible teams
	 */
	public Pawn(boolean team){
		super(team,pieceType.pawn);
	}

	/**
	 * Converts the Pawn to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	@Override
	public String toString(){
		return getPieceTeamString()+" Pawn";
	}

	/**
	 * Calculates the Pawn's value to the AI player
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return relative value to the AI
	 */
	@Override
	public int pieceValue(Board board,int position){//TODO speed test against a matrix
		// after 5th file pawn values should increment by 1.5 imo. double pawns in same col should worth less, especially the blocked pawn 
		int score=100, xPos=Coords.indexToX(position), yPos=Coords.indexToY(position);

		if(board.getSquare(Coords.XYToIndex(Coords.indexToX(position),Coords.indexToY(position)+moveYDirection(team)))!=Move.blank()){
			//This is triggered when there's a piece ahead of us. We're much less valuable when we're being blocked
			score-=65;
		}

		if(xPos==3 || xPos==4) score+=50;//Center pawns are worth more than side pawns

		if(team==WHITE && yPos>=4){//save 1 conditional
			int advancePawnScore=yPos-3;
			score+=advancePawnScore*60;
		}else if(yPos<=3){
			int advancePawnScore=4-yPos;
			score+=advancePawnScore*60;
		}
		return score;
	}

	/**
	 * Returns 1 if WHITE and -1 if BLACK
	 * @param team The team of the pawn
	 * @return -1 or +1
	 */
	private int moveYDirection(boolean team){
		return (team==WHITE) ? 1 : -1;//move forward if WHITE, backwards if BLACK
	}

	/**
	 * Returns all legal moves that this Pawn is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of SlowMove objects that are legal
	 */
	@Override
	public ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position){
		if(!position.isSet()) return new ArrayList<>();

		ArrayList<SlowMove> legalMoves=new ArrayList<>();
		ArrayList<SlowMove> moveHolder=new ArrayList<>();
		Coords endPoint;
		int offsetY=1;
		boolean canMoveOnce=false;
		if(getTeam()==BLACK) offsetY=-offsetY;//make Pawn go downwards if BLACK
		//Forward moves
		endPoint=position.getShiftedCoord(0,offsetY);
		if(endPoint.isSet() && board.getSquare(endPoint)==PieceCode.Blank){//check 1 space forwards
			canMoveOnce=true;
			//legalMoves.add(new SlowMove(position,endPoint,getChar()));//add a just move if the end point is empty
			addPawnMove(board,legalMoves,position,endPoint,getChar(),moveTypes.justMove,' ');

		}
		if(board.hasNotMoved(position) && canMoveOnce){//if first move
			endPoint.addVector(0,offsetY);//check 2 squares ahead
			if(endPoint.isSet() && board.getSquare(endPoint)==PieceCode.Blank){
				legalMoves.add(new SlowMove(position,endPoint,getChar()));//add a just move if the end point is empty
			}
		}

		//Diagonal captures
		moveHolder.add(pieceSingleMoveCheckObj(board,position,-1,1));
		moveHolder.add(pieceSingleMoveCheckObj(board,position,1,1));
		for(SlowMove m: moveHolder){//copy all capture moves
			if(m!=null && m.type()==moveTypes.capture){
				//legalMoves.add(m);
				addPawnMove(board,legalMoves,m.start(),m.end(),m.piece(),m.type(),m.capturedPiece());

			}
		}
		return legalMoves;
	}

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method for Pawns
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	@Override
	public ArrayList<Integer> getLegalMoves(Board board,int position){
		ArrayList<Integer> moves=new ArrayList<>();
		int dy=(team==WHITE) ? 1 : -1;//move forward if WHITE, backwards if BLACK
		long diagLeftMask=(1L << Coords.shiftIndex(position,-1,dy)),//diagonally to the left, purely for readability, generates the bitmask for the square
				diagRightMask=(1L << Coords.shiftIndex(position,1,dy));//diagonally to the right, purely for readability, generates the bitmask for the square
		long enemies=board.alliedPieceMask(!team);//get all squares controlled by the enemy

		//normal moves
		if(board.getSquare(Coords.shiftIndex(position,0,dy))==PieceCode.Blank){//single move if not blocked todo speed test against use blank square checks
			moves.add(Move.encodeNormal(pieceCode,position,Coords.shiftIndex(position,0,dy)));//single move in direction of travel for this pawn
			if(board.getSquare(Coords.shiftIndex(position,0,2*dy))==PieceCode.Blank && board.hasNotMoved(position)){//if first move and not blocked
				moves.add(Move.encode(Move.pawnDoubleMove,pieceCode,position,Coords.shiftIndex(position,0,2*dy)));//double move on first move
			}
		}

		boolean edgeLeft=Coords.indexToX(position)==0;
		boolean edgeRight=Coords.indexToX(position)==7;

		//captures (with Yoda code)
		if(0!=(enemies & diagLeftMask) && !edgeLeft) moves.add(Move.encode(Move.capture,pieceCode,position,Coords.shiftIndex(position,-1,dy)));//capture if enemy piece diagonally to the left
		if(0!=(enemies & diagRightMask) && !edgeRight) moves.add(Move.encode(Move.capture,pieceCode,position,Coords.shiftIndex(position,1,dy)));//capture if enemy piece diagonally to the right
		//EnPassant - no need to check for blank squares as they will be blank if EnPassant was tripped anyhow
		if(0!=(enemies & board.getEnPassant() & (1L << Coords.shiftIndex(position,-1,0))) && !edgeLeft){//Can I EnPassant to the left? (bitmask madness to quick search EnPassant against enemies left of pawn)
			moves.add(Move.encode(Move.EnPassantCapture,pieceCode,position,Coords.shiftIndex(position,-1,dy)));
		}else if(0!=(enemies & board.getEnPassant() & (1L << Coords.shiftIndex(position,1,0))) && !edgeRight){//Can I EnPassant to the right? (bitmask madness to quick search EnPassant against enemies right of pawn)
			moves.add(Move.encode(Move.EnPassantCapture,pieceCode,position,Coords.shiftIndex(position,1,dy)));
		}//else if is because enpassant only happens to one pawn at a time, save a jump sometimes

		//promotion
		if(team==WHITE && 0!=(WHITE_Promotion_mask & (1L << position)) ||//Mask WHITE for promotion eligibility
				team==BLACK && 0!=(BLACK_Promotion_mask & (1L << position))){//Mask BLACK for promotion eligibility, OR saves one jump more often than putting two ifs and selecting different promotion methods
			return generatePromotions(moves);//if we can promote, force promotion
		}

		return moves;//if no promotion, then return the normal moves list
	}

	@Override
	public ArrayList<Integer> getAttackingMoves(Board board,int position){
		ArrayList<Integer> moves=new ArrayList<>();
		int dy=(team==WHITE) ? 1 : -1;//move forward if WHITE, backwards if BLACK
		long diagLeftMask=(1L << Coords.shiftIndex(position,-1,dy)),//diagonally to the left, purely for readability, generates the bitmask for the square
				diagRightMask=(1L << Coords.shiftIndex(position,1,dy));//diagonally to the right, purely for readability, generates the bitmask for the square
		long enemies=board.alliedPieceMask(!team);//get all squares controlled by the enemy

		boolean edgeLeft=Coords.indexToX(position)==0;
		boolean edgeRight=Coords.indexToX(position)==7;

		//captures (with Yoda code)
		if(0!=(enemies & diagLeftMask) && !edgeLeft) moves.add(Move.encode(Move.capture,pieceCode,position,Coords.shiftIndex(position,-1,dy)));//capture if enemy piece diagonally to the left
		if(0!=(enemies & diagRightMask) && !edgeRight) moves.add(Move.encode(Move.capture,pieceCode,position,Coords.shiftIndex(position,1,dy)));//capture if enemy piece diagonally to the right
		//EnPassant - no need to check for blank squares as they will be blank if EnPassant was tripped anyhow
		if(0!=(enemies & board.getEnPassant() & (1L << (position-1))) && !edgeLeft){//Can I EnPassant to the left? (bitmask madness to quick search EnPassant against enemies left of pawn)
			moves.add(Move.encode(Move.EnPassantCapture,pieceCode,position,Coords.shiftIndex(position,-1,dy)));
		}else if(0!=(enemies & board.getEnPassant() & (1L << (position+1))) && !edgeRight){//Can I EnPassant to the right? (bitmask madness to quick search EnPassant against enemies right of pawn)
			moves.add(Move.encode(Move.EnPassantCapture,pieceCode,position,Coords.shiftIndex(position,1,dy)));
		}

		//promotion
		if(team==WHITE && 0!=(WHITE_Promotion_mask & (1L << position)) ||//Mask WHITE for promotion eligibility
				team==BLACK && 0!=(BLACK_Promotion_mask & (1L << position))){//Mask BLACK for promotion eligibility, OR saves one jump more often than putting two ifs and selecting different promotion methods
			return generatePromotions(moves);//if we can promote, force promotion
		}

		return moves;
	}

	/**
	 * Converts all moves into promotions for each piece type on this Pawn's team
	 * @param moves The list of integer moves to convert
	 * @return The same moves but each one is now every possible promotion
	 */
	private ArrayList<Integer> generatePromotions(ArrayList<Integer> moves){
		ArrayList<Integer> promotions=new ArrayList<>();
		int start, end, specialCode;
		for(int i=0; i<moves.size(); ++i){//search all moves, exactly copy them with each eligible piece code for promotion
			start=Move.getStartIndex(moves.get(i));//TODO: replace this with more masking madness
			end=Move.getEndIndex(moves.get(i));
			specialCode=Move.getSpecialCode(moves.get(i));
			for(int j=pieceCode+2; j<PieceCode.PIECE_TYPES; j+=2){//this is a weird one, I chose to make BLACK and WHITE piece codes odd and even respectively, this works for the same reason a bit mask works, the 1's bit acts like a team toggle and by offsetting we can select the team for (almost) free. We also can't promote to kings
				promotions.add(Move.encode(specialCode | Move.pawnPromote,j,start,end));//copy everything but iterate all eligible new piece codes and toggle the promotion bit
			}
		}
		return promotions;
	}

	/**
	 * Are we in the right position on the board to promote?
	 * @param b        The board
	 * @param position Our position
	 * @return Whether we should check for promotion
	 */
	private boolean canPromote(Board b,Coords position){
		if(getTeam() && position.getY()==6) return true;
		return !getTeam() && position.getY()==1;//save a jump (nice find Intellij)
	}

	/**
	 * Adds a pawn move to a list of pawn moves
	 * @param board      The board
	 * @param list       The list to add to
	 * @param startPos   The move start pos
	 * @param endPos     The move end pos
	 * @param piece      The move piece
	 * @param moveType   The move type
	 * @param enemyPiece The move enemy piece
	 */
	private void addPawnMove(Board board,ArrayList<SlowMove> list,Coords startPos,Coords endPos,char piece,moveTypes moveType,char enemyPiece){
		if(canPromote(board,startPos)){
			// Add a promotion move
			// If we won't add a separate class for promotionMove that encapsulates the
			// promotion logic, we can include it as a method in the SlowMove class.
			char[] promotionPieces={'Q','R','B','N'}; // List of possible promotion pieces
			for(char promoPiece: promotionPieces){
				SlowMove newM=new SlowMove(startPos,endPos,getChar(),moveTypes.pawnPromote,piece);
				list.add(newM);
			}
		}else{
			list.add(new SlowMove(startPos,endPos,getChar(),moveType,piece));
		}
	}

	/**
	 * Placeholder for the incomplete method
	 * @param o the piece to be compared.
	 * @return 0
	 */
	@Override
	public int compareTo(Piece o){
		return 0;
	}
}