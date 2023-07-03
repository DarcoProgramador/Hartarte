package com.proyecpg.hartarte.utils

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.proyecpg.hartarte.utils.Constants.POST_USER_UID


sealed class QueryParams(val query: Query){
    object MOST_RECENT: QueryParams(
        Firebase.firestore
        .collection(Constants.POST_COLLECTION)
        .orderBy(Constants.TIME_STAMP, Query.Direction.DESCENDING)
        .limit(Constants.PAGE_SIZE)
    )
    object MOST_LIKED: QueryParams(Firebase.firestore
        .collection(Constants.POST_COLLECTION)
        .orderBy(Constants.LIKES, Query.Direction.DESCENDING)
        .limit(Constants.PAGE_SIZE)
    )
    object MOST_BOOKMARKED: QueryParams(Firebase.firestore
        .collection(Constants.POST_COLLECTION)
        .orderBy(Constants.BOOKMARKS, Query.Direction.DESCENDING)
        .limit(Constants.PAGE_SIZE)
    )

    class USER_POST(uid : String) : QueryParams(
        Firebase.firestore
            .collection(Constants.POST_COLLECTION)
            .whereEqualTo(POST_USER_UID, uid)
            .orderBy(Constants.TIME_STAMP, Query.Direction.DESCENDING)
            .limit(Constants.PAGE_SIZE)
    )

    class QUERY_SEARCH(query: Query) : QueryParams(query)

}