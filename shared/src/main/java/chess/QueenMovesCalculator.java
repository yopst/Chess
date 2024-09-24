package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        RookMovesCalculator rookCalc = new RookMovesCalculator();
        BishopMovesCalculator bishopCalc = new BishopMovesCalculator();

        Collection<ChessMove> rookMoves = rookCalc.calculateMoves(board,position);
        Collection<ChessMove> bishopMoves = bishopCalc.calculateMoves(board,position);

        validMoves.addAll(rookMoves);
        validMoves.addAll(bishopMoves);

        return validMoves;
    }
}
