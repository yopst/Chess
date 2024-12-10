package chess;

import java.util.ArrayList;
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
    private ChessBoard lastBoard;

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

    //doesn't check for validmoves exceptions
    //doesn't set last move
    private void undoMove() {
        ChessMove reverseMove = board.getLastMove().reverseMove();
        ChessPosition start = reverseMove.getStartPosition();
        ChessPosition end = reverseMove.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        board.addPiece(end,piece);
        board.removePiece(start);
        this.changeTurns();
    }

    private void revertBoard() {
        board = lastBoard;
    }

    public ChessPiece promotePiece(ChessMove move) {
        if (move.getPromotionPiece() != null) {
            ChessPiece newPiece;
            TeamColor pieceColor = turn;
            ChessPiece.PieceType type = move.getPromotionPiece();
            switch (type) {
                case PAWN:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN);
                    break;
                case ROOK:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK);
                    break;
                case BISHOP:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP);
                    break;
                case KNIGHT:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT);
                    break;
                case QUEEN:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN);
                    break;
                case KING:
                    newPiece = new ChessPiece(pieceColor, ChessPiece.PieceType.KING);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported piece type: " + type);
            }
            return newPiece;
        }
        return null;
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
        Collection<ChessMove> potentialMoves = board.getPiece(startPosition).pieceMoves(board,startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        //If it were your turn
        boolean changedTeams = false;
        if (board.getPiece(startPosition).getTeamColor() != turn) {
            this.changeTurns();
            changedTeams = true;
        }
        for (ChessMove move: potentialMoves) {
            try {
                ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                ChessMove pausedLastMove = board.getLastMove();
                this.makeMove(move);
                this.undoMove();
                board.setLastMove(pausedLastMove);
                board.addPiece(move.getEndPosition(),capturedPiece);
                validMoves.add(move);
            } catch (InvalidMoveException e) {
                //doesn't add the move if exception thrown
            }
        }
        if (changedTeams) {
            //change back
            this.changeTurns();
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            throw new InvalidMoveException("Invalid move: there is no piece at start position.");
        }
        if (piece.getTeamColor() != turn) {
            throw new InvalidMoveException("Invalid move: The piece being moved is not of the teamcolor who's turn it is.");
        }
        if (!board.onBoard(start) || board.emptySpaceOnBoard(start)) {
            throw new InvalidMoveException("Invalid move: Start position must be on the board and not empty.");
        }
        if (!board.onBoard(end)) {
            throw new InvalidMoveException("Invalid move: END position must be on the board and not empty.");
        }

        //Is this move even a potentialMove given the pieceMoves
        Collection<ChessMove> moves = board.getPiece(move.getStartPosition()).pieceMoves(board,move.getStartPosition());
        if (moves.contains(move)) {
            lastBoard = board;
            if (board.whereIsTheKing(turn) != null) {
                ChessPiece capturedPiece = board.getPiece(end);
                if (this.promotePiece(move) != null) {
                    board.addPiece(end, this.promotePiece(move));
                }
                else {
                    board.addPiece(end,piece);
                }
                board.removePiece(start);
                ChessMove pausedLastMove = board.getLastMove();
                board.setLastMove(move);
                if (move.getIsEnPassant()) {
                    if (board.getPiece(end).getTeamColor() == TeamColor.WHITE) {
                        ChessPosition downOne = new ChessPosition(end.getRow() - 1, end.getColumn());
                        capturedPiece = board.getPiece(downOne);
                        board.removePiece(downOne);
                    }
                    else {
                        ChessPosition upOne = new ChessPosition(end.getRow() + 1, end.getColumn());
                        capturedPiece = board.getPiece(upOne);
                        board.removePiece(upOne);
                    }
                }
                if (isInCheck(turn)) {
                    this.undoMove();
                    if (move.getIsEnPassant()) {
                        this.revertBoard();
                    }
                    else {
                        board.addPiece(end,capturedPiece);
                    }
                    this.changeTurns();
                    board.setLastMove(pausedLastMove);
                    throw new InvalidMoveException("Invalid Move: your King is in check by making this move.");
                }
            }
            else { //some tests require making moves without a king

                if (this.promotePiece(move) != null) {
                    board.addPiece(end,this.promotePiece(move));
                }
                else {
                    board.addPiece(end,piece);
                }
                board.removePiece(start);
                if (move.getIsEnPassant()) {
                    if (board.getPiece(end).getTeamColor() == TeamColor.WHITE) {
                        ChessPosition downOne = new ChessPosition(end.getRow() - 1, end.getColumn());
                        board.removePiece(downOne);
                    }
                    else {
                        ChessPosition upOne = new ChessPosition(end.getRow() + 1, end.getColumn());
                        board.removePiece(upOne);
                    }
                }
                board.setLastMove(move);
            }
            this.changeTurns();
        }
        else {
            throw new InvalidMoveException("Invalid move: The piece at start position cannot be moved in this way.");
        }
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
        if (!isInCheck(teamColor)) {
            return false;
        }

        //For every piece of your team, try to make a
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                if (!board.emptySpaceOnBoard(pos) &&
                        board.getPiece(pos).getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = board.getPiece(pos).pieceMoves(board,pos);
                    for (ChessMove move: moves) {
                        try {
                            ChessMove pausedLastMove = board.getLastMove();
                            this.makeMove(move);
                            this.undoMove();
                            board.setLastMove(pausedLastMove);
                            return false;
                        } catch (InvalidMoveException _) { }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor != turn) {
            return false;
        }
        if (this.isInCheckmate(teamColor)) {
            return false;
        }
        int numMovesAvailable = 0;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                if (!board.emptySpaceOnBoard(pos) &&
                        board.getPiece(pos).getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = board.getPiece(pos).pieceMoves(board,pos);
                    numMovesAvailable += moves.size();
                    for (ChessMove move: moves) {
                        try {
                            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                            ChessMove pausedLastMove = board.getLastMove();
                            this.makeMove(move);
                            this.undoMove();
                            board.addPiece(move.getEndPosition(),capturedPiece);
                            board.setLastMove(pausedLastMove);
                            return false;
                        } catch (InvalidMoveException e) {
                            numMovesAvailable--;
                        }
                    }
                }
            }
        }
        return numMovesAvailable == 0;
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
