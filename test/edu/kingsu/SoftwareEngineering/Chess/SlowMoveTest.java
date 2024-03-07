/*
File: SlowMoveTest.java
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
import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

/**
 * Tests for the SlowMove helper class
 * @author Dalton Herrewynen
 * @version 1
 */
public class SlowMoveTest{
	Board board;

	@Before
	public void setup(){
		board=new Board(Board.CLEAR);
	}

	@After
	public void tearDown(){
		board=null;
	}

	/** Test the minimal constructor */
	@Test
	public void moveByConstructor(){
		Coords initPos=new Coords(2,2);
		Coords endPos=new Coords(4,4);
		board.setSquare(new Queen(BLACK),initPos);
		SlowMove testMove=new SlowMove(initPos,endPos,board.getSquareObj(initPos).getChar(),moveTypes.justMove);
		assertEquals("Expected and received start coordinate did not match",
				initPos.getIndex(),testMove.start().getIndex());
		assertEquals("Expected and received end coordinate did not match",
				endPos.getIndex(),testMove.end().getIndex());
	}

	/** Ensure that Coord objects are not referenced by the calling method, causes issues */
	@Test
	public void memoryPointerTest(){
		Coords startPos=new Coords(2,2);
		Coords endPos=new Coords(4,4);
		board.setSquare(new Queen(BLACK),startPos);
		SlowMove canonical=new SlowMove(startPos,endPos,board.getSquareObj(startPos).getChar(),moveTypes.justMove,' ',0,"",Winner.none,causeOfGameover.none);
		SlowMove minimal=new SlowMove(startPos,endPos,board.getSquareObj(startPos));
		assertEquals("Canonical constructor: Values should match: start Coord",startPos.toString(),canonical.start().toString());//value
		assertEquals("Canonical constructor: Values should match: end Coord",endPos.toString(),canonical.end().toString());
		assertNotSame("Canonical constructor: Should be a copy not the same instance, start Coord",startPos,canonical.start());
		assertNotSame("Canonical constructor: Should be a copy not the same instance, end Coord",endPos,canonical.end());
		assertEquals("Values should match: minimal constructor, start Coord",startPos.toString(),minimal.start().toString());//value
		assertEquals("Values should match: minimal constructor, end Coord",endPos.toString(),minimal.end().toString());
		assertNotSame("minimal constructor: Should be a copy not the same instance, start Coord",startPos,minimal.start());
		assertNotSame("minimal constructor: Should be a copy not the same instance, end Coord",endPos,minimal.end());
		//sanity check, shift each coord, then test again
		int startIndexOriginal=startPos.getIndex();
		int endIndexOriginal=endPos.getIndex();
		startPos.addVector(1,1);
		endPos.addVector(1,1);
		assertEquals("Canonical: start: Coordinate changes should not carry to the move",startIndexOriginal,canonical.start().getIndex());
		assertEquals("Canonical: end: Coordinate changes should not carry to the move",endIndexOriginal,canonical.end().getIndex());
		assertEquals("Minimal: start: Coordinate changes should not carry to the move",startIndexOriginal,minimal.start().getIndex());
		assertEquals("Minimal: end: Coordinate changes should not carry to the move",endIndexOriginal,minimal.end().getIndex());
	}

	/** Test conversion to the new Move integer encoding pseudo-object */
	@Test
	public void testEncodeMove(){
		int moveSrc, move;
		SlowMove slowMove;
		Coords startPos=new Coords(), endPos=new Coords();
		for(int start=0; start<TOTAL_SQUARES; ++start){
			for(int end=0; end<TOTAL_SQUARES; ++end){//check all possible combinations of start and end location
				startPos.setIndex(start);
				endPos.setIndex(end);
				if(startPos.getY()==1 && endPos.getY()==3){//pawn double move on start (white)
					slowMove=new SlowMove(startPos,endPos,'p');
					move=slowMove.encodeFASTMove();
					moveSrc=Move.encode(Move.pawnDoubleMove,PieceCode.PawnW,start,end);
					assertEquals("White Pawn Double Move:"+Move.describe(moveSrc)+Move.describe(move),Move.describe(moveSrc),Move.describe(move));
				}else if(startPos.getY()==6 && endPos.getY()==4){//pawn double move on start (black)
					slowMove=new SlowMove(startPos,endPos,'P');
					move=slowMove.encodeFASTMove();
					moveSrc=Move.encode(Move.pawnDoubleMove,PieceCode.PawnB,start,end);
					assertEquals("Black Pawn Double Move:"+Move.describe(moveSrc)+Move.describe(move),Move.describe(moveSrc),Move.describe(move));
				}
				for(int piece=2; piece<PieceCode.PIECE_TYPES; ++piece){//just move
					moveSrc=Move.encodeNormal(piece,start,end);
					slowMove=new SlowMove(startPos,endPos,PieceCode.pieceObj(piece));
					move=slowMove.encodeFASTMove();
					assertEquals("Just Move:"+Move.describe(moveSrc)+Move.describe(move),Move.describe(moveSrc),Move.describe(move));
				}
			}
		}
		for(int i=0; i<BOARD_SIZE; ++i){//promotion for WHITE
			startPos.setCoords(i,6);
			endPos.setCoords(i,7);
			int start=startPos.getIndex();
			int end=endPos.getIndex();
			for(int piece=2; piece<PieceCode.PIECE_TYPES; piece+=2){//WHITE
				slowMove=new SlowMove(startPos,endPos,'p',moveTypes.pawnPromote,PieceCode.decodeChar(piece));
				move=slowMove.encodeFASTMove();
				moveSrc=Move.encode(Move.pawnPromote,piece,start,end);
				assertEquals("Promotion:"+Move.describe(moveSrc)+Move.describe(move),Move.describe(moveSrc),Move.describe(move));
			}
		}
		for(int i=0; i<BOARD_SIZE; ++i){//promotion for BLACK
			startPos.setCoords(i,1);
			endPos.setCoords(i,0);
			int start=startPos.getIndex();
			int end=endPos.getIndex();
			for(int piece=3; piece<PieceCode.PIECE_TYPES; piece+=2){//BLACK
				slowMove=new SlowMove(startPos,endPos,'P',moveTypes.pawnPromote,PieceCode.decodeChar(piece));
				move=slowMove.encodeFASTMove();
				moveSrc=Move.encode(Move.pawnPromote,piece,start,end);
				assertEquals("Promotion:"+Move.describe(moveSrc)+Move.describe(move),Move.describe(moveSrc),Move.describe(move));
			}
		}
	}
}
