package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //UP AND RIGHT
        ChessPosition tallL = new ChessPosition(position.getRow() + 2, position.getColumn() + 1);
        ChessPosition shortL = new ChessPosition(position.getRow() + 1, position.getColumn() + 2);
        if (board.emptySpaceOnBoard(tallL)) {
            validMoves.add(new ChessMove(position,tallL));
        }
        else if (board.onBoard(tallL) && board.getPiece(tallL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,tallL));
        }
        if (board.emptySpaceOnBoard(shortL)) {
            validMoves.add(new ChessMove(position,shortL));
        }
        else if (board.onBoard(shortL) && board.getPiece(shortL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,shortL));
        }
        //UP AND LEFT
        tallL = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
        shortL = new ChessPosition(position.getRow() + 1, position.getColumn() - 2);
        if (board.emptySpaceOnBoard(tallL)) {
            validMoves.add(new ChessMove(position,tallL));
        }
        else if (board.onBoard(tallL) && board.getPiece(tallL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,tallL));
        }
        if (board.emptySpaceOnBoard(shortL)) {
            validMoves.add(new ChessMove(position,shortL));
        }
        else if (board.onBoard(shortL) && board.getPiece(shortL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,shortL));
        }
        //DOWN AND LEFT
        tallL = new ChessPosition(position.getRow() - 2, position.getColumn() - 1);
        shortL = new ChessPosition(position.getRow() - 1, position.getColumn() - 2);
        if (board.emptySpaceOnBoard(tallL)) {
            validMoves.add(new ChessMove(position,tallL));
        }
        else if (board.onBoard(tallL) && board.getPiece(tallL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,tallL));
        }
        if (board.emptySpaceOnBoard(shortL)) {
            validMoves.add(new ChessMove(position,shortL));
        }
        else if (board.onBoard(shortL) && board.getPiece(shortL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,shortL));
        }
        //DOWN AND RIGHT
        tallL = new ChessPosition(position.getRow() - 2, position.getColumn() + 1);
        shortL = new ChessPosition(position.getRow() - 1, position.getColumn() + 2);
        if (board.emptySpaceOnBoard(tallL)) {
            validMoves.add(new ChessMove(position,tallL));
        }
        else if (board.onBoard(tallL) && board.getPiece(tallL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,tallL));
        }
        if (board.emptySpaceOnBoard(shortL)) {
            validMoves.add(new ChessMove(position,shortL));
        }
        else if (board.onBoard(shortL) && board.getPiece(shortL).getTeamColor() != color) {
            validMoves.add(new ChessMove(position,shortL));
        }

        return validMoves;
    }
}
