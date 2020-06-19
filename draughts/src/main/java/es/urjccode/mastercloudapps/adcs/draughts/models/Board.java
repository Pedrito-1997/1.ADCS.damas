package es.urjccode.mastercloudapps.adcs.draughts.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Board {

    private Piece[][] pieces;

    Board() {
        this.pieces = new Piece[Coordinate.getDimension()][Coordinate.getDimension()];
        for (int i = 0; i < Coordinate.getDimension(); i++)
            for (int j = 0; j < Coordinate.getDimension(); j++)
                this.pieces[i][j] = null;
    }

    Piece getPiece(Coordinate coordinate) {
        assert coordinate != null;
        return this.pieces[coordinate.getRow()][coordinate.getColumn()];
    }

    void put(Coordinate coordinate, Piece piece) {
        this.pieces[coordinate.getRow()][coordinate.getColumn()] = piece;
    }

    Piece remove(Coordinate coordinate) {
        assert this.getPiece(coordinate) != null;
        Piece piece = this.getPiece(coordinate);
        this.put(coordinate, null);
        return piece;
    }

    void move(Coordinate origin, Coordinate target) {
        assert this.getPiece(origin) != null;
        this.put(target, this.remove(origin));
    }

    List<Piece> getBetweenDiagonalPieces(Coordinate origin, Coordinate target) {
        List<Piece> betweenDiagonalPieces = new ArrayList<Piece>();
        if (origin.isOnDiagonal(target))
            for (Coordinate coordinate : origin.getBetweenDiagonalCoordinates(target)) {
                Piece piece = this.getPiece(coordinate);
                if (piece != null)
                    betweenDiagonalPieces.add(piece);
            }
        return betweenDiagonalPieces;
    }

    int getAmountBetweenDiagonalPieces(Coordinate origin, Coordinate target) {
        if (!origin.isOnDiagonal(target))
            return 0;
        int betweenDiagonalPieces = 0;
        for (Coordinate coordinate : origin.getBetweenDiagonalCoordinates(target))
            if (this.getPiece(coordinate) != null)
                betweenDiagonalPieces++;
        return betweenDiagonalPieces;
    }

    Color getColor(Coordinate coordinate) {
        final Piece piece = this.getPiece(coordinate);
        if (piece == null)
            return null;
        return piece.getColor();
    }

    boolean isEmpty(Coordinate coordinate) {
        return this.getPiece(coordinate) == null;
    }

    @Override
    public String toString() {
        String string = "";
        string += this.toStringHorizontalNumbers();
        for (int i = 0; i < Coordinate.getDimension(); i++)
            string += this.toStringHorizontalPiecesWithNumbers(i);
        string += this.toStringHorizontalNumbers();
        return string;
    }

    private String toStringHorizontalNumbers() {
        String string = " ";
        for (int j = 0; j < Coordinate.getDimension(); j++)
            string += j;
        return string + "\n";
    }

    private String toStringHorizontalPiecesWithNumbers(int row) {
        String string = " " + row;
        for (int j = 0; j < Coordinate.getDimension(); j++) {
            Piece piece = this.getPiece(new Coordinate(row, j));
            if (piece == null)
                string += " ";
            else {
                string += piece;
            }
        }
        return string + row + "\n";
    }

    public void removeRandomBadMovement(List<Coordinate> coordinatesCanEat, List<Coordinate> removedCoordinates) {
        if(removedCoordinates.size() == 0 && coordinatesCanEat.size() > 0 ) {
            int number = new Random().nextInt(coordinatesCanEat.size());
            removedCoordinates.add(0,coordinatesCanEat.get(number));
            this.remove(coordinatesCanEat.get(number));
        }
    }

    public Error isCorrectPairMove(Turn turn, int pair, Coordinate... coordinates) {
        assert coordinates[pair] != null;
        assert coordinates[pair + 1] != null;
        if (this.isEmpty(coordinates[pair]))
            return Error.EMPTY_ORIGIN;
        if (turn.getOppositeColor() == this.getColor(coordinates[pair]))
            return Error.OPPOSITE_PIECE;
        if (!this.isEmpty(coordinates[pair + 1]))
            return Error.NOT_EMPTY_TARGET;
        List<Piece> betweenDiagonalPieces =
            this.getBetweenDiagonalPieces(coordinates[pair], coordinates[pair + 1]);
        return this.getPiece(coordinates[pair]).isCorrectMovement(betweenDiagonalPieces, pair, coordinates);
    }

    public Coordinate getBetweenDiagonalPiece(int pair, Coordinate... coordinates) {
        assert coordinates[pair].isOnDiagonal(coordinates[pair + 1]);
        List<Coordinate> betweenCoordinates = coordinates[pair].getBetweenDiagonalCoordinates(coordinates[pair + 1]);
        if (betweenCoordinates.isEmpty())
            return null;
        for (Coordinate coordinate : betweenCoordinates) {
            if (this.getPiece(coordinate) != null)
                return coordinate;
        }
        return null;
    }

    public void pairMove(List<Coordinate> removedCoordinates, int pair, Coordinate... coordinates) {
        Coordinate forRemoving = this.getBetweenDiagonalPiece(pair, coordinates);
        if (forRemoving != null) {
            removedCoordinates.add(0, forRemoving);
            this.remove(forRemoving);
        }
        this.move(coordinates[pair], coordinates[pair + 1]);
        if (this.getPiece(coordinates[pair + 1]).isLimit(coordinates[pair + 1])) {
            Color color = this.getColor(coordinates[pair + 1]);
            this.remove(coordinates[pair + 1]);
            this.put(coordinates[pair + 1], new Draught(color));
        }
    }

    public Error isCorrectGlobalMove(Error error, List<Coordinate> removedCoordinates, Coordinate... coordinates){
        if (error != null)
            return error;
        if (coordinates.length > 2 && coordinates.length > removedCoordinates.size() + 1)
            return Error.TOO_MUCH_JUMPS;
        return null;
    }

    public List<Coordinate> getCoordinatesWithActualColor(Color color) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < Coordinate.getDimension(); i++) {
            for (int j = 0; j < Coordinate.getDimension(); j++) {
                Coordinate coordinate = new Coordinate(i, j);
                Piece piece = this.getPiece(coordinate);
                if (piece != null && piece.getColor() == color)
                    coordinates.add(coordinate);
            }
        }
        return coordinates;
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
