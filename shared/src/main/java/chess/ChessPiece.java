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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
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

    public PieceMovesCalculator getCalculator() {
        switch (type) {
            case PAWN:
                return new PawnMovesCalculator();
            case ROOK:
                return new RookMovesCalculator();
            case KNIGHT:
                return new KnightMovesCalculator();
            case BISHOP:
                return new BishopMovesCalculator();
            case KING:
                return new KingMovesCalculator();
            case QUEEN:
                return new QueenMovesCalculator();
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }

    public String toString() {
        switch (type) {
            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) return "p";
                return "P";
            case ROOK:
                if (color == ChessGame.TeamColor.BLACK) return "r";
                return "R";
            case KNIGHT:
                if (color == ChessGame.TeamColor.BLACK) return "n";
                return "N";
            case BISHOP:
                if (color == ChessGame.TeamColor.BLACK) return "b";
                return "B";
            case KING:
                if (color == ChessGame.TeamColor.BLACK) return "k";
                return "K";
            case QUEEN:
                if (color == ChessGame.TeamColor.BLACK) return "q";
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
        return getCalculator().calculateMoves(board, myPosition);
    }
}
