package games.dominos;

import iialib.games.algs.IHeuristic;

import java.util.ArrayList;

public class DominosHeuristics {
	
	public static IHeuristic<DominosBoard,DominosRole>  hVertical = (board,role) -> {

		int nbV = board.nbVerticalMoves();
		int nbH = board.nbHorizontalMoves();

		if (nbH == 0) return IHeuristic.MAX_VALUE;
		if (nbV == 0) return IHeuristic.MIN_VALUE;

		return nbV - nbH;

    };
    

	public static IHeuristic<DominosBoard,DominosRole> hHorizontal = (board,role) -> {

		int nbV = board.nbVerticalMoves();
		int nbH = board.nbHorizontalMoves();

		if (nbH == 0) return IHeuristic.MIN_VALUE;
		if (nbV == 0) return IHeuristic.MAX_VALUE;

		return nbH - nbV;

	};
   
}
	