package kotlinx.html.js

import org.gradle.api.*

private const val GITHUB_ORGANIZATION = "Kotlin"
private const val GITHUB_REPOSITORY = "kotlin.html"

private const val GIT_URL = "git+https://github.com/$GITHUB_ORGANIZATION/$GITHUB_REPOSITORY.git"
private const val GITHUB_BUGS_URL = "https://github.com/$GITHUB_ORGANIZATION/$GITHUB_REPOSITORY/issues"
private const val GITHUB_WIKI_URL = "https://github.com/$GITHUB_ORGANIZATION/$GITHUB_REPOSITORY/wiki"

fun Project.packageJson(version: String, organization: String? = null): String = """{
  "name": "${organization?.let { "@$it/" } ?: ""}${project.name}",
  "version": "$version",
  "description": "Library for building HTML in Kotlin",
  "main": "${project.name}-js.js",
  "repository": {
    "type": "git",
    "url": "$GIT_URL"
  },
  "keywords": [
    "Kotlin",
    "Language",
    "HTML",
    "JavaScript",
    "JetBrains"
  ],
  "author": "JetBrains",
  "license": "Apache-2.0",
  "bugs": {
    "url": "$GITHUB_BUGS_URL"
  },
  "homepage": "$GITHUB_WIKI_URL"
}
"""