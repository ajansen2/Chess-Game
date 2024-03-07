/*
File: KnightTest.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
import static edu.kingsu.SoftwareEngineering.Chess.UtilsForTests.*;

/**
 * Tests for the Knight piece class
 * @author Dalton Herrewynen
 * @version 2
 */
public class KnightTest{
	/** The offsets of the moves the Knight could make if there is room */
	public static final int[][] legalOffset={//0 is x, 1 is y
			{-2,1},{-1,2},{-2,-1},{-1,-2},//left half
			{2,1},{1,2},{2,-1},{1,-2}};   //right half
	/** Constants for testing moves on and off the board */
	public static final int minOnBoard=2, maxOnBoard=5;
	Board board;
	BoardState blankState;
	ArrayList<Integer> gotMoves, filteredMoves;
	ArrayList<Coords> expectedCoords, gotCoords;//these are the destination coords

	@Before
	public void setup(){
		board=new Board(Board.CLEAR);
		expectedCoords=new ArrayList<>();
		gotCoords=new ArrayList<>();
		gotMoves=new ArrayList<>();
		filteredMoves=new ArrayList<>();
		blankState=board.saveState();
	}

	@After
	public void tearDown(){
		board=null;
		expectedCoords=null;
		gotCoords=null;
		gotMoves=null;
		filteredMoves=null;
		blankState=null;
	}

	/** Test the Move structure for WHITE */
	@Test
	public void testMoveAnatomyWHITE(){
		testMoveAnatomyCommon(WHITE);
	}

	/** Test the Move structure for BLACK */
	@Test
	public void testMoveAnatomyBLACK(){
		testMoveAnatomyCommon(BLACK);
	}

	/**
	 * Test the Move structure, the common code for both teams
	 * @param team Which team to test for
	 */
	public void testMoveAnatomyCommon(boolean team){
		Coords piecePos=new Coords(4,4);
		Knight piece=new Knight(team);
		board.setSquare(piece.pieceCode,piecePos);
		gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
		assertFalse("There should be encoded move integers here",gotMoves.isEmpty());
		for(int move: gotMoves){
			assertEquals("Moves should have starting position correct",piecePos.toString(),Coords.orderedPair(Move.getStartIndex(move)));
			assertEquals("Move should be of Normal type (1)",Move.normalMove,Move.getSpecialCode(move));
			assertEquals("Piece code, converted to pretty printed name",PieceCode.decodePieceName(piece.pieceCode),Move.getPieceName(move));
		}
	}

	/** Test the WHITE Knight where it can make all its possible moves */
	@Test
	public void testBasicMoveWHITE(){
		for(int y=minOnBoard; y<=maxOnBoard; ++y){
			for(int x=minOnBoard; x<=maxOnBoard; ++x){//iterate all X and Y combinations that will only generate moves on the board
				Coords piecePos=new Coords(x,y);
				testBasicMoveTemplate(piecePos,WHITE);
			}
		}
	}

	/** Test the BLACK Knight where it can make all its possible moves */
	@Test
	public void testBasicMoveBLACK(){
		for(int y=minOnBoard; y<=maxOnBoard; ++y){
			for(int x=minOnBoard; x<=maxOnBoard; ++x){//iterate all X and Y combinations that will only generate moves on the board
				Coords piecePos=new Coords(x,y);
				testBasicMoveTemplate(piecePos,BLACK);
			}
		}
	}

	/** Test the WHITE Knight where some possible moves would be off the board */
	@Test
	public void testBasicMoveOffBoardWHITE(){
		for(int y=0; y<BOARD_SIZE; ++y){
			for(int x=0; x<BOARD_SIZE; ++x){//iterate over all squares, only use squares that can generate moves off the board
				if(x>=minOnBoard && x<maxOnBoard && y>=minOnBoard && y<maxOnBoard) continue;//don't recheck squares that only generate moves on the board, they're already checked in another test
				Coords piecePos=new Coords(x,y);//put it where it will potentially generate off-board moves
				testBasicMoveTemplate(piecePos,WHITE);
			}
		}
	}

	/** Test the BLACK Knight where some possible moves would be off the board */
	@Test
	public void testBasicMoveOffBoardBLACK(){
		for(int y=0; y<BOARD_SIZE; ++y){
			for(int x=0; x<BOARD_SIZE; ++x){//iterate over all squares
				if(x>=minOnBoard && x<maxOnBoard && y>=minOnBoard && y<maxOnBoard) continue;//don't recheck squares that only generate moves on the board, they're already checked in another test
				Coords piecePos=new Coords(x,y);//put it where it will potentially generate off-board moves
				testBasicMoveTemplate(piecePos,BLACK);
			}
		}
	}

	/**
	 * Used for testing the basic moves of the Knight Piece for either team
	 * @param piecePos the coordinate of where to put the piece on the Board
	 * @param team     WHITE or BLACK
	 */
	private void testBasicMoveTemplate(Coords piecePos,boolean team){
		Knight knight=new Knight(team);
		board.setSquare(knight.pieceCode,piecePos);
		gotMoves=knight.getLegalMoves(board,piecePos.getIndex());
		gotCoords=getDestCoords(gotMoves);
		expectedCoords=new ArrayList<>();

		for(int i=0; i<gotMoves.size(); ++i){//make sure Piece Codes match
			assertEquals("Move "+i+" "+Move.describe(gotMoves.get(i))+"  should have PieceCode match the Knight"
					,knight.pieceCode,Move.getPieceCode(gotMoves.get(i)));
		}

		for(int[] ints: legalOffset){
			Coords testDest=new Coords(piecePos);
			if(testDest.addVector(ints[0],ints[1])){//if piece would still be on the board
				expectedCoords.add(testDest);//add it to the list of coords that can be reached
			}
		}
		gotCoords.sort(new Coords());//sort the destinations
		expectedCoords.sort(new Coords());
		assertEquals("Tile "+piecePos+" Got and Expected coord list should be same length",expectedCoords.size(),gotCoords.size());
		for(int i=0; i<gotCoords.size(); ++i){
			assertEquals("Tile "+piecePos+" Failure at move "+i+" destinations do not match",expectedCoords.get(i).toString(),gotCoords.get(i).toString());
		}
		board.loadState(blankState);//clean up after myself
	}

	/** Call the capture moves test for BLACK Knights */
	@Test
	public void testCaptureBLACK(){
		testCaptureCommon(BLACK);
	}

	/** Call the capture moves test for WHITE Knights */
	@Test
	public void testCaptureWHITE(){
		testCaptureCommon(WHITE);
	}

	/**
	 * Common code for capture tests
	 * @param team The team of this Knight
	 */
	private void testCaptureCommon(boolean team){
		Piece piece=new Knight(team), enemy=new Pawn(!team), friendly=new Pawn(team);
		Coords piecePos=new Coords(), coords;
		for(int i=0; i<TOTAL_SQUARES; ++i){
			piecePos.setIndex(i);
			board.setSquare(piece.pieceCode,piecePos);
			expectedCoords.clear();
			for(int[] offset: legalOffset){//figure out how many offsets are on the board
				coords=piecePos.getShiftedCoord(offset[0],offset[1]);
				if(coords.isSet()==SET) expectedCoords.add(coords.copy());//add only if set
			}
			for(Coords coord: expectedCoords){//fill reachable squares with enemies
				board.setSquare(enemy.pieceCode,coord);//these should each generate a capture
			}
			gotMoves=piece.getLegalMoves(board,piecePos.getIndex());//get all moves
			for(int j=0; j<gotMoves.size(); ++j){//make sure Piece Codes match
				assertEquals("Move "+j+" "+Move.describe(gotMoves.get(j))+" should have PieceCode match the Knight"
						,piece.pieceCode,Move.getPieceCode(gotMoves.get(j)));
			}
			filteredMoves=findCaptures(gotMoves,board);//just check for captures

			assertEquals("Tile:"+piecePos+" Should have found same number of captures as enemies",expectedCoords.size(),filteredMoves.size());
			for(int move: gotMoves){
				assertEquals("Tile:"+piecePos+" capture move at "+Coords.orderedPair(Move.getEndIndex(move))
						+" should point to an enemy position",enemy.getChar(),PieceCode.decodeChar(board.getSquare(Move.getEndIndex(move))));
				assertTrue("Tile: "+piecePos+": capture move at"+Coords.orderedPair(Move.getEndIndex(move))+
						" should flag capture", Move.isCapture(move));
			}

			for(Coords coord: expectedCoords){//fill reachable squares with friendlies
				board.setSquare(friendly.pieceCode,coord);//these should not generate captures
			}
			gotMoves=piece.getLegalMoves(board,piecePos.getIndex());//get all moves
			for(int j=0; j<gotMoves.size(); ++j){//make sure Piece Codes match
				assertEquals("Move "+j+" "+Move.describe(gotMoves.get(j))+" should have PieceCode match the Knight",
						piece.pieceCode,Move.getPieceCode(gotMoves.get(j)));
			}
			filteredMoves=findCaptures(gotMoves,board);//just check for captures

			assertEquals("Tile:"+piecePos+" Should have found 0 capture moves, should not capture friendlies",0,filteredMoves.size());

			for(Coords coord: expectedCoords){//clean up for next square
				board.setSquare(PieceCode.Blank,coord);//blank out squares
			}
			board.setSquare(PieceCode.Blank,piecePos);
		}
	}
}
