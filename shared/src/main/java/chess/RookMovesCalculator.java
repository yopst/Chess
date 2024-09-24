package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //Vertical UP
        ChessPosition current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition upOne = new ChessPosition(current.getRow() + 1, current.getColumn());
            if (board.emptySpaceOnBoard(upOne)) {
                validMoves.add(new ChessMove(position,upOne));
                current = upOne;
            }
            //attack
            else if(current.getRow() + 1 <= 8 &&
                    board.getPiece(upOne).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,upOne));
                break;
            }
            else {
                break;
            }
        }
        //Vertical DOWN
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition downOne = new ChessPosition(current.getRow() - 1, current.getColumn());
            if (board.emptySpaceOnBoard(downOne)) {
                validMoves.add(new ChessMove(position,downOne));
                current = downOne;
            }
            //attack
            else if(current.getRow() - 1 >= 1 &&
                    board.getPiece(downOne).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,downOne));
                break;
            }
            else {
                break;
            }
        }
        //Horizontal Left
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition leftOne = new ChessPosition(current.getRow(), current.getColumn() - 1);
            if (board.emptySpaceOnBoard(leftOne)) {
                validMoves.add(new ChessMove(position,leftOne));
                current = leftOne;
            }
            //attack
            else if(current.getColumn() - 1 >= 1 &&
                    board.getPiece(leftOne).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,leftOne));
                break;
            }
            else {
                break;
            }
        }
        //Horizontal Right
        current = new ChessPosition(position.getRow(), position.getColumn());
        while(true) {
            ChessPosition rightOne = new ChessPosition(current.getRow(), current.getColumn() + 1);
            if (board.emptySpaceOnBoard(rightOne)) {
                validMoves.add(new ChessMove(position,rightOne));
                current = rightOne;
            }
            //attack
            else if(current.getColumn() + 1 <= 8 &&
                    board.getPiece(rightOne).getTeamColor() != color) {
                validMoves.add(new ChessMove(position,rightOne));
                break;
            }
            else {
                break;
            }
        }
        return validMoves;
    }
}
