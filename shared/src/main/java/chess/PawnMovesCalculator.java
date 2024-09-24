package chess;

import java.util.Collection;
import java.util.ArrayList;

public class PawnMovesCalculator implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        //move forward
        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition verticalMove1 = new ChessPosition(position.getRow() + 1, position.getColumn());
            if (board.emptySpaceOnBoard(verticalMove1)) {
                ChessMove move = new ChessMove(position, verticalMove1);
                if (verticalMove1.getRow() == 8) {
                    move = new ChessMove(position, verticalMove1, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            ChessPosition verticalMove1 = new ChessPosition(position.getRow() - 1, position.getColumn());
            if (board.emptySpaceOnBoard(verticalMove1)) {
                ChessMove move = new ChessMove(position, verticalMove1);
                if (verticalMove1.getRow() == 1) {
                    move = new ChessMove(position, verticalMove1, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, verticalMove1, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
        }

        //double move forward at start of game, at starting position and is not blocked
        if (position.getRow() == 2 &&
                color == ChessGame.TeamColor.WHITE &&
                validMoves.isEmpty() != true) { //can move 1 vertical
            ChessPosition verticalMove2 = new ChessPosition(position.getRow() + 2, position.getColumn());

            if (board.emptySpaceOnBoard(verticalMove2)) { //can move 2 vertical
                ChessMove move = new ChessMove(position, verticalMove2);
                validMoves.add(move);
            }
        }
        if (position.getRow() == 7 &&
                color == ChessGame.TeamColor.BLACK &&
                validMoves.isEmpty() != true) { //can move 1 vertical
            ChessPosition verticalMove2 = new ChessPosition(position.getRow() - 2, position.getColumn());

            if (board.emptySpaceOnBoard(verticalMove2)) { //can move 2 vertical
                ChessMove move = new ChessMove(position, verticalMove2);
                validMoves.add(move);
            }
        }

        //simple attack
        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition diagonal1Left = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
            ChessPosition diagonal1Right = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
            if (board.onBoard(diagonal1Left) &&
                    !board.emptySpaceOnBoard(diagonal1Left) &&
                    board.getPiece(diagonal1Left).getTeamColor() != color) {
                ChessMove move = new ChessMove(position, diagonal1Left);
                if (diagonal1Left.getRow() == 8) {
                    move = new ChessMove(position, diagonal1Left, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
            if (board.onBoard(diagonal1Right) &&
                    !board.emptySpaceOnBoard(diagonal1Right) &&
                    board.getPiece(diagonal1Right).getTeamColor() != color) {
                ChessMove move = new ChessMove(position, diagonal1Right);
                if (diagonal1Right.getRow() == 8) {
                    move = new ChessMove(position, diagonal1Right, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            ChessPosition diagonal1Left = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
            ChessPosition diagonal1Right = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
            if (board.onBoard(diagonal1Left) &&
                    !board.emptySpaceOnBoard(diagonal1Left) &&
                    board.getPiece(diagonal1Left).getTeamColor() != color) {
                ChessMove move = new ChessMove(position, diagonal1Left);
                if (diagonal1Left.getRow() == 1) {
                    move = new ChessMove(position, diagonal1Left, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, diagonal1Left, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
            if (board.onBoard(diagonal1Right) &&
                    !board.emptySpaceOnBoard(diagonal1Right) &&
                    board.getPiece(diagonal1Right).getTeamColor() != color) {
                ChessMove move = new ChessMove(position, diagonal1Right);
                if (diagonal1Right.getRow() == 1) {
                    move = new ChessMove(position, diagonal1Right, ChessPiece.PieceType.QUEEN);
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.KNIGHT));
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, diagonal1Right, ChessPiece.PieceType.ROOK));
                }
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}
