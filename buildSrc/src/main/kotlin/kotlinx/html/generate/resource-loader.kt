package kotlinx.html.generate

import java.net.*

private class ResourceLoader

internal fun String.asResourceUrl(): URL = ResourceLoader::class.java.classLoader.getResource(this)
        ?: throw IllegalArgumentException("Resource $this not found in classpath.")