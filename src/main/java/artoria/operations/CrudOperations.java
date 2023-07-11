package artoria.operations;

import artoria.lang.Operations;

import java.io.Serializable;
import java.util.List;

/**
 * The crud operations.
 * @param <E> The type of entity
 * @param <I> The type of input
 * @see <a href="https://en.wikipedia.org/wiki/Create,_read,_update_and_delete">Create, read, update and delete</a>
 * @author Kahle
 */
@Deprecated // TODO: 2022/11/19 Deletable
public interface CrudOperations<E, I> extends Operations {

    /**
     * Add a record from the input.
     * @param input The input content
     */
    void add(I input);

    /**
     * Edit a record from the input.
     * @param input The input content
     */
    void edit(I input);

    /**
     * Delete a record by id.
     * @param id The id of record
     */
    void delete(Serializable id);

    /**
     * Query record information by record id.
     * @param id The id of record
     * @return The record information
     */
    E info(Serializable id);

    /**
     * Query the record list based on the input.
     * @param input The input content
     * @return The record list
     */
    List<E> list(I input);

}
