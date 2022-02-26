import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connect4 {

	static char board[][];
	static int[] optimalColumns = { 4, 3, 5, 2, 6, 1, 7 };
	// static int[][] optimalTable = { { 3, 4, 5, 7, 5, 4, 3 }, { 4, 6, 8, 10, 8, 6,
	// 4 }, { 5, 8, 11, 13, 11, 8, 5 },
	// { 5, 8, 11, 13, 11, 8, 5 }, { 4, 6, 8, 10, 8, 6, 4 }, { 3, 4, 5, 7, 5, 4, 3 }
	// };
	// static boolean maxDepthReached = false;

	private static int winCondition(char[][] state, char color) {
		// Horizontal check
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (state[i][j] == color && state[i][j + 1] == color && state[i][j + 2] == color
						&& state[i][j + 3] == color)
					return 0;
			}
		}

		// Vertical check
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 7; j++) {
				if (state[i][j] == color && state[i + 1][j] == color && state[i + 2][j] == color
						&& state[i + 3][j] == color)
					return 0;
			}
		}

		// Bottom left to top right check
		for (int i = 3; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (state[i][j] == color && state[i - 1][j + 1] == color && state[i - 2][j + 2] == color
						&& state[i - 3][j + 3] == color) {
					return 0;
				}
			}
		}

		// Bottom right to top left check
		for (int i = 3; i < 6; i++) {
			for (int j = 3; j < 7; j++) {
				if (state[i][j] == color && state[i - 1][j - 1] == color && state[i - 2][j - 2] == color
						&& state[i - 3][j - 3] == color) {
					return 0;
				}
			}
		}

		if (getOpenSpaces(state).size() == 0) {
			return 44;
		}

		return 1;
	}

	private static boolean placeMove(char[][] board, int column, char color) {
		for (int i = 0; i < 6; i++) {
			if (board[5 - i][column - 1] == ' ') {
				board[5 - i][column - 1] = color;
				return true;
			}
		}
		return false;
	}

	private static boolean removeMove(char[][] board, int column, char color) {
		for (int i = 0; i < 6; i++) {
			if (board[i][column - 1] == color) {
				board[i][column - 1] = ' ';
				return true;
			}

		}
		return false;
	}

	// Find available spaces to improve runtime
	private static List<Integer> getOpenSpaces(char[][] board) {
		List<Integer> spaces = new ArrayList<Integer>();
		for (int i = 0; i < 7; i++) {
			if (board[0][i] == ' ')
				spaces.add(i + 1);
		}
		return spaces;
	}

	private static void printBoard(char[][] board) {
		System.out.println("  1   2   3   4   5   6   7");
		System.out.println("-----------------------------");
		for (int i = 0; i < 6; i++) {
			System.out.println("| " + board[i][0] + " | " + board[i][1] + " | " + board[i][2] + " | " + board[i][3]
					+ " | " + board[i][4] + " | " + board[i][5] + " | " + board[i][6] + " |");
			System.out.println("-----------------------------");

		}

	}

	private static int scoreYWin(int depth) {
		return depth - 1000;
	}

	private static int scoreRWin(int depth) {
		return 1000 - depth;
	}

	private static int maxAlphaBeta(char[][] state, int alpha, int beta, int depth, int maxDepth, boolean isBaseCase) {
		int max = -1001;
		int pos = 0;
		if (winCondition(state, 'R') == 0)
			return scoreRWin(depth);
		else if (winCondition(state, 'Y') == 0)
			return scoreYWin(depth);
		else if (winCondition(state, 'Y') == 44)
			return 0;

		depth++;
		if (depth == maxDepth) {
			// maxDepthReached = true;
			return 0;
		}
		for (int i = 0; i < 7; i++) {
			if (placeMove(state, optimalColumns[i], 'R') == true) {
				int m = minAlphaBeta(state, alpha, beta, depth, maxDepth, false);
				if (m > max) {
					max = m;
					pos = optimalColumns[i];
				}
				removeMove(state, optimalColumns[i], 'R');
				if (max >= beta)
					return max;
				if (max > alpha)
					alpha = max;
			} else {
				continue;
			}
		}
		return max;
	}

	private static int minAlphaBeta(char[][] state, int alpha, int beta, int depth, int maxDepth, boolean isBaseCase) {
		int min = 1001;
		int pos = 0;
		if (winCondition(state, 'R') == 0)
			return scoreRWin(depth);
		else if (winCondition(state, 'Y') == 0)
			return scoreYWin(depth);
		else if (winCondition(state, 'Y') == 44)
			return 0;

		depth++;
		if (depth == maxDepth) {
			// maxDepthReached = true;
			return 0;
		}
		for (int i = 0; i < 7; i++) {
			if (placeMove(state, optimalColumns[i], 'Y') == true) {
				int m = maxAlphaBeta(state, alpha, beta, depth, maxDepth, false);
				if (m < min) {
					min = m;
					pos = optimalColumns[i];
				}
				removeMove(state, optimalColumns[i], 'Y');
				if (min <= alpha)
					return min;
				if (min < beta)
					beta = min;
			} else {
				continue;
			}
		}
		if (isBaseCase)
			return pos;
		return min;

	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(new InputStreamReader(System.in));

		String continuePlaying = "Y";
		while (continuePlaying.equalsIgnoreCase("Y")) {
			char[][] board = { { ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
					{ ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, { ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
					{ ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, { ' ', ' ', ' ', ' ', ' ', ' ', ' ' } };
			int alternator = 0;
			int turn = 1;
			System.out.println("Who goes first, R (You) or Y (AI)");
			char orderOfPlay = sc.next().charAt(0);
			char player = orderOfPlay;

			System.out.println("Turn: " + turn);

			if (orderOfPlay == 'R') {
				printBoard(board);
			}

			// If AI goes first
			if (orderOfPlay == 'Y') {
				alternator++;
			}

			while (winCondition(board, player) == 1) {
				// Users move
				if (alternator % 2 == 0) {
					player = 'R';
					System.out.println("Enter column (1-7): ");
					int position = sc.nextInt();
					if (position > 7 | position < 1)
						continue;

					if (!placeMove(board, position, player)) {
						printBoard(board);
						System.out.println("Column full");
						continue;
					}
					printBoard(board);
				}

				// AIs move
				else {
					player = 'Y';
					long start = System.currentTimeMillis();
					int position;
					if (turn <= 2) {
						position = 4;
					} else if (turn < 6) {
						position = minAlphaBeta(board, -1001, 1001, 0, 12, true);
					} else if (turn < 9) {
						position = minAlphaBeta(board, -1001, 1001, 0, 14, true);
					} else if (turn < 16) {
						position = minAlphaBeta(board, -1001, 1001, 0, 16, true);
					} else if (turn < 20) {
						position = minAlphaBeta(board, -1001, 1001, 0, 18, true);
					} else {
						position = minAlphaBeta(board, -1001, 1001, 0, 200, true);
					}

					/**
					 * // If max depth is reached, take value from optimal moves table if
					 * (maxDepthReached) { int priority = 0; for (int i = 0; i < 7; i++) { for (int
					 * j = 0; j < 6; j++) { if (board[5 - j][i] == ' ') { if (optimalTable[5 - j][i]
					 * > priority) { position = i + 1; priority = optimalTable[5 - j][i]; } break; }
					 * } } System.out.println("PRIORITY CHECK");
					 * 
					 * maxDepthReached = false; }
					 */

					long end = System.currentTimeMillis();
					float secondsElapsed = (end - start) / 1000F;
					placeMove(board, position, player);
					printBoard(board);
					System.out.println("Time elapsed: " + secondsElapsed + " seconds\n");
					System.out.println("AI chose position " + position);
				}

				alternator++;
				turn++;
				if (winCondition(board, player) == 1) {
					System.out.println("\nTurn:" + turn);
				}

			}

			if (winCondition(board, player) == 44)
				System.out.println("Draw");
			else
				System.out.println("Winner: " + player);

			System.out.println("Continue Playing? (Y or N): ");
			continuePlaying = sc.next();

		}
		System.out.println("Bye!");
		sc.close();
	}
}
