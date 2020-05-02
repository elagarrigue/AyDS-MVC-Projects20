package ayds.nene.movieinfo.home.model.repository.external.omdb

import ayds.nene.movieinfo.home.model.entities.OmdbMovie
import ayds.nene.movieinfo.home.model.repository.external.ExternalService
import retrofit2.Response

internal class OmdbService(
    private val omdbAPI: OmdbAPI,
    private val omdbMovieResolver: OmdbResponseToOmdbMovieResolver
) : ExternalService {

    override fun getMovie(title: String): OmdbMovie {
        val callResponse = getOmdbMovieFromService(title)
        return omdbMovieResolver.getMovieFromExternalData(callResponse.body())
    }

    private fun getOmdbMovieFromService(title: String): Response<String> {
        return omdbAPI.getTerm(title).execute()
    }

}