package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

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

    public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
        this.playerMaxRole = playerMaxRole;
        this.playerMinRole = playerMinRole;
        this.h = h;
    }

    //
    public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
        this(playerMaxRole, playerMinRole, h);
        this.depthMax = depthMax;
    }

    /*
     * IAlgo METHODS =============
     */

    @Override
    public Move bestMove(Board board, Role playerRole) {
        System.out.println("[AlphaBeta]");

        Move bestMove = null;
        nbNodes = 0;
        nbLeaves = 0;
        int alpha = IHeuristic.MIN_VALUE;
        int beta = IHeuristic.MAX_VALUE;

        nbNodes++;
        for (Move move : board.possibleMoves(playerMaxRole)) {
            Board next = board.play(move, playerMaxRole);
            int value = minMax(next, 1, alpha, beta);                    // à l'adversaire de répondre
            if (bestMove == null || value > alpha) {
                alpha = value;
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
        return "AlphaBeta(ProfMax=" + depthMax + ")";
    }

    /*
     * PRIVATE METHODS ===============
     */

    private int maxMin(Board board, int depth, int alpha, int beta) {
        if (depth == depthMax || board.isGameOver()) {      // EstFeuille
            nbLeaves++;
            return h.eval(board, playerMaxRole);            // feval(AMI, etat)
        }
        nbNodes++;
        for (Move move : board.possibleMoves(playerMaxRole)) {
            Board next = board.play(move, playerMaxRole);
            alpha = Math.max(alpha, minMax(next, depth + 1, alpha, beta));
            if (alpha >= beta) return beta;      // coupure : ENNEMI n'ira pas ici
        }
        return alpha;
    }

    private int minMax(Board board, int depth, int alpha, int beta) {
        if (depth == depthMax || board.isGameOver()) {      // EstFeuille
            nbLeaves++;
            return h.eval(board, playerMinRole);            // feval(ENNEMI, etat)
        }
        nbNodes++;
        for (Move move : board.possibleMoves(playerMinRole)) {
            Board next = board.play(move, playerMinRole);
            beta = Math.min(beta, maxMin(next, depth + 1, alpha, beta));
            if (alpha >= beta) return alpha;      // coupure : AMI n'ira pas ici
        }
        return beta;
    }
}
