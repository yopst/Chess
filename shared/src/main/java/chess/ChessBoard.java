package chess;

import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] chessBoard  = new ChessPiece[8][8];
    private ChessMove lastMove;

    public ChessBoard(ChessBoard board) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                this.addPiece(pos, board.getPiece(pos));
            }
        }
        lastMove = board.getLastMove();
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
        chessBoard = new ChessPiece[8][8];

        //set Pawns
        for (int column = 1; column <= 8; column++) {
            ChessPiece pawnW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPiece pawnB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(new ChessPosition(2,column), pawnW);
            addPiece(new ChessPosition(7,column), pawnB);
        }

        //set Rooks
        for (int i = 1; i <= 2; i++) {
            int col = 1;
            if (i == 2) {
                col = 8;
            }
            ChessPiece rookW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
            ChessPiece rookB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

            ChessPosition positionW = new ChessPosition(1,col);
            ChessPosition positionB = new ChessPosition(8,col);
            addPiece(positionW, rookW);
            addPiece(positionB, rookB);
        }

        // Set Knights
        for (int i = 1; i <= 2; i++) {
            int col = 2;
            if (i == 2) {
                col = 7;
            }
            ChessPiece knightW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
            ChessPiece knightB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

            ChessPosition positionW = new ChessPosition(1,col);
            ChessPosition positionB = new ChessPosition(8,col);
            addPiece(positionW, knightW);
            addPiece(positionB, knightB);
        }

        //Set Bishops
        for (int i = 1; i <= 2; i++) {
            int col = 3;
            if (i == 2) {
                col = 6;
            }
            ChessPiece bishopW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
            ChessPiece bishopB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

            ChessPosition positionW = new ChessPosition(1,col);
            ChessPosition positionB = new ChessPosition(8,col);
            addPiece(positionW, bishopW);
            addPiece(positionB, bishopB);
        }

        //Set Queens
        ChessPiece queenW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece queenB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        ChessPosition positionW = new ChessPosition(1,4);
        ChessPosition positionB = new ChessPosition(8,4);
        addPiece(positionW, queenW);
        addPiece(positionB, queenB);

        //Set Kings
        ChessPiece kingW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece kingB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

        positionW = new ChessPosition(1,5);
        positionB = new ChessPosition(8,5);
        addPiece(positionW, kingW);
        addPiece(positionB, kingB);
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
        return this.getPiece(position) == null;
    }

    public boolean onBoard(ChessPosition position) {
        return position.getRow() <= 8
                && position.getRow() >= 1
                && position.getColumn() <= 8
                && position.getColumn() >= 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ChessPiece[] row : chessBoard) {
            for (ChessPiece piece: row) {
                if (piece != null) {
                    sb.append(piece);
                    sb.append(",");
                }
            }
            sb.append("\n");
        }
        System.out.println(whereIsTheKing(ChessGame.TeamColor.WHITE).toString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(this.toString(), that.toString());
    }
}
