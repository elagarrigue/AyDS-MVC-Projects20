package ayds.cuckoo.movieinfo.home.model.repository

import ayds.cuckoo.movieinfo.home.model.entities.OmdbMovie

interface OmdbRepository {
    fun getMovie(title: String): OmdbMovie?
}