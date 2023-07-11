package com.proyecpg.hartarte.data.model

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.proyecpg.hartarte.utils.Constants.TITLE_FIELD
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class PostSerial(
    val titulo: String,
    val descripcion : String,
    val likes : Int,
    val bookmarks : Int,
    val createdAt : Long,
    val user : UserSerialize,
    val images : ArrayList<String>,
    override val _highlightResult: JsonObject?,
    override val objectID: ObjectID
) : Highlightable, Indexable{

    val highlightedName: HighlightedString?
        get() = getHighlight(Attribute(TITLE_FIELD))
}

@Serializable
data class UserSerialize(
    val uid : String,
    val name :String,
    val photo : String
)