/*
Copyright (C) 2023 Christopher Bury. All Rights Reserved.
License is hereby granted to The Kings University to store, compile, run, and display this file for grading and educational purposes.
 */
package edu.kingsu.SoftwareEngineering.Chess;

import static edu.kingsu.SoftwareEngineering.Chess.Types.*;

import java.awt.*;

import javax.swing.*;

/**
 * Class that holds all types and enums that are globally used
 * @author Christopher Bury
 * @version 1
 */
public class TutorialHelper{
	public static final String INTRO_TUTORIAL_STRING="Welcome to the tutorial! This mode will walk you through a high-level chess game so you can see what it's all about.\n\n"
			+"The game we will walk you through is the match between chess grandmaster Garry Kasparov and Veselin Topalov in 1999.\n\n"
			+"Click on the blinking tiles to follow Kasparov's moves through the game, and these blurbs will give some explanation along the way.";

	private static final Font buttonFont=new Font("Dialog",Font.PLAIN,20);
	private static final String[][] TUTORIAL_GAME=new String[][]{
			//1
			new String[]{"e4","d6",
					"Kasparov starts by moving one of his pawns to the center in an early bid to take up space and make his presence known on the board."
			},
			//2
			new String[]{"d4","Nf6",
					"Topalov responds with a pawn push of his own, playing more defensively by not rushing to take center space right away.\n\n"
							+"In response, Kasparov chooses to push yet another pawn."
			},
			//3
			new String[]{"Nc3","g6",
					"Topalov moves a knight to protect the center, jumping over the line of his pawns.\n\n"
							+"Kasparov chooses to develop one of his knights in response."
			},
			//4
			new String[]{"Be3","Bg7",
					"Kasparov advances his bishop to e3, focusing on controlling key squares and setting up for future attacks.\n\n"
							+"Topalov responds by fianchettoing his bishop to g7, fortifying his king's defense and controlling the long diagonal."
			},
			//5
			new String[]{"Qd2","c6",
					"Kasparov aligns his queen with his bishop on d2, hinting at a potential kingside attack.\n\n"
							+"Topalov plays c6, strengthening his pawn structure and preparing for a potential queenside expansion."
			},
			//6
			new String[]{"f3","b5",
					"Kasparov reinforces his central pawns with f3, preparing for an eventual pawn push to g4.\n\n"
							+"Topalov counters by advancing his pawn to b5, beginning an expansion on the queenside."
			},
			//7
			new String[]{"Nge2","Nbd7",
					"Kasparov develops his knight to e2, keeping options open for both kingside and queenside play.\n\n"
							+"Topalov develops his other knight to d7, providing support for central pawns and preparing for kingside castle."
			},
			//8
			new String[]{"Bh6","Bxh6",
					"Kasparov aggressively exchanges his bishop for Topalov's fianchettoed bishop, aiming to weaken Topalov's kingside defenses.\n\n"
							+"Topalov accepts the trade, capturing with his bishop on h6."
			},
			//9
			new String[]{"Qxh6","Bb7",
					"Kasparov's queen captures the bishop on h6, intensifying the threat on the kingside.\n\n"
							+"Topalov responds by developing his bishop to b7, putting pressure on e4 and central squares."
			},
			//10
			new String[]{"a3","e5",
					"Kasparov prepares for a queenside expansion with a3, potentially supporting b4.\n\n"
							+"Topalov strikes in the center with e5, challenging Kasparov's central pawn structure."
			},
			//11
			new String[]{"O-O-O","Qe7",
					"Kasparov castles queenside, signaling a commitment to a complex and unbalanced game.\n\n"
							+"Topalov's queen move to e7 connects his rooks and supports his central pawns."
			},
			//12
			new String[]{"Kb1","a6",
					"Kasparov moves his king to b1, a subtle but important step for safety in queenside castling.\n\n"
							+"Topalov plays a6, preparing for further queenside expansion and creating breathing space for his king."
			},
			//13
			new String[]{"Nc1","O-O-O",
					"Kasparov repositions his knight to c1, eyeing potential squares on b3 or d3.\n\n"
							+"Topalov mirrors Kasparov's strategy by castling queenside, enhancing rook activity and king safety."
			},
			//14
			new String[]{"Nb3","exd4",
					"Kasparov's knight moves to b3, targeting the d4 square and strengthening queenside control.\n\n"
							+"Topalov captures on d4, opening the e-file and aiming to disrupt Kasparov's pawn structure."
			},
			//15
			new String[]{"Rxd4","c5",
					"Kasparov recaptures on d4 with his rook, maintaining a strong central presence.\n\n"
							+"Topalov immediately challenges the rook with c5, aiming to open lines on the queenside."
			},
			//16
			new String[]{"Rd1","Nb6",
					"Kasparov retreats his rook to d1, maintaining pressure along the d-file.\n\n"
							+"Topalov repositions his knight to b6, targeting Kasparov's c4 pawn and controlling critical squares."
			},
			//17
			new String[]{"g3","Kb8",
					"Kasparov plays g3, a preparatory move for f4 and strengthening his kingside structure.\n\n"+
							"Topalov's king move to b8 is a safety precaution, clearing the c-file and avoiding any potential tactics."
			},
			//18
			new String[]{"Na5","Ba8",
					"Kasparov's knight leaps to a5, putting pressure on b7 and threatening to infiltrate Topalov's queenside.\n\n"+
							"Topalov retreats his bishop to a8, maintaining influence on the long diagonal."
			},
			//19
			new String[]{"Bh3","d5",
					"Kasparov's bishop move to h3 targets weaknesses in Topalov's pawn structure, particularly the e6 square.\n\n"+
							"Topalov counters by advancing d5, challenging Kasparov's central dominance and seeking active play."
			},
			//20
			new String[]{"Qf4+","Ka7",
					"Kasparov delivers a check with Qf4, forcing Topalov's king to a7 and exposing it on the queenside.\n\n"+
							"Topalov's king move to a7 seeks to avoid immediate threats and find relative safety."
			},
			//21
			new String[]{"Rhe1","d4",
					"Kasparov aligns his rook with the e-file, adding pressure to Topalov's e5 pawn and preparing for a central breakthrough.\n\n"+
							"Topalov advances his pawn to d4, cramping Kasparov's position and looking to open more lines."
			},
			//22
			new String[]{"Nd5","Nbxd5",
					"Kasparov plays Nd5, a tactical strike aiming to dismantle Topalov's pawn structure and seize control of key squares.\n\n"+
							"Topalov captures the knight with Nbxd5, maintaining central integrity and removing a key aggressor."
			},
			//23
			new String[]{"exd5","Qd6",
					"Kasparov recaptures with his pawn on d5, keeping central tension and opening lines for his pieces.\n\n"+
							"Topalov's queen move to d6 centralizes his queen, supporting pawns and coordinating with rooks."
			},
			//24
			new String[]{"Rxd4","cxd4",
					"Kasparov captures Topalov's pawn on d4 with his rook, aiming to maintain material balance and open files.\n\n"+
							"Topalov responds by capturing the rook with cxd4, aiming to simplify the position and reduce pressure."
			},
			//25
			new String[]{"Re7+","Kb6",
					"Kasparov's rook move to e7 delivers a check, penetrating Topalov's position and threatening further incursion.\n\n"+
							"Topalov's king advances to b6, escaping the immediate threat but still remaining in a precarious position."
			},
			//26
			new String[]{"Qxd4+","Kxa5",
					"Kasparov's queen captures the pawn on d4 with a check, maintaining pressure and aiming to exploit Topalov's king's exposed position.\n\n"+
							"Topalov's king moves to a5, attempting to find safety but wandering further into danger."
			},
			//27
			new String[]{"b4+","Ka4",
					"Kasparov advances his pawn to b4 with a check, aggressively pursuing Topalov's king and opening lines for attack.\n\n"+
							"Topalov's king moves to a4, finding temporary safety but getting dangerously close to Kasparov's pieces."
			},
			//28
			new String[]{"Qc3","Qxd5",
					"Kasparov advances his queen to c3, aligning it with Topalov's king, creating a line of attack.\n\n"+
							"Topalov responds by capturing Kasparov's pawn on d5, taking control of the center."
			},
			//29
			new String[]{"Ra7","Bb7",
					"Kasparov aggressively moves his rook to a7, threatening Topalov's king's position.\n\n"+
							"Topalov counters by placing his bishop on b7, fortifying his defenses."
			},
			//30
			new String[]{"Rxb7","Qc4",
					"Kasparov captures Topalov's bishop on b7 with his rook, intensifying the attack.\n\n"+
							"Topalov shifts his queen to c4, looking to counter Kasparov's aggressive play."
			},
			//31
			new String[]{"Qxf6","Kxa3",
					"Kasparov's queen takes the knight on f6, seizing a tactical advantage.\n\n"+
							"Topalov's king moves to a3, trying to escape the growing threat."
			},
			//32
			new String[]{"Qxa6+","Kxb4",
					"Kasparov's queen moves to a6, putting Topalov's king in check.\n\n"+
							"Topalov moves his king to b4, seeking safety."
			},
			//33
			new String[]{"c3+","Kxc3",
					"Kasparov plays c3, delivering a check to Topalov's king.\n\n"+
							"Topalov captures the c3 pawn with his king, trying to alleviate the pressure."

			},
			//34
			new String[]{"Qa1+","Kd2",
					"Kasparov places Topalov's king in check again, moving his queen to a1.\n\n"+
							"Topalov's king escapes to d2."
			},
			//35
			new String[]{"Qb2+","kd1",
					"Kasparov continues his offensive, checking Topalov's king with his queen on b2.\n\n"+
							"Topalov's king moves to d1, narrowly evading capture."
			},
			//36
			new String[]{"Bf1","Rd2",
					"Kasparov develops his bishop to f1, strategically positioning it.\n\n"+
							"Topalov's rook moves to d2, mounting a counterattack."
			},
			//37
			new String[]{"Rd7","Rxd7",
					"Kasparov's rook dives into the fray on d7, challenging Topalov's defenses.\n\n"+
							"Topalov captures the invading rook with his own rook on d7."
			},
			//38
			new String[]{"Bxc4","bxc4",
					"Kasparov captures Topalov's pawn on c4 with his bishop, clearing the path.\n\n"+
							"Topalov responds by taking the bishop with his pawn."
			},
			//39
			new String[]{"Qxh8","Rd3",
					"Kasparov's queen captures the rook on h8, gaining a material advantage.\n\n"+
							"Topalov moves his rook to d3, looking for strategic opportunities."
			},
			//40
			new String[]{"Qa8","c3",
					"Kasparov repositions his queen to a8, eyeing the battlefield.\n\n"+
							"Topalov pushes his c3 pawn forward, expanding his influence."
			},
			//41
			new String[]{"Qa4+","Ke1",
					"Kasparov's queen checks from a4, keeping the pressure on Topalov.\n\n"+
							"Topalov's king moves to e1, seeking refuge."
			},
			//42
			new String[]{"f4","f5",
					"Kasparov advances his f4 pawn, opening up the game.\n\n"+
							"Topalov counters with a pawn move to f5, mirroring Kasparov's strategy."
			},
			//43
			new String[]{"Kc1","Rd2",
					"Kasparov's king moves to c1, safeguarding its position.\n\n"+
							"Topalov's rook advances to d2, pressing the attack."
			},

	};

	public static final String END_TUTORIAL_STRING="Topalov resigns the game shortly after this point, seeing no viable way to win after the last several moves of Kasparov's queen chasing his king around the board."+
			"This ends the tutorial! See if you can play the rest of the game and deliver a decisive checkmate!";

	/**
	 * Get the current move for the tutorial
	 * @param model The GameModel class
	 * @param team  The team to get the move of
	 * @return The move
	 */
	public static Integer getMove(GameModel model,boolean team){
		int index=0;
		if(team==BLACK)
			index=1;

		int turnIndex=model.getMoveCount()/2;

		if(turnIndex>=TUTORIAL_GAME.length){
			// We're out of tutorial moves
			return null;
		}else{
			// Get the PGN move based on the team index
			String s=TUTORIAL_GAME[turnIndex][index];

			// Get the move from PGN notation
			Integer m=AlgNotationHelper.moveFromAlgebraicNotation(
					s,
					team,model.getCurrentBoard());
			return m;
		}
	}

	/**
	 * Gets the current tutorial blurb
	 * @param moveCount The current move count
	 * @return The current blurb
	 */
	public static String blurbString(int moveCount){
		int turnIndex=moveCount/2;
		if(turnIndex>=TUTORIAL_GAME.length){
			// We're out of tutorial moves
			return END_TUTORIAL_STRING;
		}else{
			// Get the move blurb based on the team index
			String s=TUTORIAL_GAME[turnIndex][2];
			return s;
		}
	}

	/**
	 * Displays a popup with the current tutorial text.
	 * @param text The text to display
	 */
	public static void showTutorialBlurb(String text){
		if(text==null) return;//Early escape if null

		JFrame gameState=new JFrame("Tutorial");

		String htmlText="<html><p>"+text+"</p></html>";
		JLabel stateLabel=new JLabel(htmlText);
		JButton button=new JButton("Wow, Amazing move. Thank you!");
		button.addActionListener(e->gameState.dispose());
		button.setFont(buttonFont);
		stateLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		stateLabel.setVerticalAlignment(JLabel.CENTER);
		gameState.setLayout(new BorderLayout());
		gameState.add(stateLabel,BorderLayout.CENTER);
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(button);
		gameState.add(buttonPanel,BorderLayout.SOUTH);
		button.setPreferredSize(new Dimension(375,50));
		button.setFocusable(false);
		gameState.setSize(new Dimension(800,400));
		gameState.setLocationRelativeTo(null);
		gameState.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameState.setVisible(true);

		
		System.out.println(stateLabel.getText());
	}

	/**
	 * Displays a popup with the current tutorial text.
	 * @param moveCount The number of moves in the game so far
	 */
	public static void showTutorialBlurb(int moveCount){
		showTutorialBlurb(blurbString(moveCount));
	}

	/**
	 * Displays a popup with the intro tutorial text.
	 */
	public static void showTutorialIntroBlurb(){

		JFrame gameState=new JFrame("Tutorial");

		String htmlText="<html><p>"+INTRO_TUTORIAL_STRING+"</p></html>";
		JLabel stateLabel=new JLabel(htmlText);
		JButton button=new JButton("Cool!");
		button.setFont(buttonFont);
		button.addActionListener(e->{
			gameState.dispose();
			showTutorialBlurb(0);
		});
		stateLabel.setFont(new Font("Dialog",Font.PLAIN,18));
		stateLabel.setHorizontalAlignment(JLabel.CENTER);
		stateLabel.setVerticalAlignment(JLabel.CENTER);
		gameState.setLayout(new BorderLayout());
		gameState.add(stateLabel,BorderLayout.CENTER);
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(button);
		gameState.add(buttonPanel,BorderLayout.SOUTH);
		button.setPreferredSize(new Dimension(275,50));
		button.setFocusable(false);
		gameState.setSize(new Dimension(800,400));
		gameState.setLocationRelativeTo(null);
		gameState.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		gameState.setVisible(true);
	}
}