/*
File: PieceTest.java
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
 * Tests for the Piece Super Class
 * @author Dalton Herrewynen
 * @version 1
 */
public class PieceTests{
	final char[] pieceCode={'k','n','b','r','q','p'};
	Board board;

	@Before
	public void setup(){
		board=new Board(Board.CLEAR);
	}

	@After
	public void tearDown(){
		board=null;
	}

	/** Tests the piece generation functions used for pawn promotion */
	@Test
	public void testPieceTypeGeneration(){
		Piece black, white;
		for(pieceType type: pieceType.values()){
			white=Piece.generatePieceFromType(type,WHITE);
			black=Piece.generatePieceFromType(type,BLACK);
			if(type==pieceType.blank){
				assertNull("Blank WHITE piece should be null",white);
				assertNull("Blank BLACK piece should be null",black);
			}else{
				assert white!=null;
				assert black!=null;
				assertEquals("WHITE Piece should be on WHITE team",getTeamString(WHITE),getTeamString(white.getTeam()));
				assertEquals("WHITE Piece should be of correct type",type.toString(),white.getType().toString());
				assertEquals("BLACK Piece should be on BLACK team",getTeamString(BLACK),getTeamString(black.getTeam()));
				assertEquals("BLACK Piece should be of correct type",type.toString(),black.getType().toString());
			}
		}
	}

	/** Tests the piece generation functions used for pawn promotion */
	@Test
	public void testPieceCharGeneration(){
		Piece black, white;
		for(char code: pieceCode){
			white=Piece.generatePieceFromChar(setCaseFromTeam(code,WHITE));
			black=Piece.generatePieceFromChar(setCaseFromTeam(code,BLACK));
			if(code==' '){
				assertNull("Blank WHITE piece should be null",white);
				assertNull("Blank BLACK piece should be null",black);
			}else{
				assertEquals("WHITE Piece should be on WHITE team",getTeamString(WHITE),getTeamString(white.getTeam()));
				assertEquals("WHITE Piece should be of correct type",setCaseFromTeam(code,WHITE),white.getChar());
				assertEquals("BLACK Piece should be on BLACK team",getTeamString(BLACK),getTeamString(black.getTeam()));
				assertEquals("BLACK Piece should be of correct type",setCaseFromTeam(code,BLACK),black.getChar());
			}
		}
	}
}
