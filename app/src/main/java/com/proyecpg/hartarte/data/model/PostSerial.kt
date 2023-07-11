package com.proyecpg.hartarte.data.model

import com.algolia.instantsearch.core.highlighting.HighlightedString
import com.algolia.instantsearch.highlighting.Highlightable
import com.algolia.search.model.Attribute
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Product(
    val titulo: String,
    val descripcion : String,
    val likes : Int,
    val bookmarks : Int,
    val createdAt : Long,
    val user : UserSerialize,
    val images : ArrayList<String>,
    override val _highlightResult: JsonObject?
) : Highlightable {

    val highlightedName: HighlightedString?
        get() = getHighlight(Attribute("name"))
}

@Serializable
data class UserSerialize(
    val uid : String,
    val name :String,
    val photo : String
)