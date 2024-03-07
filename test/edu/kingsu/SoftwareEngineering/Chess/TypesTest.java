/*
File: TypesTest.java
Copyright (C) 2023 Dalton Herrewynen. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
Ownership is to be held by the primary author.
Licence is granted to the secondary members as noted in the Authors.md file for display, running, compiling, and modification for use in their future projects. Just keep my name on functions I wrote.
 */
package edu.kingsu.SoftwareEngineering.Chess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
import static edu.kingsu.SoftwareEngineering.Chess.UtilsForTests.*;
import static org.junit.Assert.*;

/**
 * Tests for the Types utility class
 * @author Dalton Herrewynen
 * @version 1
 */
public class TypesTest{
	Board blankBoard, gameBoard;

	@Before
	public void setUp(){
		blankBoard=new Board(Board.CLEAR);
		gameBoard=new Board(Board.DEFAULT);
	}

	@After
	public void tearDown(){
		blankBoard=null;
		gameBoard=null;
	}

	/** Test for the team setting case from team */
	@Test
	public void testTeamCases(){
		//white is lowercase
		assertEquals("WHITE team is lowercase",'a',setCaseFromTeam('a',WHITE));
		assertEquals("WHITE team is lowercase",'a',setCaseFromTeam('A',WHITE));
		assertEquals("BLACK team is UPPERCASE",'A',setCaseFromTeam('a',BLACK));
		assertEquals("BLACK team is UPPERCASE",'A',setCaseFromTeam('A',BLACK));
	}

	/** Test for valid team value */
	@Test
	public void testConstantsValues(){
		assertNotEquals("Teams should not be the same value",BLACK,WHITE);
		assertNotEquals("SET and UNSET should be different",SET,UNSET);
		assertNotEquals("Success and failure should be different",SUCCESS,FAIL);
		assertTrue("Success should be true because that makes sense",SUCCESS);
	}

	/** Tests the line checker function */
	@Test
	public void isEmptyLineTest(){
		Coords[] start=new Coords[BOARD_SIZE];
		Coords[] end=new Coords[BOARD_SIZE];
		Pawn piece=new Pawn(WHITE);
		for(int i=0; i<BOARD_SIZE; ++i){//set up horizontal checks
			start[i]=new Coords(0,i);
			end[i]=new Coords(BOARD_SIZE-1,i);
		}
		for(int i=0; i<BOARD_SIZE; ++i){//test vertical
			//blank lines
			assertTrue("Forward: Row:"+i+" was empty, Should be true",isLineEmpty(blankBoard,start[i],end[i]));//full line
			assertTrue("Backward: Row:"+i+" was empty, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//backwards
			blankBoard.setSquare(piece.pieceCode,start[i]);//put a piece on each edge
			blankBoard.setSquare(piece.pieceCode,end[i]);//test edges
			assertTrue("Forward: Row:"+i+" has pieces on edge, Should be true",isLineEmpty(blankBoard,start[i],end[i]));//full line
			assertTrue("Backward: Row:"+i+" has pieces on edge, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//backwards
			start[i].addVector(3,0);//check right half of board
			blankBoard.setSquare(piece.pieceCode,start[i]);//put pieces on middle
			assertTrue("Forward: Row:"+i+" has coords right the piece, Should be true",isLineEmpty(blankBoard,start[i],end[i]));
			assertTrue("Backward: Row:"+i+" has coords right the piece, Should be true",isLineEmpty(blankBoard,end[i],start[i]));
			end[i].setCoords(0,end[i].getY());//test left half of the board
			assertTrue("Forward: Row:"+i+" has coords left of the piece, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//reversed order because coords are reversed
			assertTrue("Backward: Row:"+i+" has coords left of the piece, Should be true",isLineEmpty(blankBoard,start[i],end[i]));
			start[i].setCoords(0,start[i].getY());//test edges again to test if it can detect a filled line
			end[i].setCoords(BOARD_SIZE-1,end[i].getY());
			assertFalse("Forward: Across row:"+i+" has a piece obstructing it in the middle, should be false",isLineEmpty(blankBoard,start[i],end[i]));
			assertFalse("Backward: Across row:"+i+" has a piece obstructing it in the middle, should be false",isLineEmpty(blankBoard,end[i],start[i]));
		}
		blankBoard=new Board(Board.CLEAR);
		for(int i=0; i<BOARD_SIZE; ++i){//set up vertical checks
			start[i]=new Coords(i,0);
			end[i]=new Coords(i,BOARD_SIZE-1);
		}
		for(int i=0; i<BOARD_SIZE; ++i){//test vertical
			//blank lines
			assertTrue("Forward: Column:"+i+" was empty, Should be true",isLineEmpty(blankBoard,start[i],end[i]));//full line
			assertTrue("Backward: Column:"+i+" was empty, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//backwards
			blankBoard.setSquare(piece.pieceCode,start[i]);//put a piece on each edge
			blankBoard.setSquare(piece.pieceCode,end[i]);//test edges
			assertTrue("Forward: Column:"+i+" has pieces on edge, Should be true",isLineEmpty(blankBoard,start[i],end[i]));//full line
			assertTrue("Backward: Column:"+i+" has pieces on edge, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//backwards
			start[i].addVector(0,3);//check right half of board
			blankBoard.setSquare(piece.pieceCode,start[i]);//put pieces on middle
			assertTrue("Forward: Column:"+i+" has coords above the piece, Should be true",isLineEmpty(blankBoard,start[i],end[i]));
			assertTrue("Backward: Column:"+i+" has coords above the piece, Should be true",isLineEmpty(blankBoard,end[i],start[i]));
			end[i].setCoords(end[i].getX(),0);//test left half of the board
			assertTrue("Forward: Column:"+i+" has coords below of the piece, Should be true",isLineEmpty(blankBoard,end[i],start[i]));//reversed order because coords are reversed
			assertTrue("Backward: Column:"+i+" has coords below of the piece, Should be true",isLineEmpty(blankBoard,start[i],end[i]));
			start[i].setCoords(start[i].getX(),0);//test edges again to test if it can detect a filled line
			end[i].setCoords(end[i].getX(),BOARD_SIZE-1);
			assertFalse("Forward: Across Column:"+i+" has a piece obstructing it in the middle, should be false",isLineEmpty(blankBoard,start[i],end[i]));
			assertFalse("Backward: Across Column:"+i+" has a piece obstructing it in the middle, should be false",isLineEmpty(blankBoard,end[i],start[i]));
		}
	}

	/** Test the ability to reverse masks */
	@Test
	public void testReverseMask(){
		long mask=0b0101000000000000000000000000000000000000000000000000000000001101L;//pattern covers most possibilities
		long expected=0b1011000000000000000000000000000000000000000000000000000000001010L;//most fails will manifest in these end patterns
		long gotLong=reverseMask(mask);
		testLongStringByChar(maskString(expected),maskString(gotLong),8);
	}

	/** Test the correctness of mask printing */
	@Test
	public void testMaskString(){
		String got="", expected="1010000000001000000000000000000000000000100000000000000000001111";//arbitrary but should make it should cover reversed and other failure modes
		long mask=0b1010000000001000000000000000000000000000100000000000000000001111L;//the long
		got=maskString(mask);
		testLongStringByChar(expected,got,8);
	}
}