package artoria.data.tuple;

import java.io.Serializable;

/**
 * An implementation class for a triple consisting of three elements.
 * @param <L> The left element type
 * @param <M> The middle element type
 * @param <R> The right element type
 * @author Kahle
 */
public class TripleImpl<L, M, R> implements Triple<L, M, R>, Serializable {
    private M middle;
    private R right;
    private L left;

    public TripleImpl(L left, M middle, R right) {
        this.middle = middle;
        this.right = right;
        this.left = left;
    }

    public TripleImpl() {

    }

    @Override
    public L getLeft() {

        return left;
    }

    public void setLeft(L left) {

        this.left = left;
    }

    @Override
    public M getMiddle() {

        return middle;
    }

    public void setMiddle(M middle) {

        this.middle = middle;
    }

    @Override
    public R getRight() {

        return right;
    }

    public void setRight(R right) {

        this.right = right;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (object == null || getClass() != object.getClass()) { return false; }
        TripleImpl<?, ?, ?> triple = (TripleImpl<?, ?, ?>) object;
        if (left != null ? !left.equals(triple.left) : triple.left != null) {
            return false;
        }
        if (middle != null ? !middle.equals(triple.middle) : triple.middle != null) {
            return false;
        }
        return right != null ? right.equals(triple.right) : triple.right == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (middle != null ? middle.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TripleImpl{" +
                "left=" + left +
                ", middle=" + middle +
                ", right=" + right +
                '}';
    }

}
