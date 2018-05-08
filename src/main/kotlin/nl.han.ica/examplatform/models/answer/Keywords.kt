package nl.han.ica.examplatform.models.answer

/**
 * This class represents a list of Keywords which are strings.
 *
 * @constructor [keywords] The keywords for different [Answer]s
 */
class Keywords(private val keywords: ArrayList<String>) : Collection<String> {
    override val size: Int
        get() = keywords.size

    init {
        for (keyword in keywords) {
            require(!keyword.contains(' ')) { "Keyword must be one word" }
        }
    }

    /**
     * This method checks if keywords contains a certain element
     *
     * @param [element] The element you want to find in [keywords]
     */
    override fun contains(element: String): Boolean {
        return keywords.contains(element)
    }

    /**
     * This method checks if keywords contains a certain collection of elements
     *
     * @param elements The [elements] you want to find in [keywords]
     */
    override fun containsAll(elements: Collection<String>): Boolean {
        for (element in elements) {
            if (!keywords.contains(element)) {
                return false
            }
        }
        return true
    }

    /**
     * Checks if [keywords] is empty
     */
    override fun isEmpty(): Boolean {
        return keywords.isEmpty()
    }

    /**
     * Used to retrieve an iterator for [keywords]
     */
    override fun iterator(): Iterator<String> {
        return keywords.iterator()
    }

    /**
     * Overrides the standard toString method
     */
    override fun toString(): String {
        val sb = StringBuilder()
        for (string in keywords) {
            sb.append(string)
            sb.append(", ")
        }
        return sb.toString()
    }
}