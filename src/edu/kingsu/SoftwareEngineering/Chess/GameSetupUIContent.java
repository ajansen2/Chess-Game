package edu.kingsu.SoftwareEngineering.Chess;

/**
 * No time to design a method to load the strings from a gut keeping them out of the main code
 * should still keep the file below 3000 lines long
 * @author Dalton Herrewynen
 * @version 1
 */
public abstract class GameSetupUIContent{
	/**
	 * Copied verbatim from GameSetupUI and wrapped in a nice method
	 * @param key the node name or key to look up
	 * @return Rules text
	 */
	public static String lookupRulesText(String key){
		return switch(key){
			case "Rules of Chess" ->
					"<html><center>Chess is a timeless and strategic board game that has captivated minds for centuries.<br>Whether you're a complete beginner or just looking to brush up on the basics, here are the fundamental rules of chess to get you started on your chess journey.<br>The chessboard consists of 64 squares arranged in an 8x8 grid.<br>Each player starts with 16 pieces: one king, one queen, two rooks, two knights, two bishops, and eight pawns.<br>Place the pieces on the board with the back row (rank) containing the rooks in the corners, followed by the knights, bishops, queen, and king.<br> Pawns fill the front row.<br><br><br><b>**Objective of the game:**<b><br>Checkmate your opponent's king to win the game.<br>Checkmate is a move that leaves your opponent with no more options: there is no move that won't result in you getting their king and therefore ending the game. Since a checkmate is so final, the word is used for other kinds of final victories.</center></html>";
			case "The King" ->
					"<html><center>The king is the most critical piece, and the game ends if it's in checkmate.<br>Move one square in any direction.</center></html>";
			case "The Pawn" ->
					"<html><center>The pawn moves forward but captures diagonally.<br>On its first move, it has the option to advance two squares.<br>Pawns promote to any piece upon reaching the eighth rank.</center></html>";
			case "The Rook" ->
					"<html><center>The rook moves horizontally or vertically.<br>It cannot jump over other pieces.<br>Rooks are powerful in open lines and endgames.</center></html>";
			case "The Knight" ->
					"<html><center>The knight has an L-shaped move, jumping over other pieces.<br>It's the only piece that can jump.<br>Knights are valuable for controlling the center of the board.</center></html>";
			case "The Bishop" ->
					"<html><center>The bishop moves diagonally.<br>Each player starts with two bishops.<br>Bishops complement each other, covering squares of both colors.</center></html>";
			case "The Queen" ->
					"<html><center>The queen combines the powers of the rook and bishop.<br>It moves horizontally, vertically, and diagonally.<br>The queen is a versatile and powerful piece.</center></html>";
			default -> "content not available";
		};
	}

	/**
	 * Copied verbatim from GameSetupUI and wrapped in a nice method
	 * @param key the node name or key to look up
	 * @return Help menu text
	 */
	public static String lookupHelpContent(String key){
		return switch(key){
			case "Italian Game" -> """
					**Italian Game: A Comprehensive Guide**\n\n*Introduction:*\n\nThe Italian Game is one of the oldest and most respected chess openings. Originating in the 16th century, it has been played by countless chess legends and continues to be a popular choice at all levels.\n\n*Historical Background:*\n\nThe Italian Game earned its name due to its association with Italian chess masters, including the legendary chess player Giuoco Piano. Its rich history and strategic depth make it a fascinating opening to study.\n\n*Basic Moves:*\n\nThe Italian Game typically starts with the following moves:\n\n\n1. e4 e5\n2. Nf3 Nc6\n3. Bc4\n\n*Key Concepts:*	\n- **Center Control:** The opening focuses on controlling the central squares with pawns and pieces, setting the stage for future actions.\n	\n- **Piece Development:** Rapid development of knights and bishops allows for quick mobilization and puts pressure on the opponent.\n\n- **Attack on f7:** The bishop on c4 targets the f7 square, often leading to tactical opportunities and potential weaknesses in Black's position.\n	\n*Strategic Ideas:*\n\n- **Double King's Pawn Opening:** The Italian Game is part of the Double King's Pawn Opening family, along with the Ruy Lopez and Scotch Game.\n- **Castling Kingside:** Players often castle kingside for king safety, securing the rook and preparing for central and queenside play.\n- **Pawn Structure:** The opening can lead to diverse pawn structures, offering flexibility in strategic plans.	\n*Common Variations:*\n- **Italian Gambit:** Some players opt for the Italian Gambit (4. d4 exd4 5. O-O), sacrificing a pawn for rapid development and attacking chances.	\n- **Classical System:** The classical approach involves c3 and d4, reinforcing central control and opening lines for the queen and bishops.\n*Example Game:*\n\n[Event "Italian Game Masterpiece"]\n[Site "?"]\n[Date "????.??.??"]\n[Round "?"]\n[White "ChessMaster"]\n[Black "Opponent"]\n[Result "*"]\n\n1. e4 e5\n2. Nf3 Nc6\n3. Bc4 Nf6\n4. d4 exd4\n5. O-O Nxe4\n6. Re1 d5\n7. Bxd5 Qxd5\n8. Nc3 Qa5\n9. Nxe4 Be6\n10. Neg5 O-O-O\n11. Nxe6 fxe6\n12. Rxe6 Bd6	\n\nThis captivating game showcases the strategic complexity and tactical possibilities inherent in the Italian Game.	\n*Conclusion:*\n\nThe Italian Game offers a blend of history, strategy, and dynamic play. Explore its variations, learn from master games, and incorporate it into your repertoire for an enriching chess experience.
					""";
			case "How to Play Chess" -> """
					Chess is a timeless and strategic board game that has captivated minds for centuries.\n\nWhether you're a complete beginner or just looking to brush up on the basics, here are the fundamental rules of chess to get you started on your chess journey.The game starts with 8 pieces in each player's hand: one king and eight pawns.\n\nThe chessboard consists of 64 squares arranged in an 8x8 grid.\n\nEach player starts with 16 pieces: one king, one queen, two rooks, two knights, two bishops, and eight pawns.\n\nPlace the pieces on the board with the back row (rank) containing the Rooks in the corners, followed by the Knights, Bishops, Queen, and king.\n\nPawns fill the front row.\n\n\n**Objective of the game:**\n\nCheckmate your opponent's king to win the game.\n\nCheckmate is a move that leaves your opponent with no more options: there is no move that won't result in you getting their king and therefore ending the game.\n\nSince a checkmate is so final, the word is used for other kinds of final victories.
					""";
			case "Ruy Lopez" -> """
					**Ruy López Opening: A Comprehensive Guide**\n\n*Introduction:*\n\nThe Ruy López, also known as the Spanish Opening, is one of the most classical and enduring chess openings. Dating back to the 16th century, it is named after the Spanish priest Ruy López de Segura, who studied it extensively. The Ruy López is popular among all levels of chess players, from beginners to grandmasters.\n\n*Historical Background:*\n\nThe Ruy López has evolved over centuries, with its strategic and tactical ideas being refined by world champions and legendary players. It is a cornerstone of chess opening theory.\n\n*Basic Moves:*\n\nThe Ruy López opening is characterized by:\n\n1. e4 e5\n2. Nf3 Nc6\n3. Bb5\n\n*Key Concepts:*\n\n- **Pressure on e5:** The knight on f3 and the bishop on b5 work together to target Black's central e5 pawn.\n- **Piece Activity:** The aim is to develop pieces to active squares, setting up for a strong middle game.\n- **Pawn Structure Flexibility:** This opening allows for various pawn structures, adaptable to different types of games.\n\n*Strategic Ideas:*\n\n- **Control of the Center:** Central control is a key objective in many e4 openings.\n- **Maneuvering for Advantage:** The opening often leads to complex maneuvering, giving both sides opportunities for initiative.\n- **Kingside Safety:** Castling early for king safety and activating the rook is common.\n\n*Common Variations:*\n\n- **Morphy Defense:** (3...a6) This leads to sub-variations like the Closed and Open Ruy López.\n- **Berlin Defense:** (3...Nf6) Known for its solidity and strategic depth.\n- **Steinitz Defense:** (3...d6) An earlier approach aiming for a solid, though somewhat passive, position.\n\n*Example Game:*\n\n[Event \"Ruy López Masterclass\"]\n[Site \"?\"]\n[Date \"????.??.??\"]\n[Round \"?\"]\n[White \"Grandmaster\"]\n[Black \"Challenger\"]\n[Result \"*\"]\n\n1. e4 e5\n2. Nf3 Nc6\n3. Bb5 a6\n4. Ba4 Nf6\n5. O-O Be7\n6. Re1 b5\n7. Bb3 d6\n8. c3 O-O\n9. h3 Nb8\n10. d4 Nbd7\n11. c4\n\nThis game demonstrates the depth and complexity of the Ruy López, highlighting its strategic and tactical richness.\n\n*Conclusion:*\n\nThe Ruy López offers a deep exploration of strategic and tactical ideas, making it a valuable part of a chess player's repertoire, regardless of their level of expertise.\n
					""";
			case "Queen's Gambit" -> """
					**Queen's Gambit: A Comprehensive Guide**\n\n*Introduction:*\n\nThe Queen's Gambit is one of the oldest and most renowned chess openings. Popularized in the 19th century, it has become a staple in the repertoire of many grandmasters and chess enthusiasts alike.\n\n*Historical Background:*\n\nThe Queen's Gambit has a rich history in chess, evolving through centuries of play. It has been a favorite among many chess legends for its solid yet dynamic nature.\n\n*Basic Moves:*\n\nThe Queen's Gambit starts with the moves:\n\n1. d4 d5\n2. c4\n\n*Key Concepts:*\n\n- **Control of the Center:** The opening aims at controlling the center while setting up for multiple strategic plays.\n- **Pawn Structure:** It offers a robust pawn structure, leading to a strong middle game.\n- **Flexibility:** The Queen's Gambit allows for various responses and transpositions, making it a versatile opening.\n\n*Strategic Ideas:*\n\n- **Piece Development:** Emphasizes the development of bishops and knights to control the board.\n- **Pawn Play:** Involves intricate pawn play and structure decisions that can dictate the course of the game.\n- **Control and Counterplay:** Provides options for both control and aggressive counterplay.\n\n*Common Variations:*\n\n- **Queen's Gambit Accepted:** Where Black accepts the gambit and captures the pawn on c4.\n- **Queen's Gambit Declined:** Where Black opts to maintain a strong pawn structure and not accept the gambit.\n- **Slav Defense:** A solid response to the Queen's Gambit, involving d5 and c6.\n\n*Example Game:*\n\n[Event \"Queen's Gambit Showcase\"]\n[Site \"?\"]\n[Date \"????.??.??\"]\n[Round \"?\"]\n[White \"Master\"]\n[Black \"Challenger\"]\n[Result \"*\"]\n\n1. d4 d5\n2. c4 e6\n3. Nc3 Nf6\n4. Bg5 Be7\n5. e3 O-O\n6. Nf3 Nbd7\n7. Rc1 c6\n8. Bd3 dxc4\n9. Bxc4 Nd5\n10. O-O\n\nThis game demonstrates the strategic depth and versatility of the Queen's Gambit.\n\n*Conclusion:*\n\nThe Queen's Gambit is a classic opening that offers a mix of solid strategy and dynamic play, making it an essential part of any chess player's learning curve.\n
					""";
			case "King's Gambit" -> """
					**King's Gambit: A Comprehensive Guide**\n\n*Introduction:*\n\nThe King's Gambit is one of the most aggressive and oldest openings in chess, tracing back to the 15th century. It is famous for leading to dynamic and tactical positions.\n\n*Historical Background:*\n\nThe King's Gambit has been played by many legendary players throughout history. It exemplifies the romantic era of chess, where bold attacks and sacrifices were the norm.\n\n*Basic Moves:*\n\nThe King's Gambit begins with the moves:\n\n1. e4 e5\n2. f4\n\n*Key Concepts:*\n\n- **Pawn Sacrifice for Activity:** The gambit sacrifices a pawn for quick development and attacking chances.\n- **King Safety:** Despite its name, ensuring the safety of the king is crucial after the initial pawn push.\n- **Center Control:** By pushing the f-pawn, White aims to dominate the center and open lines for the pieces.\n\n*Strategic Ideas:*\n\n- **Rapid Development:** The opening facilitates quick mobilization of pieces, especially the bishops and knights.\n- **Attacking Play:** It is known for leading to open, aggressive positions with lots of tactical possibilities.\n- **Flexibility in Strategy:** The King's Gambit can transpose into various structures, offering a wide range of strategic plans.\n\n*Common Variations:*\n\n- **King's Gambit Accepted:** Black accepts the pawn sacrifice (2...exf4).\n- **King's Gambit Declined:** Black opts for a more solid setup, not capturing the f4 pawn.\n- **Falkbeer Counter Gambit:** A counter-attacking option for Black (2...d5).\n\n*Example Game:*\n\n[Event \"King's Gambit Brilliance\"]\n[Site \"?\"]\n[Date \"????.??.??\"]\n[Round \"?\"]\n[White \"Tactician\"]\n[Black \"Defender\"]\n[Result \"*\"]\n\n1. e4 e5\n2. f4 exf4\n3. Nf3 g5\n4. Bc4 g4\n5. O-O gxf3\n6. Qxf3 Qf6\n7. e5 Qxe5\n8. Bxf7+ Kd8\n9. d4 Qxd4+\n10. Kh1 Ne7\n\nThis game highlights the sharp and aggressive nature of the King's Gambit.\n\n*Conclusion:*\n\nThe King's Gambit is a daring and ambitious opening, perfect for players who enjoy tactical battles and dynamic play. It offers a wealth of historical and theoretical interest for chess enthusiasts.\n
					""";
			case "French Defence" -> """
					**French Defense: A Comprehensive Guide**\n\n*Introduction:*\n\nThe French Defense is a renowned and strategically complex chess opening. It is known for its solid structure and counterattacking potential, favored by players who prefer a more positional approach.\n\n*Historical Background:*\n\nThe French Defense has been a popular choice in chess for centuries, offering rich strategic play. It gained significant attention in the 19th century and remains a staple in modern chess theory.\n\n*Basic Moves:*\n\nThe French Defense typically starts with the moves:\n\n1. e4 e6\n2. d4 d5\n\n*Key Concepts:*\n\n- **Pawn Structure:** The solid pawn chain in the center is a hallmark of the French Defense.\n- **Space and Counterplay:** Often, Black concedes space in exchange for solid structure and counterattacking opportunities.\n- **Piece Development:** The opening poses unique challenges and opportunities for piece development, particularly for Black's light-squared bishop.\n\n*Strategic Ideas:*\n\n- **Control of the Center:** Despite the solid front, the battle for central control is crucial.\n- **Flank Attacks:** Both sides may launch attacks on opposite wings, leading to dynamic play.\n- **Endgame Strength:** The French Defense often leads to strong endgame positions for Black.\n\n*Common Variations:*\n\n- **Advance Variation:** White advances the pawn with 3. e5.\n- **Exchange Variation:** White exchanges on d5, leading to open lines.\n- **Tarrasch Variation:** Involves Nd2 by White, challenging Black's pawn structure.\n\n*Example Game:*\n\n[Event \"French Defense Mastery\"]\n[Site \"?\"]\n[Date \"????.??.??\"]\n[Round \"?\"]\n[White \"Strategist\"]\n[Black \"Tactician\"]\n[Result \"*\"]\n\n1. e4 e6\n2. d4 d5\n3. Nc3 Nf6\n4. e5 Nfd7\n5. f4 c5\n6. Nf3 Nc6\n7. Be3 a6\n8. Qd2 b5\n9. Bd3 Qb6\n10. O-O Be7\n\nThis game showcases the strategic depth and counterattacking nature of the French Defense.\n\n*Conclusion:*\n\nThe French Defense offers a blend of solidity and complexity, appealing to players who enjoy deep strategic battles and gradual buildup. It's a valuable addition to any player's opening repertoire.\n
					""";
			case "English Opening" -> """
					**English Opening: A Comprehensive Guide**\n\n*Introduction:*\n\nThe English Opening is a flexible and strategic chess opening, starting with a move to control the center from a flank position. It leads to a variety of positional and dynamic structures.\n\n*Historical Background:*\n\nThe English Opening has been a part of chess for many centuries, gaining popularity in the 20th century. It offers a more subtle approach to the opening phase, contrasting with more direct central openings.\n\n*Basic Moves:*\n\nThe English Opening typically begins with:\n\n1. c4\n\n*Key Concepts:*\n\n- **Control from the Flank:** The move c4 aims to control the center indirectly.\n- **Flexibility:** The opening allows White to adapt to a wide range of setups based on Black's response.\n- **Pawn Structure:** It often leads to asymmetrical pawn structures, offering rich strategic play.\n\n*Strategic Ideas:*\n\n- **Piece Development:** The opening emphasizes developing pieces towards the center and maintaining flexibility.\n- **Central Control:** Although the approach is indirect, fighting for central control remains essential.\n- **Transpositional Possibilities:** The English can transpose into many other openings, making it a versatile choice.\n\n*Common Variations:*\n\n- **Symmetrical Variation:** Black mirrors White's setup with 1...c5.\n- **Reversed Sicilian:** Emanating from 1...e5, it takes on the nature of a Sicilian Defense with colors reversed.\n- **King's Indian Setup:** Black can choose a King's Indian Defense setup against the English, leading to rich strategic battles.\n\n*Example Game:*\n\n[Event \"English Opening Showcase\"]\n[Site \"?\"]\n[Date \"????.??.??\"]\n[Round \"?\"]\n[White \"Master\"]\n[Black \"Challenger\"]\n[Result \"*\"]\n\n1. c4 e5\n2. Nc3 Nf6\n3. Nf3 Nc6\n4. g3 d5\n5. cxd5 Nxd5\n6. Bg2 Nb6\n7. O-O Be7\n8. a3 O-O\n9. b4 Bf6\n10. Rb1 Bf5\n\nThis game exemplifies the strategic and adaptable nature of the English Opening.\n\n*Conclusion:*\n\nThe English Opening is a subtle yet powerful tool in a chess player's arsenal, offering the flexibility to adapt to various styles of play. It encourages creativity and strategic planning, making it a popular choice at all levels of play.\n
					""";
			// need more cases zz
			case "Rules of The Pieces" -> """
						Understanding the legal moves of each chess piece is crucial for mastering the game. Here's a guide to the rules of the pieces:\n\n**Pawn:**\nPawns move forward but capture diagonally. On the first move, a pawn has the option to advance two squares.\nPawns promote to any piece upon reaching the eighth rank.\n\n**Rook:**\nThe rook moves horizontally or vertically. It cannot jump over other pieces and is powerful in open lines and endgames.\n\n**Knight:**\nThe knight has an L-shaped move, jumping over other pieces. It's the only piece that can jump and is valuable for controlling the center of the board.\n\n**Bishop:**\nThe bishop moves diagonally. Each player starts with two bishops, and bishops complement each other by covering squares of both colors.\n\n**Queen:**\nThe queen combines the powers of the rook and bishop. It moves horizontally, vertically, and diagonally, making it a versatile and powerful piece.\n\n**King:**\nThe king is the most critical piece, and the game ends if it's in checkmate. The king can move one square in any direction.\n\nThese are the basic rules for the movement of each chess piece. Familiarize yourself with these rules to enhance your strategic play.
					""";
			case "Special Moves" -> """
					Chess includes some special moves and rules that add depth to the game. Here's a guide to these special moves:\n\n**En Passant:**\nIf a pawn moves two squares forward from its starting position and lands beside an opponent's pawn, the opponent has the option to capture the first pawn "en passant" on the next move.\n\n**Pawn Promotion:**\nWhen a pawn reaches the eighth rank, it can be promoted to any other piece (except a king). The player usually chooses a queen, as it is the most powerful piece.\n\n**Castling:**\nCastling is a special king move involving the rook. It allows the king to move two squares toward a rook, and the rook moves to the square next to the king. However, specific conditions must be met:\n- Neither piece involved has moved before.\n- The squares between the king and rook are unoccupied.\n- The king is not in check, nor may the squares it crosses or lands on be under attack.\n\n**Check and Checkmate:**\nA king is in "check" when it is under attack. If a player's king is in check and there is no legal move to escape the threat, it is checkmate, and the game ends.\n\nUnderstanding these special moves adds strategic depth to your chess play. Practice them to enhance your overall game strategy.
					""";
			case "Central Control" -> """
					Understanding Central Control:\n   Central control is a pivotal concept in chess that significantly influences game dynamics. It revolves around the strategic contest for key central squares – d4, d5, e4, and e5.\n\nImportance of Central Squares:\n   Central squares provide a commanding position for launching attacks and controlling the board.\n\nStrategic Advantages:\n   Achieving central control contributes to enhanced piece mobility, flexibility, and potential pawn breaks.

						""";
			case "Tactical Themes" -> """
					Common Tactical Themes:\n   Tactical awareness is crucial for success in the middle game. Here are some common tactical themes to keep in mind:\n\nForks:\n   Identifying positions where a single piece attacks two or more opponent pieces simultaneously.\n\nPins:\n   Restricting the movement of an opponent's piece to protect a more valuable piece behind it.\n\nDiscovered Attacks:\n   Unleashing the power of a piece by moving another piece that was blocking its path.
					""";
			case "Forking Opportunities" -> """
					Leveraging Forks:\n   Forks are potent tactical maneuvers that can lead to material advantage. Understanding how to create forking opportunities is essential:\n\nIdentifying Vulnerable Squares:\n   Recognizing squares where a fork is possible based on the positioning of opponent pieces.\n\nStrategic Piece Placement:\n   Planning moves to strategically position pieces and create potential fork scenarios."
					""";
			case "Opening Transition to Middle Game" -> """
					Navigating the Opening Transition:\n   The transition from the opening to the middle game is a critical phase. Consider the following:\n\nRecognizing Transition Points:\n   Identifying key moments where the opening evolves into the middle game.\n\nAdapting Strategic Plans:\n   Adjusting plans based on the strategic implications of the opening moves.\n\nAnticipating Opponent's Plans:\n   Anticipating and responding to the opponent's plans as the game transitions to a more dynamic phase.
					""";
			case "Pawn Structures" -> """
					Understanding pawn structures is essential for strategic planning in chess. Here's a brief overview:\n\n
					**Pawn Chains:**\n
					A pawn chain is a connected series of pawns of the same color. Chains can be strong defensive structures, but weaknesses can arise if the chain is broken.\n\n
					**Pawn Islands:**\n
					Pawn islands are groups of pawns connected horizontally or vertically. Minimizing pawn islands generally leads to a more compact and solid pawn structure.\n\n
					**Isolated Pawns:**\n
					An isolated pawn is a pawn with no neighboring pawns of the same color. While it can control key squares, it may become a target for attacks.\n\n
					**Doubled Pawns:**\n
					Doubled pawns occur when two pawns of the same color are stacked on the same file. They can be a weakness if not properly supported.\n\n
					**Pawn Majority:**\n
					A pawn majority occurs when a player has more pawns on one side of the board. It can be used for a pawn majority attack or to create passed pawns.
					""";
			case "Piece Activity" -> """
					Maximizing piece activity is crucial for a successful chess strategy. Here are some tips:\n\n
					**Centralization:**\n
					Place your pieces in the center of the board to control key squares and exert influence over the entire board.\n\n
					**Coordination:**\n
					Ensure your pieces work together harmoniously. Coordinated pieces can support each other's strengths and create threats.\n\n
					**Avoiding Passive Pieces:**\n
					Keep your pieces from becoming passive or trapped. Active pieces contribute more to your position and can respond effectively to the opponent's threats.\n\n
					**Outposts:**\n
					An outpost is a square on the opponent's side of the board that is difficult for them to challenge. Place your pieces on outposts to control key squares.\n\n
					**King Safety:**\n
					While not directly related to piece activity, ensuring the safety of your king allows your other pieces to focus on strategic and tactical objectives.
					""";
			case "King and Pawn vs. King" -> """
					Endgames often involve scenarios where a king and pawns face an opposing king. Here's what you need to know:\n\n**Basic Endgame Principles:**\n
					- Centralize your king: Bring your king to the center to control key squares.\n
					- Advance your pawns: Create passed pawns to put pressure on the opponent.\n
					- Opposition: Use the opposition to control access to key squares.\n\n
					**King and Pawn vs. King:**\n
					In this scenario, the side with the pawn aims to advance it while restricting the opponent's king. Use your king to support pawn advancement.\n\n
					**King Activity:**\n
					Activating your king is crucial. It should be in a position to support your pawns and restrict the opponent's king.\n\n
					**Stalemate Awareness:**\n
					Be cautious about stalemate traps. Ensure that your opponent has legal moves to make, or it could result in a draw.
					""";
			case "Rook Endings" -> """
					Rook endings are common in chess endgames. Here are some principles to guide you:\n\n
					**Active Rooks:**\n
					Keep your rooks active and centralized. Active rooks control key files and ranks and can create threats against the opponent's king.\n\n
					**King Activity:**\n
					Centralize your king. In rook endings, the king often plays a vital role in supporting pawn advances and controlling the board.\n\n
					**Connected Rooks:**\n
					Connecting your rooks can be powerful. They support each other and can control large sections of the board.\n\n
					**Pawn Majority:**\n
					If you have a pawn majority, use it to create passed pawns. Passed pawns can become a powerful asset in rook endings.\n\n
					**Pawn Structure Awareness:**\n
					Be aware of pawn structures and how they influence the dynamics of the rook ending.
					""";
			case "Pawn Endgames" -> """
					In pawn endgames, understanding pawn structures and the potential for pawn promotion is crucial. Here are key points to consider:\n\n
					**Passed Pawns:**\n
					- Passed pawns are pawns that have no opposing pawns in their path to promotion. Advance your passed pawns to create threats and gain a positional advantage.\n
					- Be mindful of blockading opponent's passed pawns to prevent their advancement.\n
					- Connected passed pawns, when working together, can be a powerful force.\n\n
					**Pawn Chains:**\n
					- Create pawn chains to control key squares and limit your opponent's pawn mobility.\n
					- Break your opponent's pawn chains to create weaknesses and open lines.\n
					- Centralize your king to support your pawn structure.\n\n
					**King Activity:**\n
					- Activate your king and bring it to the center to play a more active role.\n
					- Use your king to support the advancement of your pawns.\n
					- Be cautious about pawn endings with an imbalanced number of pawns.\n
					""";
			case "Knight Endings" -> """
					Knight endings present unique challenges and opportunities. Here's what you need to know:\n\n
					**Knight Maneuvering:**\n
					- Knights are excellent pieces in maneuvering and controlling key squares. Place your knight on outposts to dominate the board.\n
					- In endgames, a centralized knight can be a powerful asset.\n
					- Be cautious about knights' vulnerability to forks and tactics.\n\n
					**Knight vs. Pawns:**\n
					- Knights can be effective in stopping passed pawns due to their jumping ability.\n
					- In some scenarios, a knight can control key squares and contribute to a successful pawn promotion defense.\n
					- Coordinate your knight with your king for maximum effectiveness.\n\n
					**Pawn Structure Awareness:**\n
					- Evaluate the pawn structure to determine the optimal outposts for your knight.\n
					- Consider the position of your knight in relation to potential pawn breaks.\n
					""";
			case "Bishop Endings" -> """
					Bishop endings involve strategic maneuvering and pawn structure awareness. Here's a guide to mastering bishop endgames:\n\n
					**Bishop Pair:**\n
					- The bishop pair (having both bishops) can be a significant advantage. Use them to control long diagonals and coordinate their movements.\n
					- Centralize your bishops to exert influence over the board.\n
					- Be cautious about pawn structures that might restrict your bishops' mobility.\n\n
					**Bishops vs. Knights:**\n
					- In bishop endings, bishops often outshine knights due to their long-range capabilities.\n
					- Consider the position of pawns when evaluating the strength of bishops.\n
					- Use your bishops to control critical squares and limit your opponent's options.\n\n
					**Pawn Structure Influence:**\n
					- Assess the pawn structure to determine the best diagonals for your bishops.\n
					- Break opponent pawn structures to create weaknesses and open lines for your bishops.\n
					""";
			case "Queen Endgames" -> """
					Queen endgames require careful calculation and precise maneuvering. Here's what you need to know:\n\n
					**Active Queen Play:**\n
					- Keep your queen active and use it to control key squares.\n
					- Coordinate your queen with your other pieces to create threats.\n
					- Be cautious about exposing your queen to forks and tactical vulnerabilities.\n\n
					**Queen vs. Minor Pieces:**\n
					- A queen can be superior to minor pieces in open positions. Use its range to control the board.\n
					- Be cautious about minor pieces coordinating to control key squares.\n
					- In some scenarios, a well-coordinated queen and pawn structure can outperform minor pieces.\n\n
					**Pawn Structure Awareness:**\n
					- Evaluate the pawn structure and open lines to determine the optimal squares for your queen.\n
					- Use your queen to support pawn advancements or to restrict opponent pawn breaks.\n
					""";
			case "King and Pawn vs. King and Pawn" -> """
					King and pawn vs. king and pawn endings involve intricate pawn races and tactical considerations. Here's a comprehensive guide:\n\n
					**Pawn Advancement:**\n
					- The side with the pawn should aim to advance it while ensuring the opponent's king cannot stop its promotion.\n
					- Use your king to support the pawn and create a passed pawn.\n
					- Be cautious about stalemate possibilities and zugzwang situations.\n\n
					**King Activity:**\n
					- Activate your king and use it to restrict the opponent's king's movement.\n
					- Calculate pawn races and assess whether your king or pawn can reach a critical square first.\n
					- Consider opposition tactics to control access to key squares.\n
					""";
			case "Opposite-Colored Bishop Endings" -> """
					Opposite-colored bishop endings add a layer of complexity to endgames. Here's what you need to know to navigate these positions successfully:\n\n
					**Bishop Color Dynamics:**\n
					- Opposite-colored bishops often favor the attacker. Use your bishop to control squares of the opposite color.\n
					- Be cautious about fortresses and drawing possibilities.\n
					- Coordinate your pieces to exploit the weaknesses created by opposite-colored bishops.\n\n
					**Pawn Structure Impact:**\n
					- Evaluate the pawn structure to determine whether opposite-colored bishops provide winning chances.\n
					- Use your bishop to target weak pawns and restrict the opponent's bishop.\n
					""";
			case "Two Bishops Endings" -> """
					Two bishops endings can be highly favorable due to their long-range capabilities. Here's a guide to navigating these endgames:\n\n
					**Bishop Pair Advantage:**\n
					- Having both bishops can provide a significant advantage. Coordinate their movements to control the board.\n
					- Aim to open up the position to leverage the power of the bishops.\n
					- Be cautious about pawn structures that might limit the bishops' effectiveness.\n\n
					**Pawn Structure Influence:**\n
					- Assess the pawn structure to determine the optimal diagonals for your bishops.\n
					- Break opponent pawn structures to create open lines for your bishops.\n
					""";
			case "Rook and Pawn vs. Rook" -> """
					Rook and pawn vs. rook endings involve tactical intricacies and precise maneuvering. Here's a comprehensive guide:\n\n
					**Pawn Advancement Strategies:**\n
					- Create a passed pawn and use it to create threats or force weaknesses in the opponent's position.\n
					- Be cautious about the potential for stalemate or insufficient material situations.\n
					- Use your rook to control open lines and restrict the opponent's rook.\n\n
					**King Activity:**\n
					- Activate your king and bring it to the center to support pawn advancements
					""";
			case "Minor Piece Endings" -> """
					Minor piece endings involve positions with knights and bishops. Here's a comprehensive guide to navigating these endgames:\n\n
					**Knight Maneuverability:**\n
					- Activate your knights and use their ability to control squares.\n
					- Coordinate your knights to create threats and control key areas of the board.\n
					- Consider pawn structures that favor knight maneuverability.\n\n
					**Bishop Dynamics:**\n
					- Bishops can be powerful in open positions. Use them to control long diagonals and create threats.\n
					- Coordinate your bishops to exploit weaknesses in the opponent's structure.\n
					- Be cautious about pawn structures that might limit your bishops' mobility.\n
					""";
			case "Conversion Technique in Endgames" -> """
					The conversion technique in endgames refers to the process of converting an advantage into a winning position. Here's a guide to mastering this essential skill:\n\n
					**Piece Coordination:**\n
					- Coordinate your pieces to create threats and restrict your opponent's options.\n
					- Activate your king and use it to support the advance of passed pawns.\n
					- Be cautious about counterplay and ensure your advantage is secure before converting.\n\n
					**Creating Winning Chances:**\n
					- Identify weaknesses in your opponent's position and exploit them strategically.\n
					- Calculate variations to ensure that your plan leads to a winning position.\n
					- Consider time management and avoid unnecessary risks when converting an advantage.\n
					""";
			case "Creating Passed Pawns in Endgames" -> """
					Creating passed pawns in endgames is a key strategic concept. Here's a guide to mastering this skill:\n\n
					**Pawn Advancement:**\n
					- Advance your pawns to create passed pawns that can become powerful assets in the endgame.\n
					- Use your pieces, especially the king, to support the advance of passed pawns.\n
					- Be cautious about opponent attempts to block or capture your passed pawns.\n
					""";
			case "Endgame Tactics and Themes" -> """
					Endgame tactics and themes play a crucial role in deciding the outcome of a game. Here's a comprehensive guide to key tactics and themes in endgames:\n\n
					**Forking Opportunities:**\n
					- Look for opportunities to fork your opponent's pieces or pawns using your own pieces.\n
					- Knights and queens are particularly effective in creating forks due to their ability to jump over other pieces.\n
					- Use forks to gain material advantages or create threats that lead to a favorable endgame.\n\n
					**Skewers and Pins:**\n
					- Utilize skewers and pins to create threats and win material.\n
					- A skewer involves attacking two pieces in a line, with the opponent forced to move the more valuable piece, allowing you to capture the less valuable one.\n
					- Pins immobilize an opponent's piece, preventing it from moving and potentially winning material.\n\n
					**Zwischenzug (Intermezzo):**\n
					- Zwischenzug refers to an in-between move, where a player makes a surprising intermediate move between expected moves.\n
					- Use zwischenzugs to disrupt your opponent's plans and create unexpected threats.\n
					- These tactical ideas can be particularly powerful in the endgame, where each move carries significant weight.\n
					""";
			case "Endgame Strategy Principles" -> """
					Endgame strategy principles guide decision-making in the final phase of the game. Here's a comprehensive guide to key endgame strategy principles:\n\n
					**King Activity:**\n
					- Activate your king and bring it to the center of the board. A centralized king can play a crucial role in controlling key squares and supporting pawn advancements.\n
					- Use your king to create threats and restrict the opponent's king's movement.\n
					- Centralize the king before advancing pawns to avoid potential counterplay.\n\n
					**Piece Coordination:**\n
					- Coordinate your pieces effectively to control the board and create threats.\n
					- Ensure that your pieces work harmoniously, supporting each other and restricting your opponent's options.\n
					- Avoid leaving pieces undefended or vulnerable to tactical threats.\n\n
					**Pawn Structure Awareness:**\n
					- Evaluate the pawn structure to determine the optimal plans and potential weaknesses.\n
					- Create passed pawns when possible and exploit weaknesses in your opponent's pawn structure.\n
					- Be cautious about creating pawn weaknesses in your own structure that could be exploited.\n
					""";
			default -> "No content available.";
		};
	}
}
