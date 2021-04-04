# Spotify playlist finder

Simple, ugly app to find playlists on Spotify where I can submit my music.

## Which playlists are open to submissions?
Let's take a look at description for specific keywords:

```kotlin
val keywords = listOf("@", "submit", "submission", "http", "https", "curated", "instagram")
```

- `@` may indicate email or Instagram handle
- http or https may indicate curator website with submission form
- other keywords are self-explanatory

Another indicator: playlist owner has public email.

## How to use it?

- Clone repo and build Gradle project.
- Set environmental variables for SPOTIFY_CLIENT_ID and SPOTIFY_CLIENT_SECRET. (or put them directly in the code)
> How to do that? Check Spotify dev website: https://developer.spotify.com/documentation/general/guides/app-settings/
- Write up yours search phrase in main function: `searchAndWriteToCsv("lofi guitar chill")`. I guess that search phrase could be passed from main function args, but I run this project from IDE and that is sufficient.
- Run main function.

App should create csv file with the following name structure:
`{timestamp}_{search_phrase}.csv`

There is high probability that you won't find many playlists meeting the criteria. Feel free to try out various search phrases, or modify filtering criteria.

## Dependencies
- https://github.com/rozkminiacz/spotify-web-api-java (fork of https://github.com/thelinmichael/spotify-web-api-java) with one field added to data model
- Kotlin JVM, v1.4.10

And last but not least, besides programming I sometimes produce music. You can listen to my stuff here:
https://open.spotify.com/artist/4H890UQXWdvbSsOijxYSpE
