package agenda.model

import kotlin.random.Random

interface Data<T : Any> {
    val id: Int

    interface New<T : Any> : Data<T>
    interface Existing<T : Any> : Data<T>

    companion object {
        fun checkId(id: Int): Unit = check(id >= 0) { "Data ID must be positive, but was: $id" }
    }
}

/** Checks whether this [Data] is an instance of [Data.New] */
inline val <T : Any> Data<T>.isNew: Boolean
    get() = this is Data.New<T>

/** Returns a random negative integer. */
inline val newId: Int
    get() = Random.nextBits(Int.SIZE_BITS - 1) or Int.MIN_VALUE
