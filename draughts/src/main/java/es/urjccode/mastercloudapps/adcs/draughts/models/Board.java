package es.urjccode.mastercloudapps.adcs.draughts.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Board implements PieceProvider {

    private static final int DIMENSION = 8;
    private Piece[][] pieces;

    Board() {
        this.pieces = new Piece[this.getDimension()][this.getDimension()];
        for (int i = 0; i < this.getDimension(); i++) {
            for (int j = 0; j < this.getDimension(); j++) {
                this.pieces[i][j] = null;
            }
        }
    }

    void put(Coordinate coordinate, Piece piece) {
        assert piece != null;
        this.pieces[coordinate.getRow()][coordinate.getColumn()] = piece;
    }

    Piece remove(Coordinate coordinate) {
        assert this.getPiece(coordinate) != null;
        Piece removedPiece = this.getPiece(coordinate);
        this.pieces[coordinate.getRow()][coordinate.getColumn()] = null;
        return removedPiece;
    }

    void move(Coordinate origin, Coordinate target) {
        this.put(target, this.remove(origin));
    }

    @Override
    public Piece getPiece(Coordinate coordinate) {
        return this.pieces[coordinate.getRow()][coordinate.getColumn()];
    }

    @Override
    public boolean isEmpty(Coordinate coordinate) {
        return this.pieces[coordinate.getRow()][coordinate.getColumn()] == null;
    }

    Color getColor(Coordinate coordinate) {
        if (this.pieces[coordinate.getRow()][coordinate.getColumn()] == null){
            return null;
        }
		return this.pieces[coordinate.getRow()][coordinate.getColumn()].getColor();
    }

    List<Piece> getPieces(Color color) {
        List<Piece> pieces = new ArrayList<Piece>();
        for (int i = 0; i < this.getDimension(); i++) {
            for (int j = 0; j < this.getDimension(); j++) {
                pieces.add(this.pieces[i][j]);
            }
        }
        return pieces;
    }

    int getDimension() {
        return Board.DIMENSION;
    }

    @Override
    public String toString() {
        String string = "";
        string += this.toStringHorizontalNumbers();
        for (int i = 0; i < this.getDimension(); i++) {
            string += this.toStringHorizontalPiecesWithNumbers(i);
        }
        string += this.toStringHorizontalNumbers();
        return string;
    }

    private String toStringHorizontalNumbers() {
        String string = " ";
        for (int j = 0; j < Board.DIMENSION; j++) {
            string += j;
        }
        return string + "\n";
    }

    private String toStringHorizontalPiecesWithNumbers(int row) {
        String string = "." + row;
        for (int j = 0; j < this.getDimension(); j++) {
            Piece piece = this.getPiece(new Coordinate(row, j));
            if (piece == null) {
                string += " ";
            } else {
                final String[] letters = { "b", "n" };
                string += letters[piece.getColor().ordinal()];
            }
        }
        return string + row + "\n";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(pieces);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (!Arrays.deepEquals(pieces, other.pieces))
            return false;
        return true;
    }

}