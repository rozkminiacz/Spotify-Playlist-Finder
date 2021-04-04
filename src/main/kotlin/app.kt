import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified
import java.io.File
import java.lang.System.getenv

val api by lazy {
    val clientId: String? = getenv("SPOTIFY_CLIENT_ID")
    val clientSecret: String? = getenv("SPOTIFY_CLIENT_SECRET")

    require(!clientId.isNullOrBlank() && !clientSecret.isNullOrBlank()) {
        "Client id and client secret are required!"
    }

    val apis = SpotifyApi.builder()
        .setClientId(clientId)
        .setClientSecret(clientSecret)
        .build()

    apis.clientCredentials().build().execute()
        .also {
            apis.accessToken = it.accessToken
        }
    apis
}

fun main() {
    try {
        searchAndWriteToCsv("lofi guitar chill")
    } catch (exception: Exception) {
        println(exception)
    }
}

fun searchAndWriteToCsv(searchPhrase: String) {
    val playlists = findPlaylists(searchPhrase) {
        submissionsCriteria(it)
    }

    if (playlists.isNotEmpty()) {

        val file =
            File("${System.currentTimeMillis()}_${searchPhrase.replace(".", "_").replace(" ", "_").trimIndent()}.csv")
        val header = playlists[0].keys
        file.writeText(
            text = """${header.joinToString("\t")}
            |${
                playlists
                    .joinToString(separator = "\n") { it.values.joinToString("\t") }
            }
        """.trimMargin()
        )
    } else {
        println("No results for $searchPhrase matching criteria")
    }
}

private fun findPlaylists(searchQuery: String, criteria: (PlaylistSimplified) -> Boolean): List<Map<String, Any>> {

    val searchResult = api.searchItem(searchQuery, "playlist").build()
        .execute()

    return searchResult
        .playlists
        .also {
            println("Found ${it.total}, limit ${it.limit}")
        }.items
        .filter(criteria)
        .map { playlistSimplified ->
            mapOf(
                "id" to playlistSimplified.id,
                "url" to playlistSimplified.externalUrls.externalUrls["spotify"].toString().trimIndent(),
                "name" to playlistSimplified.name.trimIndent(),
                "description" to playlistSimplified.description.trimIndent(),
                "email" to playlistSimplified.owner.email
            ).onEach(::println)
        }
}

private fun submissionsCriteria(it: PlaylistSimplified): Boolean {
    val description = it.description
    val owner = it.owner
    val keywords = listOf("@", "submit", "submission", "http", "https", "curated", "instagram")
    return keywords.any { keyword ->
        description.contains(keyword)
    } || owner.email != null
}
