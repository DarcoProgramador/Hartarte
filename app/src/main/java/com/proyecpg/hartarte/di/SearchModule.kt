package com.proyecpg.hartarte.di

import android.app.Application
import com.algolia.search.client.ClientSearch
import com.algolia.search.logging.LogLevel
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.proyecpg.hartarte.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class SearchModule {

    @Provides
    fun provideSearchClient(app: Application): ClientSearch = ClientSearch(
        ApplicationID(app.getString(R.string.web_client_search_aplication_id)),
        APIKey(app.getString(R.string.web_client_search_apikey)),
        LogLevel.All
    )

    @Provides
    fun provideIndexName(app: Application) : IndexName = IndexName(app.getString(R.string.web_client_search_index_name))
}