package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] chessBoard  = new ChessPiece[8][8];
    private ChessMove lastMove;

    public ChessBoard(ChessBoard board_) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                this.addPiece(pos,board_.getPiece(pos));
            }
        }
        lastMove = board_.getLastMove();
    }
    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        chessBoard[position.getRow()-1][position.getColumn()-1] = null;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow()-1][position.getColumn()-1];
    }


    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    public ChessMove getLastMove() {
        return lastMove;
    }

    public void setLastMove(ChessMove move) {
        lastMove = move;
    }

    public ChessPosition whereIsTheKing(ChessGame.TeamColor color) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = this.getPiece(pos);
                if (piece != null &&
                        piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }

    public boolean emptySpaceOnBoard(ChessPosition position) {
        if (!this.onBoard(position)) {
            return false;
        }
        if (this.getPiece(position) != null) {
            return false;
        }
        return true;
    }

    public boolean onBoard(ChessPosition position) {
        if (position.getRow() > 8
                || position.getRow() < 1
                || position.getColumn() > 8
                || position.getColumn() < 1) {
            return false;
        }
        return true;
    }
    public String to_string() {
        StringBuilder sb = new StringBuilder();
        for (ChessPiece[] row : chessBoard) {
            for (ChessPiece piece: row) {
                sb.append(piece.toString());
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
