package chess;

import javax.swing.text.Position;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        board = new ChessBoard();
        board.resetBoard();
    }

    private void changeTurns() {
        if (turn == TeamColor.WHITE) {
            turn = TeamColor.BLACK;
        }
        else {
            turn = TeamColor.WHITE;
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.whereIsTheKing(teamColor);
        //for each pieceType, determine if valid moves at positions of opponent piece of pieceType are null or there
        //Pawn attack (attack moves different from valid moves)
        ChessPosition diagLeft;
        ChessPosition diagRight;
        if (teamColor == TeamColor.BLACK) {
            diagLeft = new ChessPosition(kingPosition.getRow()-1, kingPosition.getColumn()-1);
            diagRight = new ChessPosition(kingPosition.getRow()-1, kingPosition.getColumn()+1);
        }
        else {
            diagLeft = new ChessPosition(kingPosition.getRow()+1, kingPosition.getColumn()-1);
            diagRight = new ChessPosition(kingPosition.getRow()+1, kingPosition.getColumn()+1);
        }
        if (board.onBoard(diagLeft) && !board.emptySpaceOnBoard(diagLeft)) {
            if (board.getPiece(diagLeft).getPieceType() == ChessPiece.PieceType.PAWN &&
                    board.getPiece(diagLeft).getTeamColor() != teamColor) {
                return true;
            }
        }
        if (board.onBoard(diagRight) && !board.emptySpaceOnBoard(diagRight)) {
            if (board.getPiece(diagRight).getPieceType() == ChessPiece.PieceType.PAWN &&
                    board.getPiece(diagRight).getTeamColor() != teamColor) {
                return true;
            }
        }

        // Imagine if there was a piece of pieceType at the kings position,
        // if they could attack a piece of the opponent of the same type then the king is inCheck
        //Knight
        ChessPiece kingKnight = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
        Collection<ChessMove> knightMoves = kingKnight.pieceMoves(board, kingPosition);
        for (ChessMove move: knightMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KNIGHT) {
                return true;
            }
        }


        //Rook
        ChessPiece kingRook = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        Collection<ChessMove> rookMoves = kingRook.pieceMoves(board,kingPosition);
        for (ChessMove move: rookMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.ROOK ||
                            board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        //Bishop
        ChessPiece kingBishop = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
        Collection<ChessMove> bishopMoves = kingBishop.pieceMoves(board,kingPosition);
        for (ChessMove move: bishopMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    (board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.BISHOP ||
                            board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.QUEEN)) {
                return true;
            }
        }

        //King
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> kingMoves = king.pieceMoves(board,kingPosition);
        for (ChessMove move: kingMoves) {
            if (!board.emptySpaceOnBoard(move.getEndPosition()) &&
                    board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }
}
