package nl.han.ica.examplatform.models.answer

/**
 * This class represents a list of Keywords witch ar strings.
 */
class Keywords(private val keywords: Array<String>) : Collection<String> {
    override val size: Int
        get() = keywords.size

    init {
        for (keyword in keywords) {
            require(!keyword.contains(' ')) { "Keyword must be one word" }
        }
    }

    override fun contains(element: String): Boolean {
        return keywords.contains(element)
    }

    override fun containsAll(elements: Collection<String>): Boolean {
        for (element in elements) {
            if (!keywords.contains(element)) {
                return false
            }
        }
        return true
    }

    override fun isEmpty(): Boolean {
        return keywords.isEmpty()
    }

    override fun iterator(): Iterator<String> {
        return keywords.iterator()
    }
}