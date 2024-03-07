package edu.kingsu.SoftwareEngineering.Chess.Adi;

import java.util.*;

public class Knight extends Piece{

	//all possible move cordinates that the knight can have given its current position
	//these are possible but invalid sometimes
	private final static int[] CANDIDATE_MOVE_COORDINATES={-17,-15,-10,-6,6,10,15,17};

	//constructoe to set the values
	Knight(final int piecePosition,final Alliance pieceAlliance){
		super(piecePosition,pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMove(final Board board){
		final List<Move> legalMoves=new ArrayList<>();

		for(final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
			final int candidateDestinationCoordiante=this.piecePosition+currentCandidateOffset;

			//true means it is a valid tile coordinate amd we dont go out of bounds
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordiante)){
				if(isFirstColumnExclusion(this.piecePosition,currentCandidateOffset)
						|| isEighthColumnExclusion(this.piecePosition,currentCandidateOffset)
						|| isSecondColumnExclusion(this.piecePosition,currentCandidateOffset)
						|| isSeventhColumnExclusion(this.piecePosition,currentCandidateOffset)){
					continue;
				}

				final Tile candidateDestinationTile=board.getTile(candidateDestinationCoordiante);

				//if the tile is not occupoied
				if(!candidateDestinationTile.isTileOccupied()){
					legalMoves.add(new MajorMove(board,this,candidateDestinationCoordiante));
				}else{//if tile is occupied
					final Piece pieceAtDestination=candidateDestinationTile.getPiece();
					final Alliance pieceAlliance=pieceAtDestination.getPieceAlliance();

					//enemy piece
					if(this.pieceAlliance!=pieceAlliance){
						legalMoves.add(new AttackMove(board,this,candidateDestinationCoordiante,pieceAtDestination));
					}
				}
			}
		}
		return legalMoves;
	}

	//first edge case- if knight is sitting on first column
	private static boolean isFirstColumnExclusion(final int currentPosition,final int candidateOffset){
		//values -17,-10,6,15 are the values which breaks the rules when knight is in first column
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset==-17) || (candidateOffset==-10)
				|| (candidateOffset==6) || (candidateOffset==15));
	}

	private static boolean isSecondColumnExclusion(final int currentPosition,final int candidateOffset){
		return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset==-10) || (candidateOffset==6));
	}

	private static boolean isSeventhColumnExclusion(final int currentPosition,final int candidateOffset){
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset==-6) || (candidateOffset==10));
	}

	private static boolean isEighthColumnExclusion(final int currentPosition,final int candidateOffset){
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset==-15) || (candidateOffset==-6)
				|| (candidateOffset==10) || (candidateOffset==17));
	}
}
