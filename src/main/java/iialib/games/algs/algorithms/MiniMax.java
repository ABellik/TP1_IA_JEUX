package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class MiniMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	/** Defaut value for depth limit 
     */
	private final static int DEPTH_MAX_DEFAUT = 4;

	// Attributes
	/** Role of the max player 
     */
	private final Role playerMaxRole;

	/** Role of the min player 
     */
	private final Role playerMinRole;

	/** Algorithm max depth
     */
	private int depthMax = DEPTH_MAX_DEFAUT;

	
	/** Heuristic used by the max player 
     */
	private IHeuristic<Board, Role> h;

	//
	/** number of internal visited (developed) nodes (for stats)
     */
	private int nbNodes;
	
	/** number of leaves nodes nodes (for stats)

     */
	private int nbLeaves;

	// --------- Constructors ---------

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	//
	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[MiniMax]");

		Move bestMove = null;
		nbNodes = 0;
		nbLeaves = 0;
		int bestValue = IHeuristic.MIN_VALUE;

		nbNodes++;
		for (Move move : board.possibleMoves(playerMaxRole)) {
			Board next = board.play(move, playerMaxRole);
			int value = minMax(next, 1);                    // à l'adversaire de répondre
			if (bestMove == null || value > bestValue) {
				bestValue = value;
				bestMove = move;
			}
		}

		System.out.println("Noeuds : " + nbNodes + ", feuilles : " + nbLeaves);
		return bestMove;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "MiniMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */

	private int maxMin(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {      // EstFeuille
			nbLeaves++;
			return h.eval(board, playerMaxRole);            // feval(AMI, etat)
		}
		nbNodes++;
		int best = IHeuristic.MIN_VALUE;                    // Meilleur <- -infini
		for (Move move : board.possibleMoves(playerMaxRole)) {
			Board next = board.play(move, playerMaxRole);   // successeur s
			best = Math.max(best, minMax(next, depth + 1)); // max(Meilleur, MinMax(s))
		}
		return best;
	}

	private int minMax(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {      // EstFeuille
			nbLeaves++;
			return h.eval(board, playerMinRole);            // feval(AMI, etat)
		}
		nbNodes++;
		int best = IHeuristic.MAX_VALUE;                    // Meilleur <- -infini
		for (Move move : board.possibleMoves(playerMinRole)) {
			Board next = board.play(move, playerMinRole);   // successeur s
			best = Math.min(best, maxMin(next, depth + 1)); // max(Meilleur, MinMax(s))
		}
		return best;
	}
}
