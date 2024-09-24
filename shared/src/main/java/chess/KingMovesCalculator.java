package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPosition> positions = new ArrayList<>();

        ChessPosition upOne = new ChessPosition(position.getRow() + 1, position.getColumn());
        positions.add(upOne);
        ChessPosition downOne = new ChessPosition(position.getRow() - 1, position.getColumn());
        positions.add(downOne);
        ChessPosition rightOne = new ChessPosition(position.getRow(), position.getColumn() + 1);
        positions.add(rightOne);
        ChessPosition leftOne = new ChessPosition(position.getRow(), position.getColumn() - 1);
        positions.add(leftOne);

        ChessPosition diagUpRight = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
        positions.add(diagUpRight);
        ChessPosition diagUpLeft = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
        positions.add(diagUpLeft);
        ChessPosition diagDownRight = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
        positions.add(diagDownRight);
        ChessPosition diagDownLeft = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
        positions.add(diagDownLeft);

        for (ChessPosition pos: positions) {
            if (board.emptySpaceOnBoard(pos)) {
                validMoves.add(new ChessMove(position,pos));
            }
            else if (board.onBoard(pos) && board.getPiece(pos).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,pos));
            }
        }
        return null;
    }
}
