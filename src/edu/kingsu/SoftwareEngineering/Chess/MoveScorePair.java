package edu.kingsu.SoftwareEngineering.Chess;

/**
 * Stores a move and a score pair, want to keep SlowMove as minimal as possible
 * @param move  The move record
 * @param score The score integer
 * @author Dalton Herrewynen
 * @version 1
 */
public record MoveScorePair(int move,int score){
	/**
	 * Gets the score of the move for each team (WHITE or BLACK)
	 * @param team which team is this score for? (WHITE or BLACK)
	 * @return The score if teams match or the negative score if the teams don't match
	 */
	public int getTeamScore(boolean team){
		if(PieceCode.decodeTeam(Move.getPieceCode(move))==team && score>=0) return score;
		else return -score;//go negative
	}
}
