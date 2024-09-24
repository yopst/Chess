package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //DIAG UP RIGHT
        ChessPosition current = new ChessPosition(position.getRow(), position.getColumn());
        while (true) {
            ChessPosition upRight = new ChessPosition(current.getRow() + 1, current.getColumn() + 1);
            if (board.emptySpaceOnBoard(upRight)) {
                validMoves.add(new ChessMove(position, upRight));
                current = upRight;
            }
            //attack
            else if (board.onBoard(upRight) &&
                    board.getPiece(upRight).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, upRight));
                break;
            } else {
                break;
            }
        }
        //DIAG DOWN LEFT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while (true) {
            ChessPosition downLeft = new ChessPosition(current.getRow() - 1, current.getColumn() - 1);
            if (board.emptySpaceOnBoard(downLeft)) {
                validMoves.add(new ChessMove(position, downLeft));
                current = downLeft;
            }
            //attack
            else if (board.onBoard(downLeft) &&
                    board.getPiece(downLeft).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, downLeft));
                break;
            } else {
                break;
            }
        }
        //DIAG UP LEFT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while (true) {
            ChessPosition upLeft = new ChessPosition(current.getRow() + 1, current.getColumn() - 1);
            if (board.emptySpaceOnBoard(upLeft)) {
                validMoves.add(new ChessMove(position, upLeft));
                current = upLeft;
            }
            //attack
            else if (board.onBoard(upLeft) &&
                    board.getPiece(upLeft).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, upLeft));
                break;
            } else {
                break;
            }
        }
        //DIAG DOWN RIGHT
        current = new ChessPosition(position.getRow(), position.getColumn());
        while (true) {
            ChessPosition downRight = new ChessPosition(current.getRow() - 1, current.getColumn() + 1);
            if (board.emptySpaceOnBoard(downRight)) {
                validMoves.add(new ChessMove(position, downRight));
                current = downRight;
            }
            //attack
            else if (board.onBoard(downRight) &&
                    board.getPiece(downRight).getTeamColor() != color) {
                validMoves.add(new ChessMove(position, downRight));
                break;
            } else {
                break;
            }
        }
        return validMoves;
    }
}
