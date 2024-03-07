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

import static org.junit.Assert.*;
import static edu.kingsu.SoftwareEngineering.Chess.Types.*;
import static edu.kingsu.SoftwareEngineering.Chess.PieceCode.*;

/**
 * Tests for Dalton's Board class (a bitboard implementation)
 * @author Dalton Herrewynen
 * @version 2
 */
public class BoardTest{
	Board board;//normal game board
	Board blankBoard;//blank
	Board customBoard;//specially crafted to show faults in setters and getters
	int pawnRowNum, knightRowNum, rookRowNum;
	Coords KingPos, QueenPos;
	Piece expected, got;
	BoardState blankState, defaultState;

	@Before
	public void setupBoard(){
		board=new Board(Board.DEFAULT);
		blankBoard=new Board(Board.CLEAR);
		defaultState=board.saveState();//save default state
		blankState=blankBoard.saveState();//save the blank board
		//customized board
		customBoard=new Board(Board.CLEAR);
		//positions and row numbers
		KingPos=new Coords(0,0);
		QueenPos=new Coords(0,BOARD_SIZE-1);
		pawnRowNum=1;//2nd from bottom row: pawns
		knightRowNum=3;//middle row: knights
		rookRowNum=BOARD_SIZE-2;//2nd from top row: rooks
		//load it
		customBoard.setSquare(PieceCode.KingB,KingPos);//left bottom is king
		customBoard.setSquare(PieceCode.QueenB,QueenPos);//left top is queen
		for(int i=0; i<BOARD_SIZE; ++i){
			customBoard.setSquare(PieceCode.PawnB,Coords.XYToIndex(i,pawnRowNum));
			customBoard.setSquare(PieceCode.KnightB,Coords.XYToIndex(i,knightRowNum));
			customBoard.setSquare(PieceCode.RookB,Coords.XYToIndex(i,rookRowNum));
		}
		customBoard.setAllNotMoved();//only needed for custom boards
		got=null;
		expected=null;
	}

	@After
	public void tearDownBoard(){
		board=null;
		blankBoard=null;
		customBoard=null;
		blankState=null;
		defaultState=null;
		got=null;
		expected=null;
	}

	/** Testing to be sure the square validity checker works */
	@Test
	public void testIsSquareValid(){
		for(int x=0; x<BOARD_SIZE; ++x){//testing all tiles on the board
			for(int y=0; y<BOARD_SIZE; ++y){
				assertTrue("Square ("+x+","+y+") should be on the board but isn't.",Board.isValidSquare(x,y));
			}
		}
		for(int i=-1; -BOARD_SIZE>i; --i){//start at -1 go to negative of BOARD_SIZE
			assertFalse("Square ("+i+",0) should be invalid but isn't",Board.isValidSquare(i,0));//testing negative row, valid column
			assertFalse("Square (0,"+i+") should be invalid but isn't",Board.isValidSquare(0,i));//testing valid row, negative column
		}
	}

	/** Tests the get square method */
	@Test
	public void testGetter(){
		//bottom left: King
		got=customBoard.getSquareObj(KingPos);
		expected=new King(BLACK);
		assertEquals("Bottom left",expected.toString(),got.toString());
		//top left: Queen
		got=customBoard.getSquareObj(QueenPos);
		expected=new Queen(BLACK);
		assertEquals("Top left",expected.toString(),got.toString());
		//test rows
		for(int i=0; i<BOARD_SIZE; ++i){
			//Pawns in row 2
			expected=new Pawn(BLACK);
			got=customBoard.getSquareObj(new Coords(i,pawnRowNum));
			assertEquals("At ("+i+","+pawnRowNum+")",expected.toString(),got.toString());
			//Knights in row 3
			expected=new Knight(BLACK);
			got=customBoard.getSquareObj(new Coords(i,knightRowNum));
			assertEquals("At ("+i+","+knightRowNum+")",expected.toString(),got.toString());
			//Rooks second from the top
			expected=new Rook(BLACK);
			got=customBoard.getSquareObj(new Coords(i,rookRowNum));
			assertEquals("At ("+i+","+rookRowNum+")",expected.toString(),got.toString());
		}
	}

	/** Testing setting a square from a mask */
	@Test
	public void testSetterMask(){
		long mask;
		for(long i=0; i<64; ++i){
			mask=(1L<<i);
			assertNull("Blank board should not have a piece at "+i,PieceCode.pieceObj(blankBoard.getSquare(mask)));
			expected=new Pawn(WHITE);
			blankBoard.setSquare(expected.pieceCode,mask);
			assertEquals("Blank board at "+i,expected.toString(),PieceCode.decodePieceName(blankBoard.getSquare(mask)));
		}
	}

	/** Testing setting a square from x and y integers */
	@Test
	public void testSetterCoords(){
		Coords square;
		Piece expected;
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			square=new Coords(i);
			assertNull("Blank board should not have a piece at "+square,blankBoard.getSquareObj(square));
			expected=new Pawn(WHITE);
			blankBoard.setSquare(expected.pieceCode,square);
			assertEquals("Blank board at "+square,expected.toString(),PieceCode.decodePieceName(blankBoard.getSquare(square)));
		}
	}

	/** Testing setting a square from a Coords object */
	@Test
	public void testSetterXYInts(){
		Coords square=new Coords();
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			square.setIndex(i);
			assertNull("Blank board should not have a piece at "+square,blankBoard.getSquareObj(square));
			expected=new Pawn(WHITE);
			blankBoard.setSquare(expected.pieceCode,square.getX(),square.getY());
			assertEquals("Blank board at "+square,expected.toString(),PieceCode.decodePieceName(blankBoard.getSquare(square)));
		}
	}

	/** Test the board rotation function */
	@Test
	public void TestBoardRotation(){
		Board afterRotate=customBoard.getRotatedBoard();
		Coords before=new Coords(), after=new Coords();
		//each square
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			before.setIndex(i);
			after.setIndex(i);
			after.rotate180();//rotate the coordinate to match the rotated board, assuming Coords passed its tests
			int expected=customBoard.getSquare(before);
			int got=afterRotate.getSquare(after);
			boolean expectedMoved=customBoard.hasNotMoved(before);
			boolean gotMoved=afterRotate.hasNotMoved(after);
			assertEquals("At: "+before+" -> "+after,PieceCode.decodePieceName(expected),PieceCode.decodePieceName(got));
			assertEquals("At: "+before+" -> "+after,expectedMoved,gotMoved);
		}
	}

	/** Test the board rotation function which rotates the current board rather than returning a new one */
	@Test
	public void TestBoardSelfRotation(){
		Board afterRotate=new Board(customBoard);
		afterRotate.rotateBoard();
		Coords before=new Coords(), after=new Coords();
		//each square
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			before.setIndex(i);
			after.setIndex(i);
			after.rotate180();//rotate the coordinate to match the rotated board, assuming Coords passed its tests
			int expected=customBoard.getSquare(before);
			int got=afterRotate.getSquare(after);
			boolean expectedMoved=customBoard.hasNotMoved(before);
			boolean gotMoved=afterRotate.hasNotMoved(after);
			assertEquals("At: "+before+" -> "+after,PieceCode.decodePieceName(expected),PieceCode.decodePieceName(got));
			assertEquals("At: "+before+" -> "+after,expectedMoved,gotMoved);
		}
	}

	/** Test setters and tracking of the WHITE king */
	@Test
	public void kingLocatorTestWHITE(){
		kingLocatorTestBody(WHITE);
	}

	/** Test setters and tracking of the BLACK king */
	@Test
	public void kingLocatorTestBLACK(){
		kingLocatorTestBody(BLACK);
	}

	/** Test searching pieces */
	@Test
	public void testPieceSearch(){
		int gotIndex;
		char pieceChar, searchChar;
		for(int code=0; code<PieceCode.PIECE_TYPES; ++code){//test on all pieces
			pieceChar=PieceCode.decodeChar(code);//set the human friendly Piece Char for this code
			for(int i=0; i<TOTAL_SQUARES; ++i){//test placed on all squares
				board.loadState(blankState);//blank it out
				board.setSquare(code,i);//set one and only one piece
				for(int searchCode=0; searchCode<PieceCode.PIECE_TYPES; ++searchCode){//search all piece types
					searchChar=PieceCode.decodeChar(searchCode);//Human friendly piece code we are looking for
					int foundIndex=Coords.maskToIndex(board.searchPiece(searchCode));//search all squares for this code
					if(searchCode!=code){//if this code is not the same code I placed, expect a failure flag
						assertEquals("Square "+i+" piece "+pieceChar+" found "+searchChar+" at "+foundIndex+
								" Should not have found anything square "+foundIndex+" content "+
								PieceCode.decodePieceName(board.getSquare(foundIndex)),Coords.ERROR_INDEX,foundIndex);
					}else{//if same code searching as set, then expect same index as I set earlier
						assertEquals("Square "+i+" piece "+pieceChar+" found at incorrect Index, square "+
										foundIndex+" content "+PieceCode.decodePieceName(board.getSquare(foundIndex)),
								i,foundIndex);//expect found index to be same as i
					}
				}
			}
		}
	}

	/**
	 * Tests either King location tracker
	 * @param team the team to test against
	 */
	public void kingLocatorTestBody(boolean team){
		King king=new King(team);
		Coords got, expected=new Coords();
		assertEquals("There are no Kings on the board yet so should be UNSET",UNSET,blankBoard.getKingPosObj(team).isSet());
		assertEquals("There are no Kings on the board yet so should be UNSET",UNSET,blankBoard.getKingPosObj(!team).isSet());
		for(int i=0; i<TOTAL_SQUARES; ++i){//loop across all squares
			expected.setIndex(i);
			blankBoard.setSquare(king.pieceCode,expected);
			got=blankBoard.getKingPosObj(team);
			assertEquals("At square "+i+", King position was not correctly tracked",expected.toString(),got.toString());
			blankBoard.setSquare(PieceCode.Blank,expected);//blank out this tile if it was set
		}
		expected.setIndex(0);//set a wrong color king here
		blankBoard.setSquare(new King(!team).pieceCode,expected);
		for(int i=1; i<TOTAL_SQUARES; ++i){//loop across all squares, should still track correctly with a second king of wrong color
			expected.setIndex(i);
			blankBoard.setSquare(king.pieceCode,expected);
			got=blankBoard.getKingPosObj(team);
			assertEquals("At square "+i+", King position was not correctly tracked",expected.toString(),got.toString());
			blankBoard.setSquare(PieceCode.Blank,expected);//blank out this tile if it was set
		}
	}

	/** Tests conversion of index to a single bit mask */
	@Test
	public void indexMaskTest(){
		long expected=1L;
		for(int i=0; i<64; ++i){//longs always have 64 bits as per definition of a long
			long mask=Board.indexToMask(i);
			assertEquals("Position: "+i,expected,mask);
			expected*=2;
		}
	}

	/** Test tracking of which pieces have and have not been moved */
	@Test
	public void testHasMovedTrack(){
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			blankBoard=new Board(Board.CLEAR);
			blankBoard.setSquare(PieceCode.PawnW,i);
			blankBoard.setAllNotMoved();
			assertTrue("Piece has not moved yet, should not be seen as moved",blankBoard.hasNotMoved(i));
			blankBoard.setSquare(PieceCode.PawnB,i);//set a piece, same or otherwise, should mean the flag gets tripped and the piece has now moved
			assertFalse("Piece has now been moved, should be marked as such",blankBoard.hasNotMoved(i));
		}
	}

	/** Test the ability to save and load a board state */
	@Test
	public void testLoadSaveState(){
		BoardState state=board.saveState();
		blankBoard.loadState(state);
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			assertEquals("Tile: "+Coords.orderedPair(i)+" pieces should match after load",board.getSquare(i),blankBoard.getSquare(i));
			assertEquals("Tile: "+Coords.orderedPair(i)+" EnPassant should match after load",board.getEnPassant() & (1L<<i),blankBoard.getEnPassant() & (1L<<i));
			assertEquals("Tile: "+Coords.orderedPair(i)+" hasMoved() should match after load",board.hasNotMoved(i),blankBoard.hasNotMoved(i));
		}
	}

	/** Test the ability to load from another board object */
	@Test
	public void testLoadBoard(){
		blankBoard.loadState(board);
		for(int i=0; i<TOTAL_SQUARES; ++i){//Loop over all squares
			assertEquals("Tile: "+Coords.orderedPair(i)+" pieces should match after load",board.getSquare(i),blankBoard.getSquare(i));
			assertEquals("Tile: "+Coords.orderedPair(i)+" EnPassant should match after load",board.getEnPassant() & (1L<<i),blankBoard.getEnPassant() & (1L<<i));
			assertEquals("Tile: "+Coords.orderedPair(i)+" hasMoved() should match after load",board.hasNotMoved(i),blankBoard.hasNotMoved(i));
		}
	}

	/** Testing the ability to make a move using an encoded move integer */
	@Test
	public void testMakeMove(){
		int pieceCode=PieceCode.QueenW, dest, move;
		for(int start=0; start<TOTAL_SQUARES; ++start){//going to test basic moves by placing a queen and moving it around manually
			board.loadState(blankState);//blank out the board
			if(Coords.indexToY(start)<7){//if not on top row, go up one row
				dest=Coords.XYToIndex(Coords.indexToX(start),Coords.indexToY(start)+1);
			}else{//if on top row go down a row
				dest=Coords.XYToIndex(Coords.indexToX(start),Coords.indexToY(start)-1);
			}
			move=Move.encodeNormal(pieceCode,start,dest);//encode the move
			board.setSquare(pieceCode,start);//set the piece on the square
			board.makeMove(move);
			assertEquals("Move "+start+" -> "+dest+" Incorrect piece destination "+Coords.maskToIndex(board.searchPiece(pieceCode))
					,pieceCode,board.getSquare(dest));//check if it's actually present where it should be
		}
	}

	/** King Side castling ability */
	@Test
	public void testKingSideCastling(){
		int move;
		//Test WHITE side
		Coords expectedCastle=new Coords(5,0), expectedKing=new Coords(6,0);//where the castles and kings should go to
		Coords placeCastle=new Coords(7,0), placeKing=new Coords(4,0);//where to place the kings and castles

		board.loadState(blankState);//Set up the board, load pieces, set as not moved
		board.setSquare(KingW,placeKing);
		board.setSquare(RookW,placeCastle);
		board.setAllNotMoved();//make all not moved yet

		move=Move.encode(Move.kSideCastle,KingW,placeKing.getIndex(),0);//end coordinates are irrelevant for castling, only record position of the king
		board.makeMove(move);//generate and make the move

		//check if it was successful
		assertEquals("Expected a WHITE King at "+expectedKing,decodePieceName(KingW),decodePieceName(board.getSquare(expectedKing)));
		assertEquals("Expected a WHITE Rook at "+expectedCastle,decodePieceName(RookW),decodePieceName(board.getSquare(expectedCastle)));

		//Test BLACK side
		expectedCastle.addVector(0,7);
		expectedKing.addVector(0,7);
		placeCastle.addVector(0,7);
		placeKing.addVector(0,7);//Move up to BLACK side

		board.loadState(blankState);//Set up the board, load pieces, set as not moved
		board.setSquare(KingB,placeKing);
		board.setSquare(RookB,placeCastle);
		board.setAllNotMoved();//make all not moved yet

		move=Move.encode(Move.kSideCastle,KingB,placeKing.getIndex(),0);//end coordinates are irrelevant for castling, only record position of the king
		board.makeMove(move);//generate and make the move

		//check if it was successful
		assertEquals("Expected a BLACK King at "+expectedKing,decodePieceName(KingB),decodePieceName(board.getSquare(expectedKing)));
		assertEquals("Expected a BLACK Rook at "+expectedCastle,decodePieceName(RookB),decodePieceName(board.getSquare(expectedCastle)));
	}

	/** Queen Side castling ability */
	@Test
	public void testQueenSideCastling(){
		int move;
		//Test WHITE side
		Coords expectedCastle=new Coords(3,0), expectedKing=new Coords(2,0);//where the castles and kings should go to
		Coords placeCastle=new Coords(0,0), placeKing=new Coords(4,0);//where to place the kings and castles

		board.loadState(blankState);//set up the board, load pieces
		board.setSquare(KingW,placeKing);
		board.setSquare(RookW,placeCastle);
		board.setAllNotMoved();//make all not moved yet

		move=Move.encode(Move.qSideCastle,KingW,placeKing.getIndex(),0);//end coordinates are irrelevant for castling, only record position of the king
		board.makeMove(move);//generate and make the move

		//Check if it was successful
		assertEquals("Expected a WHITE King at "+expectedKing,decodePieceName(KingW),decodePieceName(board.getSquare(expectedKing)));
		assertEquals("Expected a WHITE Rook at "+expectedCastle,decodePieceName(RookW),decodePieceName(board.getSquare(expectedCastle)));

		//Test BLACK side
		expectedCastle.addVector(0,7);
		expectedKing.addVector(0,7);
		placeCastle.addVector(0,7);
		placeKing.addVector(0,7);//Move up to BLACK side

		board.loadState(blankState);//set up the board, load pieces
		board.setSquare(KingB,placeKing);
		board.setSquare(RookB,placeCastle);
		board.setAllNotMoved();//make all not moved yet

		move=Move.encode(Move.qSideCastle,KingB,placeKing.getIndex(),0);//end coordinates are irrelevant for castling, only record position of the king
		board.makeMove(move);//generate and make the move

		//Check if it was successful
		assertEquals("Expected a BLACK King at "+expectedKing,decodePieceName(KingB),decodePieceName(board.getSquare(expectedKing)));
		assertEquals("Expected a BLACK Rook at "+expectedCastle,decodePieceName(RookB),decodePieceName(board.getSquare(expectedCastle)));
	}

	/** Test En Passant rules for BLACK */
	@Test
	public void testEnPassantBLACK(){
		int move;
		Coords start=new Coords(), end;
		for(int i=0; i<BOARD_SIZE; ++i){//Test for all Black pawns
			start.setCoords(i,BOARD_SIZE-2);//black pawns 1 row from top
			end=start.getShiftedCoord(0,-2);//go down by 2

			assertEquals("Tile: "+start+" -> "+end+" Should not EnPassant the starting position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the start square
					,0,board.getEnPassant() & (1L<<start.getIndex()));//with masking just the start square
			assertEquals("Tile: "+start+" -> "+end+" Should not EnPassant the ending position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the End square
					,0,board.getEnPassant() & (1L<<end.getIndex()));//with masking just the end square

			move=Move.encode(Move.pawnDoubleMove,PawnB,start.getIndex(),end.getIndex());//generate the move
			board.makeMove(move);//make the move

			assertEquals("Tile: "+start+" -> "+end+" Should not EnPassant the starting position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the start square
					,0,board.getEnPassant() & (1L<<start.getIndex()));//with masking just the start square
			assertEquals("Tile: "+start+" -> "+end+" Should EnPassant the ending position, Mask filter extracts just this square if correct"//Check that we can Enpassant the End square
					,end.getIndex(),Coords.maskToIndex(board.getEnPassant() & (1L<<end.getIndex())));//with masking just the end square
		}
	}

	/** Test En Passant rules for WHITE */
	@Test
	public void testEnPassantWHITE(){
		int move;
		Coords start=new Coords(), end;
		for(int i=0; i<BOARD_SIZE; ++i){//Test for all White pawns
			start.setCoords(i,1);//white pawns on bottom
			end=start.getShiftedCoord(0,2);//go up by 2

			assertEquals("Tile: "+start+" -> "+end+" Should not EnPassant the starting position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the start square
					,0,board.getEnPassant() & (1L<<start.getIndex()));//with masking just the start square
			assertEquals("Tile: "+start+" -> "+end+" Should not EnPassant the ending position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the End square
					,0,board.getEnPassant() & (1L<<end.getIndex()));//with masking just the end square

			move=Move.encode(Move.pawnDoubleMove,PawnW,start.getIndex(),end.getIndex());//generate the move
			board.makeMove(move);//make the move

			assertEquals("Tile: "+start+" -> "+end+" Should Not EnPassant the starting position, Mask filters only this square so the result should be all 0's"//Check that we cannot Enpassant the start square
					,0,board.getEnPassant() & (1L<<start.getIndex()));//with masking just the start square
			assertEquals("Tile: "+start+" -> "+end+" Should EnPassant the ending position, Mask filter extracts just this square if correct"//Check that we can Enpassant the End square
					,end.getIndex(),Coords.maskToIndex(board.getEnPassant() & (1L<<end.getIndex())));//with masking just the end square
		}
	}

	/** Test searching of all allied pieces */
	@Test
	public void testAlliedPiecesSearch(){
		long alliedMask;
		String errPrefix;
		for(int piece=0; piece<PIECE_TYPES; ++piece){//for each piece
			for(int i=0; i<TOTAL_SQUARES; ++i){//test placing at each square
				errPrefix="Tile:"+Coords.orderedPair(i)+" piece: "+PieceCode.decodeChar(piece)+" ";
				board=new Board(Board.CLEAR);//blank the board
				board.setSquare(piece,i);//place a piece at this square
				alliedMask=board.alliedPieceMask(!PieceCode.decodeTeam(piece));//get for the other team (works for both teams)
				assertEquals(board+errPrefix+"Should not find any Pieces for the other team",0,alliedMask);
				alliedMask=board.alliedPieceMask(PieceCode.decodeTeam(piece));//get for this team
				assertEquals(board+errPrefix+"Incorrect allied Piece mask (piece: "+PieceCode.decodePieceName(piece)+
						" Location: expected: "+Coords.orderedPair(i)+" got: "+Coords.orderedPair(Coords.maskToIndex(alliedMask))
						,i,Coords.maskToIndex(alliedMask));
			}
		}
	}

	/** Test memory pointers */
	@Test
	public void testMemoryPointersCopy(){
		Board testBoard=new Board(blankBoard);
		assertNotSame("Board copying should have copied content, not pointer",testBoard,blankBoard);
		for(int i=0; i<PIECE_TYPES; ++i){//test basic piece array copy
			blankBoard=new Board(Board.CLEAR);
			testBoard=new Board(blankBoard);
			blankBoard.setSquare(i,0,1);
			assertEquals("The piece should not have been copied from board to board since setting was after copy"
					,PieceCode.Blank,testBoard.getSquare(0,1));
		}

		for(int i=0; i<BOARD_SIZE; ++i){//test EnPassant flag copying
			blankBoard=new Board(Board.CLEAR);
			testBoard=new Board(blankBoard);
			blankBoard.setSquare(PawnW,i,1);
			blankBoard.setAllNotMoved();
			blankBoard.makeMove(Move.encode(Move.pawnDoubleMove,PawnW,Coords.XYToIndex(i,1),Coords.XYToIndex(i,3)));//make double move
			assertNotEquals("EnPassant flag should have been set (use other tests for its validity)",0,blankBoard.getEnPassant());
			assertNotEquals("The EnPassant should not have been copied from board to board since setting was after copy"
					,testBoard.getEnPassant(),blankBoard.getEnPassant());
		}
	}

	/** Test the copy constructor */
	@Test
	public void testCopyConstructor(){
		blankBoard=new Board(board);
		assertEquals("EnPassant masks need to match",board.getEnPassant(),blankBoard.getEnPassant());
		assertEquals("Move tracking masks need to match",board.getUnmoved(),blankBoard.getUnmoved());
		for(int i=0; i<PIECE_TYPES; ++i){
			assertEquals("Piece tracking masks for code: "+i+" need to match",board.searchPiece(i),blankBoard.searchPiece(i));
		}
	}
}
