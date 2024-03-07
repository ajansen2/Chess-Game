/*
File: Piece.java
Copyright (C) 2023 Dalton Herrewynen, Christopher Bury. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import javax.swing.*;

import java.awt.*;
import java.util.Objects;
import java.util.ArrayList;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Class for generic pieces
 * @author Christopher Bury
 * @author Group 4
 * @version 1
 */
public abstract class Piece implements Comparable<Piece>{
	/** Directions for line diagonal line checks */
	public static final int NE=7, NW=9, SE=-9, SW=-7;
	public final int pieceCode;
	protected final boolean team;
	protected pieceType type;
	private final char pieceChar;

	/**
	 * Constructs the Piece and sets the team (color, team, etc.)
	 * @param givenTeam The Team Enum containing one of the possible teams
	 * @param givenType The Piece type supplied by the child class constructor
	 */
	public Piece(boolean givenTeam,pieceType givenType){
		this.team=givenTeam;
		this.type=givenType;
		this.pieceChar=getCharFromPieceType(givenType,givenTeam);
		this.pieceCode=PieceCode.encodePieceType(givenType,givenTeam);
	}

	/**
	 * Gets the current team
	 * @return The Team either WHITE or BLACK
	 */
	public boolean getTeam(){
		return team;
	}

	/**
	 * Prints the Piece's ASCII letter representation, for commandline printing
	 * @return A string which is used to generate ASCII art games
	 */
	public char getChar(){
		return pieceChar;
	}

	/**
	 * Returns the piece's type
	 * @return One of the valid types
	 */
	public pieceType getType(){
		return type;
	}

	/**
	 * Converts the piece to a human-readable String containing its internal state
	 * @return String representation of internal state
	 */
	public abstract String toString();

	/**
	 * Prints the team of the Piece, Wrapper for the one in Types.java
	 * @return White or Black depending on which team this Piece was set to
	 */
	public String getPieceTeamString(){
		return Types.getTeamString(team);
	}

	/**
	 * Calculates the Piece's value to the AI player
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return relative value to the AI
	 */
	public abstract int pieceValue(Board board,int position);

	/**
	 * Returns all legal moves that this Piece is capable of making
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of SlowMove objects that are legal
	 */
	public abstract ArrayList<SlowMove> getLegalMovesObj(Board board,Coords position);

	/**
	 * The <b>NEW</b> and <b>IMPROVED</b> move generator method
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	public abstract ArrayList<Integer> getLegalMoves(Board board,int position);

	/**
	 * Returns the moves where capture is possible. This usually equals legal moves, but not always
	 * @param board    The current state of the board
	 * @param position The position index to check from
	 * @return an ArrayList of integers which encode all the relevant move data for each move
	 */
	public ArrayList<Integer> getAttackingMoves(Board board,int position){
		//For most pieces, every valid move can also capture
		return getLegalMoves(board,position);
	}

	/**
	 * Returns all legal moves that this Piece is capable of making that don't leave the king in check
	 * @param board    The current Board object
	 * @param position Where this Piece is located on the board
	 * @return ArrayList of integer encoded moves that are legal
	 */
	public ArrayList<Integer> getLegalMovesAfterChecks(Board board,int position){
		ArrayList<Integer> possMoves_=getLegalMoves(board,position);
		int kingCode=PieceCode.encodePieceType(pieceType.king,team);
		int kingPos=Coords.maskToIndex(board.searchPiece(kingCode));
		King kingReference=((King) PieceCode.pieceObj(kingCode));
		//You can't be checkmated if there's no king to checkmate
		if(kingPos==Coords.ERROR_INDEX) return possMoves_;//if no king found, return the full moves list
		ArrayList<Integer> moves=new ArrayList<>();

		//For each possible move, are we still in check after said move is complete?
		Board newBoard=new Board(Board.CLEAR);
		for(Integer possMove: possMoves_){
			newBoard.loadState(board);//revert move
			newBoard.makeMove(possMove);//make move
			if(!kingReference.isInCheck(newBoard,Coords.maskToIndex(newBoard.searchPiece(kingCode)))) moves.add(possMove);//if not in check, add the move
		}
		return moves;
	}

	/**
	 * Generates a line check for bishops, rooks, and queens
	 * @param board      The current Board object
	 * @param position   The position of the piece we're checking
	 * @param xIncrement The x increment for the line check
	 * @param yIncrement The y increment for the line check
	 * @return The list of ending move positions that the check finds
	 */
	protected ArrayList<SlowMove> pieceLineMoveCheckObj(Board board,Coords position,int xIncrement,int yIncrement){
		Coords currentPos=position.copy();
		ArrayList<SlowMove> temp=new ArrayList<>();
		int p;
		while(currentPos.addVector(xIncrement,yIncrement)==SUCCESS){//loop as long as a coordinate is on the board
			p=board.getSquare(currentPos.getIndex());
			if(p==PieceCode.Blank){//empty square
				temp.add(new SlowMove(position,currentPos,getChar()));//just move
			}else if(PieceCode.decodeTeam(p)==team){//We ran into a teammate
				break;//break the loop, we're done
			}else{//We ran into an enemy; include capture and then return
				temp.add(new SlowMove(position,currentPos,getChar(),PieceCode.decodeChar(p)));
				break;//break the loop, we're done
			}
		}
		return temp;
	}

	/**
	 * Generates a line check for bishops, rooks, and queens
	 * @param board      The current Board object
	 * @param position   The position of the piece we're checking
	 * @param xIncrement The x increment for the line check
	 * @param yIncrement The y increment for the line check
	 * @return The list of ending move positions that the check finds
	 */
	protected ArrayList<Integer> pieceLineMoveCheck(Board board,int position,int xIncrement,int yIncrement){
		Coords currentPos=new Coords(position);
		ArrayList<Integer> temp=new ArrayList<>();
		int p;
		while(currentPos.addVector(xIncrement,yIncrement)==SUCCESS){//loop as long as a coordinate is on the board
			p=board.getSquare(currentPos.getIndex());
			if(p==PieceCode.Blank){//empty square
				//temp.add(new SlowMove(position,currentPos,getChar()));//just move
				temp.add(Move.encodeNormal(pieceCode,position,currentPos.getIndex()));
			}else if(PieceCode.decodeTeam(p)==team){//We ran into a teammate
				break;//break the loop, we're done
			}else{//We ran into an enemy; include capture and then return
				temp.add(Move.encode(Move.capture,pieceCode,position,currentPos.getIndex()));
				break;//break the loop, we're done
			}
		}
		return temp;
	}

	/**
	 * Checks a Horizontal and a Vertical line for sliding pieces
	 * @param enemies  A bitmask of the enemies on the board
	 * @param blanks   A bitmask of the blank squares
	 * @param position The current position of this piece
	 * @return A list of encoded Move integers
	 */
	protected ArrayList<Integer> HVLineCheck(final long enemies,final long blanks,final int position){
		ArrayList<Integer> moves=new ArrayList<>();
		int x=Coords.indexToX(position), y=Coords.indexToY(position);
		//horizontal moves
		int i=position-1;//start behind the position,
		for(; i>=Coords.XYToIndex(0,y) && (0!=(blanks & (1L << i))); --i){//and then move backwards until end of row or a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(i>=Coords.XYToIndex(0,y) && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}
		i=position+1;//now check from 1 in front of the position
		for(; i<Coords.XYToIndex(BOARD_SIZE,y) && (0!=(blanks & (1L << i))); ++i){//and then move forwards until end of row or a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(i<Coords.XYToIndex(BOARD_SIZE,y) && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}

		//vertical moves
		i=position-BOARD_SIZE;//start below the position (remember that to move index up 1 need to move over by 8)
		for(; i>=Coords.XYToIndex(x,0) && (0!=(blanks & (1L << i))); i-=BOARD_SIZE){//and then move downwards until end of row or a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(i>=Coords.XYToIndex(x,0) && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//if we found an enemy then make a capture
		}
		i=position+BOARD_SIZE;//now check from 1 above of the position
		for(; i<Coords.XYToIndex(x,BOARD_SIZE) && (0!=(blanks & (1L << i))); i+=BOARD_SIZE){//and then move upwards until end of row or a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(i<Coords.XYToIndex(x,BOARD_SIZE) && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}
		return moves;
	}

	/**
	 * Checks both diagonal lines for sliding pieces
	 * @param enemies  A bitmask of the enemies on the board
	 * @param blanks   A bitmask of the blank squares
	 * @param position The current position of this piece
	 * @return A list of encoded Move integers
	 */
	protected ArrayList<Integer> diagLineCheck(final long enemies,final long blanks,final int position){
		ArrayList<Integer> moves=new ArrayList<>();
		final int x=Coords.indexToX(position), y=Coords.indexToY(position);
		//North West
		int i=position+NW;//iterate ahead of position
		for(; Coords.indexToX(i)<BOARD_SIZE && Coords.indexToX(i)>x && i<TOTAL_SQUARES && (0!=(blanks & (1L << i))); i+=NW){//Move NW until off side edge (Coords.indexToX()) or off end (>TOTAL_SQUARES) or find a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(Coords.indexToX(i)<BOARD_SIZE && Coords.indexToX(i)>x && i<TOTAL_SQUARES && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}

		//North East
		i=position+NE;//iterate ahead of position
		for(; Coords.indexToX(i)>=0 && Coords.indexToX(i)<x && i<TOTAL_SQUARES && (0!=(blanks & (1L << i))); i+=NE){//Move NE until off side edge (Coords.indexToX()) or off end (>TOTAL_SQUARES) or find a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(Coords.indexToX(i)>=0 && Coords.indexToX(i)<x && i<TOTAL_SQUARES && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}

		//South East
		i=position+SE;//iterate ahead of position
		for(; Coords.indexToX(i)>=0 && Coords.indexToX(i)<x && i>=0 && (0!=(blanks & (1L << i))); i+=SE){//Move SE until off side edge (Coords.indexToX()) or off end (>TOTAL_SQUARES) or find a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(Coords.indexToX(i)>=0 && Coords.indexToX(i)<x && i>=0 && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}//*/

		//South West
		i=position+SW;//iterate ahead of position
		for(; Coords.indexToX(i)<BOARD_SIZE && Coords.indexToX(i)>x && i>=0 && (0!=(blanks & (1L << i))); i+=SW){//Move SW until off side edge (Coords.indexToX()) or off end (>TOTAL_SQUARES) or find a non-blank square
			moves.add(Move.encodeNormal(pieceCode,position,i));//while blank, encode normal moves
		}
		if(Coords.indexToX(i)<BOARD_SIZE && Coords.indexToX(i)>x && i>=0 && (0!=(enemies & (1L << i)))){//if we found an enemy and the loop exited early
			moves.add(Move.encode(Move.capture,pieceCode,position,i));//then make a capture
		}//*/

		return moves;
	}

	/**
	 * Returns a list of single moves that the King can make in the given direction
	 * @param board      The current Board Object
	 * @param position   Where this King is located on the board
	 * @param xIncrement The x increment for the move direction
	 * @param yIncrement The y increment for the move direction
	 * @return SlowMove that is legal
	 */
	protected SlowMove pieceSingleMoveCheckObj(Board board,Coords position,int xIncrement,int yIncrement){
		if(team==BLACK){//rotate board if BLACK
			position=position.copyRotate180();//rotate if BLACK
			board=board.getRotatedBoard();
		}
		Coords newPos=position.copy();
		//Check if the new position is within the board bounds
		if(newPos.addVector(xIncrement,yIncrement)==SUCCESS){
			int targetPiece=board.getSquare(newPos);
			if(targetPiece==PieceCode.Blank){//if empty, just move
				if(team==BLACK) return new SlowMove(position.copyRotate180(),newPos.copyRotate180(),getChar());//rotate back if BLACK
				else return new SlowMove(position,newPos,getChar());
			}else if(PieceCode.decodeTeam(targetPiece)!=team){//If there is an enemy piece, set the move type to capture
				if(team==BLACK) return new SlowMove(position.copyRotate180(),newPos.copyRotate180(),getChar(),PieceCode.decodeChar(targetPiece));//rotate back if BLACK
				else return new SlowMove(position,newPos,getChar(),PieceCode.decodeChar(targetPiece));
			}
		}
		return null;
	}

	/**
	 * Returns a list of single moves that the King can make in the given direction
	 * @param board      The current Board Object
	 * @param position   Where this King is located on the board
	 * @param xIncrement The x increment for the move direction
	 * @param yIncrement The y increment for the move direction
	 * @return SlowMove that is legal
	 */
	protected int pieceSingleMoveCheck(Board board,int position,int xIncrement,int yIncrement){
		if(team==BLACK){//rotate board if BLACK
			position=Coords.rotate180Index(position);//rotate if BLACK
			board=board.getRotatedBoard();
		}
		int newPos=position;
		int offsetNewPos=Coords.shiftIndex(newPos,xIncrement,yIncrement);
		//Check if the new position is within the board bounds
		if(Coords.isIndexValid(offsetNewPos)==SUCCESS){
			int targetPiece=board.getSquare(newPos);
			//if(targetPiece==PieceCode.Blank)
			if(team==BLACK) return Move.encodeNormal(pieceCode,position,Coords.rotate180Index(offsetNewPos));//rotate back if BLACK
			else return Move.encodeNormal(pieceCode,position,offsetNewPos);
		}
		return Move.blank();
	}

	/**
	 * Returns an icon with the piece on it
	 * @param size  The size of the icon
	 * @param model The model
	 * @param theme The current theme
	 * @return The image icon
	 */
	public ImageIcon getImageIcon(int size,GameModel model,String theme){
		ImageIcon cache=model.getImageFromCache(theme+this);
		if(cache!=null) return cache;
		try{
			if(size==0) return null;

			ImageIcon temp=new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/"+theme+"/"+this+".png")));
			Image img=temp.getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH);
			temp.setImage(img);
			model.setImageFromCache(theme+this,temp);
			return temp;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Returns an icon with the star image on it
	 * @param size The size of the icon
	 * @return The image icon
	 */
	public static ImageIcon getStarImageIcon(int size,GameModel model){
		ImageIcon cache=model.getImageFromCache("star");
		if(cache!=null) return cache;
		try{
			if(size==0) return null;

			ImageIcon temp=new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/star.png")));
			Image img=temp.getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH);
			temp.setImage(img);
			model.setImageFromCache("star",temp);
			return temp;
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
	}

	/**
	 * Generates a new Piece object according to the code given
	 * @param pieceCode The code encoding the color and type of piece
	 * @return A new Piece object
	 * @author Dalton Herrewynen
	 */
	public static Piece generatePieceFromChar(char pieceCode){
		return generatePieceFromType(getPieceFromChar(pieceCode),getTeamFromChar(pieceCode));
	}

	/**
	 * Generates a new Piece object according to the type and team
	 * @param newType The type of piece
	 * @param team    The team color of this Piece (WHITE or BLACK)
	 * @return A new Piece object
	 * @author Dalton Herrewynen
	 */
	public static Piece generatePieceFromType(pieceType newType,boolean team){
		return switch(newType){
			case king -> new King(team);
			case queen -> new Queen(team);
			case knight -> new Knight(team);
			case rook -> new Rook(team);
			case bishop -> new Bishop(team);
			case pawn -> new Pawn(team);
			default -> null;
		};
	}
}
