/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.data;

import java.io.Serializable;

/**
 * An implementation class for a pair consisting of two elements.
 * @param <L> The left element type
 * @param <R> The right element type
 * @author Kahle
 */
@Deprecated
public class PairImpl<L, R> implements Pair<L, R>, Serializable {
    private R right;
    private L left;

    public PairImpl(L left, R right) {
        this.right = right;
        this.left = left;
    }

    public PairImpl() {

    }

    @Override
    public L getLeft() {

        return left;
    }

    public void setLeft(L left) {

        this.left = left;
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
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PairImpl<?, ?> pair = (PairImpl<?, ?>) object;
        if (left != null ? !left.equals(pair.left) : pair.left != null) {
            return false;
        }
        return right != null ? right.equals(pair.right) : pair.right == null;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PairImpl{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

}
