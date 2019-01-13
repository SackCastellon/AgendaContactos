package agenda.data.dao

import agenda.model.Data

interface Dao<T : Data<T>> : MutableIterable<T> {
    // Query Operations
    /**
     * Returns the number of elements stored.
     */
    val size: Int

    /**
     * Returns `true` if no elements are stored, `false` otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * Returns `true` if there is a stored element with the specified [id].
     */
    operator fun contains(id: Int): Boolean

    /**
     * Returns `true` if the specified [element] is stored.
     */
    operator fun contains(element: T): Boolean

    /**
     * Returns the element corresponding to the given [id], or `null` if such a id is not present.
     */
    operator fun get(id: Int): T?

    // Modification Operations
    /**
     * Stores the specified [element].
     *
     * @return `true` if the element has been stored, `false` if the element is already contained in the collection.
     */
    fun add(element: T): Boolean

    /**
     * Removes the element corresponding tho the given [id].
     *
     * @return `true` if the element has been successfully removed; `false` if it was not present.
     */
    fun remove(id: Int): Boolean
}
