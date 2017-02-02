import java.io.*
import kotlin.system.*

// activate profiles from -DactivateProfile
val profileToActivate = System.getProperty("activateProfile")
if (profileToActivate == null) {
    System.err.println("No property activateProfile specified, -DactivateProfile=someProfile")
    exitProcess(1)
}

val file = File("../pom.xml")
val pom = file.readText()

val profilesReg = "<profile>.*?(</profile>)".toRegex(RegexOption.DOT_MATCHES_ALL)
val profileId = "<id>(.*?)</id>".toRegex(RegexOption.DOT_MATCHES_ALL)
val activeByDefault = "<activeByDefault>(.*?)</activeByDefault>".toRegex(RegexOption.DOT_MATCHES_ALL)

val profiles = profilesReg.findAll(pom).toList()

if (profiles.isEmpty()) {
    System.err.println("No profiles were found in ${file.absolutePath}")
    exitProcess(1)
}

val result = buildString {
    append(pom, 0, profiles.first().range.start)

    profiles.forEachIndexed { index, profileMatch ->
        val id = profileId.find(profileMatch.value)?.groupValues?.get(1)
        val activated = activeByDefault.find(profileMatch.value)
        val activatedValue = activated?.groups?.get(1)?.range

        if (id != null && activatedValue != null) {
            val newValue = if (id == profileToActivate) "true" else "false"

            append(profileMatch.value, 0, activatedValue.start)
            append(newValue)
            append(profileMatch.value, activatedValue.endInclusive + 1, profileMatch.value.length)
        } else {
            append(profileMatch.value)
        }

        if (index != profiles.lastIndex) {
            append(pom, profileMatch.range.endInclusive + 1, profiles[index + 1].range.start)
        }
    }

    append(pom, profiles.last().range.endInclusive + 1, pom.length)
}

file.writeText(result)


