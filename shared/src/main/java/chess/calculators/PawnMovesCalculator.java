package chess.calculators;

import chess.*;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private ChessGame.TeamColor color;

    private ChessPosition leftDiag(ChessPosition pos) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() - 1);
        }
        return new ChessPosition(pos.getRow() - 1, pos.getColumn() - 1);
    }

    private ChessPosition rightDiag(ChessPosition pos) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn() + 1);
        }
        return new ChessPosition(pos.getRow() - 1, pos.getColumn() + 1);
    }

    private ChessPosition verticalOne (ChessPosition pos) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new ChessPosition(pos.getRow() + 1, pos.getColumn());
        }
        return new ChessPosition(pos.getRow() - 1, pos.getColumn());
    }

    private ChessPosition verticalTwo (ChessPosition pos) {
        if (color == ChessGame.TeamColor.WHITE) {
            return new ChessPosition(pos.getRow() + 2, pos.getColumn());
        }
        return new ChessPosition(pos.getRow() - 2, pos.getColumn());
    }

    private int promotionRow() {
        if (color == ChessGame.TeamColor.WHITE) {
            return 8;
        }
        else {
            return 1;
        }
    }

    private int startRow() {
        if (color == ChessGame.TeamColor.WHITE) {
            return 2;
        }
        else {
            return 7;
        }
    }

    private boolean attackConditions(ChessBoard board, ChessPosition pos) {
        return board.onBoard(pos) &&
                !board.emptySpaceOnBoard(pos) &&
                board.getPiece(pos).getTeamColor() != color;
    }

    private void addMoves(ChessPosition start ,ChessPosition end, ArrayList<ChessMove> validMoves, ChessMove move) {
        if (end.getRow() == promotionRow()) {
            move = new ChessMove(start, end, ChessPiece.PieceType.QUEEN);
            validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
            validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        }
        validMoves.add(move);
    }

    private boolean isAdjacent(ChessPosition one, ChessPosition two) {
        return one.getRow() == two.getRow()
                && Math.abs(one.getColumn() - two.getColumn()) == 1;
    }

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        color = board.getPiece(position).getTeamColor();

        //move forward
        ChessPosition verticalMove1 = verticalOne(position);
        if (board.emptySpaceOnBoard(verticalMove1)) {
            ChessMove move = new ChessMove(position, verticalMove1);
            addMoves(position, verticalMove1,validMoves, move);
        }

        //double move forward at start of game, at starting position and is not blocked
        if (position.getRow() == startRow() &&
                !validMoves.isEmpty()) { //can move 1 vertical
            ChessPosition verticalMove2 = verticalTwo(position);

            if (board.emptySpaceOnBoard(verticalMove2)) { //can move 2 vertical
                ChessMove move = new ChessMove(position, verticalMove2);
                validMoves.add(move);
            }
        }

        //simple attack
        ChessPosition diagonal1Left = leftDiag(position);
        ChessPosition diagonal1Right = rightDiag(position);
        if (attackConditions(board, diagonal1Left)) {
            ChessMove move = new ChessMove(position, diagonal1Left);
            addMoves(position, diagonal1Left, validMoves, move);
        }
        if (attackConditions(board, diagonal1Right)) {
            ChessMove move = new ChessMove(position, diagonal1Right);
            addMoves(position, diagonal1Right, validMoves, move);
        }

        //en passant "in passing" attack
        ChessMove lastMove = board.getLastMove();

        if (lastMove != null &&
                board.getPiece(lastMove.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN &&
                lastMove.getStartPosition().getRow() - lastMove.getEndPosition().getRow() % 2 == 0 &&
                isAdjacent(position, lastMove.getEndPosition())) {
            if (lastMove.getEndPosition().getColumn() > position.getColumn()) {
                validMoves.add(new ChessMove(position, diagonal1Right));
            }
            validMoves.add(new ChessMove(position, diagonal1Left));
        }


        return validMoves;
    }
}
