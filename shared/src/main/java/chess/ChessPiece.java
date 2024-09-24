package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor color;
    ChessPiece.PieceType type;
    PieceMovesCalculator calculator;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
        getCalculator();
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public void getCalculator() {
        switch (type) {
            case PAWN:
                calculator = new PawnMovesCalculator();
            case ROOK:
                calculator = new RookMovesCalculator();
            case KNIGHT:
                calculator = new KnightMovesCalculator();
            case BISHOP:
                calculator = new BishopMovesCalculator();
            case KING:
                calculator = new KingMovesCalculator();
            case QUEEN:
                calculator = new QueenMovesCalculator();
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    public String toString() {
        switch (type) {
            case PAWN:
                return "P";
            case ROOK:
                return "R";
            case KNIGHT:
                return "N";
            case BISHOP:
                return "B";
            case KING:
                return "K";
            case QUEEN:
                return "Q";
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }


        /**
         * @return Which team this chess piece belongs to
         */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return calculator.calculateMoves(board, myPosition);
    }
}
