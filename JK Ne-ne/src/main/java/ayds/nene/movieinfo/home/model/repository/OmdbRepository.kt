package ayds.nene.movieinfo.home.model.repository

import ayds.nene.movieinfo.home.model.entities.OmdbMovie

interface OmdbRepository {
    fun getMovie(title: String): OmdbMovie?
}