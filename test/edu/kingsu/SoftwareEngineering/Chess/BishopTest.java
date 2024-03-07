/*
File: BishopTest.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
import static edu.kingsu.SoftwareEngineering.Chess.PieceCode.*;
import static edu.kingsu.SoftwareEngineering.Chess.UtilsForTests.*;

/**
 * Tests for the Bishop piece class
 * @author Dalton Herrewynen
 * @version 2
 */
public class BishopTest{
	Board board;
	ArrayList<Integer> gotMoves, filteredMoves;
	ArrayList<Coords> gotCoords, expectedCoords;

	@Before
	public void setup(){
		board=new Board(Board.CLEAR);
		gotCoords=new ArrayList<>();
		expectedCoords=new ArrayList<>();
		gotMoves=new ArrayList<>();
		filteredMoves=new ArrayList<>();
	}

	@After
	public void tearDown(){
		board=null;
		gotCoords=null;
		expectedCoords=null;
		gotMoves=null;
		filteredMoves=null;
	}

	/** Test basic movement for WHITE Bishop, do not test capture */
	@Test
	public void testBasicMoveWHITE(){
		testBasicMoveCommon(WHITE);
	}

	/** Test basic movement for BLACK Bishop, do not test capture */
	@Test
	public void testBasicMoveBLACK(){
		testBasicMoveCommon(BLACK);
	}

	/**
	 * Tests each color's basic moves to avoid code re-use
	 * @param team WHITE or BLACK
	 */
	public void testBasicMoveCommon(boolean team){
		Bishop bishop=new Bishop(team);
		int pieceCode=bishop.pieceCode;
		Coords piecePos=new Coords();
		for(int i=0; i<TOTAL_SQUARES; ++i){//test on all tiles
			piecePos.setIndex(i);
			board.setSquare(pieceCode,piecePos);
			gotMoves=bishop.getLegalMoves(board,piecePos.getIndex());
			gotCoords=getDestCoords(gotMoves);

			expectedCoords=new ArrayList<>();
			expectedCoords.addAll(getLineOfCoords(piecePos,1,1));
			expectedCoords.addAll(getLineOfCoords(piecePos,1,-1));
			expectedCoords.addAll(getLineOfCoords(piecePos,-1,1));
			expectedCoords.addAll(getLineOfCoords(piecePos,-1,-1));

			gotCoords.sort(new Coords());//sort the coords, so they are in same order and comparisons will work
			expectedCoords.sort(new Coords());

			for(int move: gotMoves){//test correct piece code
				assertEquals("Piece code should match the Bishop",pieceCode,Move.getPieceCode(move));
			}

			assertEquals("Square "+piecePos+": Must have same number of Coords in the got and expected list",
					expectedCoords.size(),gotCoords.size());
			for(int j=0; j<gotCoords.size(); ++j){
				assertEquals("Square "+piecePos+": Move "+j+" destinations don't match",
						expectedCoords.get(j).toString(),gotCoords.get(j).toString());
			}
			board.setSquare(Blank,piecePos);//remove this piece and start again
		}
	}

	/** Test the Move structure WHITE */
	@Test
	public void testMoveAnatomyWHITE(){
		testMoveAnatomyCommon(WHITE);
	}

	/** Test the Move structure BLACK */
	@Test
	public void testMoveAnatomyBlack(){
		testMoveAnatomyCommon(BLACK);
	}

	/**
	 * The move anatomy tester for both teams to avoid code re-use
	 * @param team WHITE or BLACK
	 */
	public void testMoveAnatomyCommon(boolean team){
		Coords piecePos=new Coords(4,4);
		Bishop piece=new Bishop(team);
		board.setSquare(piece.pieceCode,piecePos);
		gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
		assertFalse("There should be encoded move integers here",gotMoves.isEmpty());
		for(int move: gotMoves){
			assertEquals("Moves should have starting position correct",piecePos.toString(),Coords.orderedPair(Move.getStartIndex(move)));
			assertEquals("Move should be of Normal type (0)",Move.normalMove,Move.getSpecialCode(move));
			assertEquals("Piece code, converted to pretty printed name",PieceCode.decodePieceName(piece.pieceCode),Move.getPieceName(move));
		}
	}

	/** Test BLACK Bishop Captures */
	@Test
	public void testBLACKCapture(){
		testCaptureCommon(BLACK);
	}

	/** Test WHITE Bishop Captures */
	@Test
	public void testWHITECapture(){
		testCaptureCommon(WHITE);
	}

	/**
	 * Test captures for either team, void code re-use
	 * @param team WHITE or BLACK
	 */
	public void testCaptureCommon(boolean team){
		Bishop piece=new Bishop(team), friendly=new Bishop(team), enemy=new Bishop(!team);
		Coords piecePos=new Coords();
		ArrayList<Coords> reachableSquares=new ArrayList<>();

		for(int i=0; i<TOTAL_SQUARES; ++i){//testing all squares
			piecePos.setIndex(i);
			board.setSquare(piece.pieceCode,piecePos);

			reachableSquares.clear();//reset
			reachableSquares.addAll(getLineOfCoords(piecePos,1,1));//4 diagonal directions
			reachableSquares.addAll(getLineOfCoords(piecePos,1,-1));
			reachableSquares.addAll(getLineOfCoords(piecePos,-1,1));
			reachableSquares.addAll(getLineOfCoords(piecePos,-1,-1));

			for(Coords enemyPos: reachableSquares){//Check ALL squares reachable by the Rook, one by one
				board.setSquare(enemy,enemyPos);//Test capture
				gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
				filteredMoves=findCaptures(gotMoves,board);
				assertEquals("Tile: "+piecePos+": Should not have exactly one capture",1,filteredMoves.size());
				assertEquals("Tile: "+piecePos+": capture move should end on the enemy piece",
						enemyPos.toString(),Coords.orderedPair(Move.getEndIndex(filteredMoves.get(0))));
				assertTrue("Tile: "+piecePos+": capture move should flag capture", Move.isCapture(filteredMoves.get(0)));
				assertEquals("Tile: "+piecePos+": capture move should have this Bishop's code set in its pieceCode field",
						piece.pieceCode,Move.getPieceCode(filteredMoves.get(0)));

				board.setSquare(friendly,enemyPos);//Don't capture friendlies
				gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
				filteredMoves=findCaptures(gotMoves,board);
				assertEquals("Tile: "+piecePos+": Should not capture friendlies at "+enemyPos+" Size of list should be 0"
						,0,filteredMoves.size());
				board.setSquare(PieceCode.Blank,enemyPos);//Clean up for next run
			}
			board.setSquare(PieceCode.Blank,piecePos);//remove the piece I placed for next square
		}
	}
}
