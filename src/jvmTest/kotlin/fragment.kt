package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.stream.*
import org.junit.Test
import kotlin.test.*

class FinalizeCounter<T>(val delegate: TagConsumer<T>) : TagConsumer<T> by delegate {
    var count = 0

    override fun finalize(): T {
        count++
        return delegate.finalize()
    }
}

class FragmentTest {
    @Test
    fun testFragment() {
        val finalizeCounter = FinalizeCounter(createHTML(false))
        val html = finalizeCounter.fragment {
            p {
                +"One"
            }
            p {
                +"Two"
            }
            p {
                +"Three"
            }
        }

        assertEquals("<p>One</p><p>Two</p><p>Three</p>", html)
        assertEquals(1, finalizeCounter.count)
    }
}
