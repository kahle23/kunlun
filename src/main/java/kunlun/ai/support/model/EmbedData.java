/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package kunlun.ai.support.model;

import java.io.Serializable;
import java.util.List;

/**
 * The embeddings' data.
 * @author Kahle
 */
public class EmbedData implements Serializable {
    /**
     * The index of the embedding in the list of embeddings.
     */
    private Integer index;
    /**
     * The embedding vector, which is a list of floats.
     */
    private List<? extends Number> embedding;

    public EmbedData(Integer index, List<? extends Number> embedding) {
        this.embedding = embedding;
        this.index = index;
    }

    public EmbedData(List<? extends Number> embedding) {

        this.embedding = embedding;
    }

    public EmbedData() {

    }

    public Integer getIndex() {

        return index;
    }

    public void setIndex(Integer index) {

        this.index = index;
    }

    public List<? extends Number> getEmbedding() {

        return embedding;
    }

    public void setEmbedding(List<? extends Number> embedding) {

        this.embedding = embedding;
    }

}
