package artoria.operations;

import artoria.lang.Operations;

import java.util.List;

/**
 * The search operations.
 * @param <E> The type of simple entity
 * @param <I> The type of input
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public interface SearchOperations<E, I> extends Operations {

    /**
     * Search the list of records based on entered criteria.
     * @param input The query condition
     * @return The simple record list
     */
    List<E> search(I input);

}
