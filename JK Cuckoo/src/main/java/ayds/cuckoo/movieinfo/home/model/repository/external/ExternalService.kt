package ayds.cuckoo.movieinfo.home.model.repository.external

import ayds.cuckoo.movieinfo.home.model.entities.OmdbMovie

interface ExternalService {

    fun getMovie(title: String): OmdbMovie
}