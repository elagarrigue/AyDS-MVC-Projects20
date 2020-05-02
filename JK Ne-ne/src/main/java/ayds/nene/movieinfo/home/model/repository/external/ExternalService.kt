package ayds.nene.movieinfo.home.model.repository.external

import ayds.nene.movieinfo.home.model.entities.OmdbMovie

interface ExternalService {

    fun getMovie(title: String): OmdbMovie
}