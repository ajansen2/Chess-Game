/*
File: PawnTest.java
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
import java.util.Arrays;

import static org.junit.Assert.*;
import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
import static edu.kingsu.SoftwareEngineering.Chess.UtilsForTests.*;

/**
 * Tests for the Pawn piece Class
 * @author Dalton Herrewynen
 * @version 2
 */
public class PawnTest{
	Board board;
	ArrayList<Integer> gotMoves;

	@Before
	public void setup(){
		board=new Board(Board.CLEAR);
		gotMoves=new ArrayList<>();
	}

	@After
	public void tearDown(){
		board=null;
		gotMoves=null;
	}

	/** Test the basic forward moves of the WHITE pawn */
	@Test
	public void testBasicMovesWHITE(){
		testBasicMovesCommon(WHITE);
	}

	/** Test the basic forward moves of the BLACK pawn */
	@Test
	public void testBasicMovesBLACK(){
		testBasicMovesCommon(BLACK);
	}

	/**
	 * Basic moves common method, both colors are similar
	 * @param team The team to test against
	 */
	public void testBasicMovesCommon(boolean team){
		Pawn pawn;
		Coords pawnPos;
		int deltaY;
		for(int x=0; x<BOARD_SIZE; ++x){//test a whole row
			pawn=new Pawn(team);
			if(team==WHITE){//I think the Ternary operator is easier to read in this case, but since you don't like it, here you go, it's not critical, they're both decent
				pawnPos=new Coords(x,1);
				deltaY=1;//if white, move up
			}else{
				pawnPos=new Coords(x,BOARD_SIZE-2);
				deltaY=-1;//if black, move down
			}
			//test first move, it can be one of two
			board.setSquare(pawn.pieceCode,pawnPos);
			board.setAllNotMoved();
			gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
			boolean singleSquareMove=UNSET, doubleSquareMove=UNSET;//move up or down 1 and 2 tiles respectively
			for(int move: gotMoves){
				int endX=Coords.indexToX(Move.getEndIndex(move));
				int endY=Coords.indexToY(Move.getEndIndex(move));
				assertEquals("X coordinate should not change",pawnPos.getX(),endX);
				if(endY==pawnPos.getY()+(deltaY)) singleSquareMove=SET;
				if(endY==pawnPos.getY()+(deltaY*2)) doubleSquareMove=SET;
			}
			assertTrue("Tile: "+pawnPos+" Did not find the move to go up/down by 1 square",singleSquareMove);
			assertTrue("Tile: "+pawnPos+" Did not find the move to go up/down by 2 squares",doubleSquareMove);
			board.setSquare(PieceCode.Blank,pawnPos);
			//test the subsequent move
			pawnPos.addVector(0,deltaY);//simulate a move
			board.setSquare(pawn.pieceCode,pawnPos);
			singleSquareMove=UNSET;
			doubleSquareMove=UNSET;
			for(int move: gotMoves){
				int endX=Coords.indexToX(Move.getEndIndex(move));
				int endY=Coords.indexToY(Move.getEndIndex(move));
				assertEquals("X coordinate should not change",pawnPos.getX(),endX);
				if(endY==pawnPos.getY()+(deltaY)) singleSquareMove=SET;
				if(endY==pawnPos.getY()+(deltaY*2)) doubleSquareMove=SET;
			}
			assertTrue("Tile: "+pawnPos+" Did not find the move to go up/down by 1 square after a move",singleSquareMove);
			assertFalse("Tile: "+pawnPos+" Should not find the move to go up/down by 2 squares after a move",doubleSquareMove);
			board.setSquare(PieceCode.Blank,pawnPos);
		}
	}

	/** Test the Move structure for WHITE Pawns */
	@Test
	public void testMoveAnatomyWHITE(){
		testMoveAnatomyCommon(BLACK);
	}

	/** Test the Move structure for BLACK Pawns */
	@Test
	public void testMoveAnatomyBLACK(){
		testMoveAnatomyCommon(BLACK);
	}

	/**
	 * Test the Move structure for Both Pawns
	 * @param team Which team to test for?
	 */
	public void testMoveAnatomyCommon(boolean team){
		Coords piecePos=new Coords(4,4);
		Pawn piece=new Pawn(team);
		board.setSquare(piece.pieceCode,piecePos);
		gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
		gotMoves=piece.getLegalMoves(board,piecePos.getIndex());
		assertFalse("There should be encoded move integers here",gotMoves.isEmpty());
		for(int move: gotMoves){
			assertEquals("Moves should have starting position correct",piecePos.toString(),Coords.orderedPair(Move.getStartIndex(move)));
			assertEquals("Move should be of Normal type (0)",Move.normalMove,Move.getSpecialCode(move));
			assertEquals("Piece code, converted to pretty printed name",PieceCode.decodePieceName(piece.pieceCode),Move.getPieceName(move));
		}
	}

	/** Test capture moves for BLACK pawn */
	@Test
	public void testCaptureBLACK(){
		testCaptureMovesCommon(BLACK);
	}

	/** Test capture moves for WHITE pawn */
	@Test
	public void testCaptureWHITE(){
		testCaptureMovesCommon(WHITE);
	}

	/**
	 * Test capture moves for either WHITE or BLACK Pawns
	 * @param team WHITE or BLACK to test
	 */
	public void testCaptureMovesCommon(boolean team){
		Coords pawnPos=new Coords(), enemyPos;
		Pawn pawn=new Pawn(team), friendly=new Pawn(team), enemy=new Pawn(!team);
		final int deltaY=(team==WHITE) ? 1 : -1;//if WHITE move up (Y positive) otherwise go down if BLACK (Y negative)
		final int posY=(team==WHITE) ? 1 : BOARD_SIZE-2;//if WHITE, start on row 1 like a game, if BLACK start 1 from top
		ArrayList<Integer> captureMoves, standardMoves;
		for(int i=0; i<BOARD_SIZE; ++i){
			pawnPos.setCoords(i,posY);
			board.setSquare(pawn.pieceCode,pawnPos);
			enemyPos=pawnPos.getShiftedCoord(-1,deltaY);//capture to the left
			if(enemyPos.isSet()==SET){//if enemy is on the board
				board.setSquare(enemy.pieceCode,enemyPos);
				gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
				captureMoves=findCaptures(gotMoves,board);
				standardMoves=findJustMoves(gotMoves,board);
				assertNotEquals("Tile "+pawnPos+" Left: Did not find a capture move",0,captureMoves.size());
				assertEquals("Tile "+pawnPos+" Left: Should move Pawn to captured square",enemyPos.toString(),Coords.orderedPair(Move.getEndIndex(captureMoves.get(0))));//Hack to make Coords turn index into human-readable string
				assertTrue("Tile: "+pawnPos+": capture move should flag capture", Move.isCapture(captureMoves.get(0)));
				assertNotEquals("Tile "+pawnPos+" Left: Did not find a standard move",0,standardMoves.size());

				board.setSquare(friendly.pieceCode,enemyPos);//test friendly piece, same place as enemy
				gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
				captureMoves=findCaptures(gotMoves,board);
				standardMoves=findJustMoves(gotMoves,board);
				assertEquals("Tile "+pawnPos+" Left: Should not try to capture a friendly piece",0,captureMoves.size());
				assertNotEquals("Tile "+pawnPos+" Left: Did not find a standard move",0,standardMoves.size());
				board.setSquare(PieceCode.Blank,enemyPos);//blank the square
			}

			enemyPos=pawnPos.getShiftedCoord(1,deltaY);//capture to the right
			if(enemyPos.isSet()==SET){//if enemy is on the board
				board.setSquare(enemy.pieceCode,enemyPos);
				gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
				captureMoves=findCaptures(gotMoves,board);
				standardMoves=findJustMoves(gotMoves,board);
				assertNotEquals("Tile "+pawnPos+" Right: Did not find a capture move",0,captureMoves.size());
				assertEquals("Tile "+pawnPos+" Right: Should move Pawn to captured square",enemyPos.toString(),Coords.orderedPair(Move.getEndIndex(captureMoves.get(0))));
				assertTrue("Tile: "+pawnPos+": capture move should flag capture", Move.isCapture(captureMoves.get(0)));
				assertNotEquals("Tile "+pawnPos+" Right: Did not find a standard move",0,standardMoves.size());

				board.setSquare(friendly.pieceCode,enemyPos);//test friendly piece, same place as enemy
				gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
				captureMoves=findCaptures(gotMoves,board);
				standardMoves=findJustMoves(gotMoves,board);
				assertEquals("Tile "+pawnPos+" Right: Should not try to capture a friendly piece",0,captureMoves.size());
				assertNotEquals("Tile "+pawnPos+" Right: Did not find a standard move",0,standardMoves.size());
				board.setSquare(PieceCode.Blank,enemyPos);//blank the square
			}

			enemyPos=pawnPos.getShiftedCoord(0,deltaY);//blocked
			board.setSquare(enemy.pieceCode,enemyPos);
			gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
			captureMoves=findCaptures(gotMoves,board);
			standardMoves=findJustMoves(gotMoves,board);
			assertEquals("Tile "+pawnPos+" Should not find a capture move when pawn blocked and nothing to its diagonal"
					,0,captureMoves.size());
			assertEquals("Tile "+pawnPos+" Should not find a standard move with pawn blocked",0,standardMoves.size());

			board.setSquare(friendly.pieceCode,enemyPos);//test friendly piece
			gotMoves=pawn.getLegalMoves(board,pawnPos.getIndex());
			captureMoves=findCaptures(gotMoves,board);
			standardMoves=findJustMoves(gotMoves,board);
			assertEquals("Tile "+pawnPos+" Should not find a capture move when pawn blocked and nothing to its diagonal"
					,0,captureMoves.size());
			assertEquals("Tile "+pawnPos+" Should not find a standard move with pawn blocked",0,standardMoves.size());

			board.setSquare(PieceCode.Blank,enemyPos);//blank the square
			board.setSquare(PieceCode.Blank,pawnPos);//blank the square
		}
	}

	/** Calls the En Passant testing method set for BLACK pawns */
	@Test
	public void testEnPassantGenerationBLACK(){
		testEnPassantGenerationCommon(BLACK);
	}

	/** Calls the En Passant testing method set for WHITE pawns */
	@Test
	public void testEnPassantGenerationWHITE(){
		testEnPassantGenerationCommon(WHITE);
	}

	/**
	 * Test either team's EnPassant move generation and application. Select which team tries to do the En Passant capture
	 * @param team WHITE or BLACK
	 */
	public void testEnPassantGenerationCommon(boolean team){
		int enemyRow, doubleMoveRow, attackDirection, dblMove;
		Coords enemyPos, enemyDblMoveDest, friendlyPos, attackDest;
		Pawn friendly=new Pawn(team), enemy=new Pawn(!team);
		if(team==WHITE){//when WHITE does the capturing
			enemyRow=6;//place enemy pawns on row 6 (rank 7)
			doubleMoveRow=4;//Row 4 (Rank 5) is where WHITE pawns go and BLACK pawns move to
			attackDirection=1;//Capturing pawns are WHITE and move up
		}else{//When BLACK is capturing
			enemyRow=1;//place enemy pawns on row 1 (rank 2)
			doubleMoveRow=3;//Row 3 (Rank 4) is where BLACK pawns go and WHITE pawns move to
			attackDirection=-1;//Capturing pawns are BLACK and move down
		}

		for(int x=0; x<BOARD_SIZE; ++x){//test cases when we should find En Passant moves
			enemyPos=new Coords(x,enemyRow);
			attackDest=enemyPos.getShiftedCoord(0,-attackDirection);//should be one square opposite direction of capturing
			enemyDblMoveDest=enemyPos.getShiftedCoord(0,-2*attackDirection);//enemy pawn double move destination, want test to work even if move generation fails so setting manually
			dblMove=Move.encode(Move.pawnDoubleMove,enemy.pieceCode,enemyPos.getIndex(),enemyDblMoveDest.getIndex());//the double move on start
			friendlyPos=new Coords(x-1,doubleMoveRow);//the attacking pawn, left of the attacked pawn, two spaces ahead of it
			if(friendlyPos.getSet()==SET){//test capturing pawn placed to the left if on the board
				board=new Board(Board.CLEAR);//blank the board
				board.setSquare(enemy.pieceCode,enemyPos.getIndex());//set the pawn to be captured
				board.setAllNotMoved();//set the pawn to be captured as unmoved
				board.setSquare(friendly.pieceCode,friendlyPos);//place attacking pawn

				board.makeMove(dblMove);//make the double move
				gotMoves=findMovesByCode(friendly.getLegalMoves(board,friendlyPos.getIndex()),Move.EnPassantCapture);//search for the EnPassant capture

				assertEquals("Left: Capturing Pawn: "+friendlyPos+" captured pawn: "+enemyPos+"->"+enemyDblMoveDest+" Should find 1 EnPassant move"
						,1,gotMoves.size());
				assertEquals("Left: Capturing Pawn: "+friendlyPos+" Should have En Passant capture destination at"+attackDest
						,attackDest.getIndex(),Move.getEndIndex(gotMoves.get(0)));
			}

			friendlyPos=new Coords(x+1,doubleMoveRow);//the attacking pawn, right of the attacked pawn, two spaces ahead of it
			if(friendlyPos.getSet()==SET){//test capturing pawn placed to the right if on the board
				board=new Board(Board.CLEAR);//blank the board
				board.setSquare(enemy.pieceCode,enemyPos.getIndex());//set the pawn to be captured
				board.setAllNotMoved();//set the pawn to be captured as unmoved
				board.setSquare(friendly.pieceCode,friendlyPos);//place attacking pawn

				board.makeMove(dblMove);//make the double move
				gotMoves=findMovesByCode(friendly.getLegalMoves(board,friendlyPos.getIndex()),Move.EnPassantCapture);//search for the EnPassant capture

				assertEquals("Right: Capturing Pawn: "+friendlyPos+" captured pawn: "+enemyPos+"->"+enemyDblMoveDest+" Should find 1 EnPassant move"
						,1,gotMoves.size());
				assertEquals("right: Capturing Pawn: "+friendlyPos+" Should have En Passant capture destination at"+attackDest
						,attackDest.getIndex(),Move.getEndIndex(gotMoves.get(0)));
			}
		}
	}

	/** Calls the Promotion move generator tester for BLACK */
	@Test
	public void testPawnPromotionBLACK(){
		testPawnPromotionCommon(BLACK);
	}

	/** Calls the Promotion move generator tester for WHITE */
	@Test
	public void testPawnPromotionWHITE(){
		testPawnPromotionCommon(WHITE);
	}

	/**
	 * The test code for either team's promotion move generation
	 * @param team WHITE or BLACK
	 */
	public void testPawnPromotionCommon(boolean team){
		int lastRow=(team==WHITE) ? BOARD_SIZE-1 : 0;//if WHITE then go to top of board, if BLACK then go to bottom
		int direction=(team==WHITE) ? 1 : -1;//if WHITE then go up, if BLACK then go down
		int pieceCodeOffset=(team==WHITE) ? 0 : 1;//if WHITE then we don't add 1 to the codes, if BLACK we do because BLACK is on every other PieceCode
		int[] foundPromotions=new int[PieceCode.PIECE_TYPES];
		Board beforeMove;
		Coords before, after;
		Pawn pawn=new Pawn(team);

		for(int i=0; i<BOARD_SIZE; ++i){//set up the board
			board.setSquare(pawn.pieceCode,i,(lastRow-direction));//place a line of pawns at the final row
		}
		beforeMove=new Board(board);//save the board state

		for(int x=0; x<BOARD_SIZE; ++x){//loop over each square in the row
			Arrays.fill(foundPromotions,0);//0 out the array
			after=new Coords(x,lastRow);//the last row in this column
			before=after.getShiftedCoord(0,-direction);//the row behind the last row, note the negative sign
			String errMsgData="Tile "+before+"->"+after+" ";
			board=new Board(beforeMove);
			gotMoves=pawn.getLegalMoves(board,before.getIndex());
			for(int move: gotMoves){
				++foundPromotions[Move.getPieceCode(move)];//count each instance of each piece code
			}

			assertEquals(errMsgData+"Should not find a promotion to BLACK pawns",0,foundPromotions[PieceCode.PawnB]);//no pawns
			assertEquals(errMsgData+"Should not find a promotion to WHITE pawns",0,foundPromotions[PieceCode.PawnW]);
			for(int i=pieceCodeOffset+PieceCode.RookW; i<PieceCode.PIECE_TYPES; i+=2){//search the other types, start at the Rooks because I put them just above the pawns
				assertEquals(errMsgData+"Should find 1 promotion move to "+PieceCode.decodePieceName(i),1,foundPromotions[i]);//look up each code count in the integer array
			}
		}
	}
}
