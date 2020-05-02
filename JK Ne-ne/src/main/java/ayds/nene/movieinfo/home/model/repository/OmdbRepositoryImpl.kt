package ayds.nene.movieinfo.home.model.repository

import ayds.nene.movieinfo.home.model.entities.OmdbMovie
import ayds.nene.movieinfo.home.model.repository.external.ExternalService
import ayds.nene.movieinfo.home.model.repository.local.LocalStorage

class OmdbRepositoryImpl(
    private val localStorage: LocalStorage,
    private val externalService: ExternalService
) : OmdbRepository {

    override fun getMovie(title: String): OmdbMovie? {
        var movie = localStorage.getMovie(title)

        when {
            movie != null -> markMovieAsLocal(movie)
            else -> {
                try {
                    movie = externalService.getMovie(title)
                    localStorage.saveMovie(title, movie)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return movie
    }

    private fun markMovieAsLocal(movie: OmdbMovie) {
        movie.isLocallyStoraged = true
    }
}