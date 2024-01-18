package kotlinx.html.generate

fun tagUnions(repository: Repository) {
    val groupings = repository.groupsByTags.filter { it.value.size > 1 }
    val groups = groupings.values.map { it.map { it.name }.toHashSet() }.distinct().sortedByDescending { it.size }
    val allUnions = repository.groupUnions

    // initial pass
    groups.forEach { group ->
        val name = unionName(group)
        val superGroups = groups.filter { it !== group && it.containsAll(group) }
        val members = group.toList()
        val intersection = members.map { repository.tagGroups[it]!!.tags.toSet() }.reduce { a, b -> a.intersect(b) }

        val union = GroupUnion(members, intersection, emptyList(), emptyList(), superGroups.map(::unionName))
        require(union.name == name)

        allUnions[name] = union
    }

    // pass2: fill additionalTags and ambiguityTags
    groups.forEach { groupMembers ->
        val unionName = unionName(groupMembers)
        val union = allUnions[unionName]!!

        val additionalTags = union.intersectionTags.filter { tag -> union.superGroups.none { tag in allUnions[it]!!.intersectionTags } }
        val ambiguityTags = union.intersectionTags.filter { tag -> union.superGroups.count { tag in allUnions[it]!!.intersectionTags } > 1 }

        allUnions[unionName] = union.copy(additionalTags = additionalTags, ambiguityTags = ambiguityTags)
    }

    // transpose map
    val unionsByGroups = allUnions.values.flatMap { u -> u.members.map { it to u.name } }.groupBy({ it.first }, { allUnions[it.second]!! })
    repository.unionsByGroups = unionsByGroups
}

fun unionName(members: Iterable<String>) = humanizeJoin(members, "Or")
