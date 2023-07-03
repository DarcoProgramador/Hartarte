package com.proyecpg.hartarte.utils

object Constants {
    //App
    const val TAG = "AppTag"

    //Collection References
    const val USERS = "users"

    //User fields
    const val DISPLAY_NAME = "username"
    const val EMAIL = "email"
    const val PHOTO_URL = "photoUrl"
    const val CREATED_AT = "createdAt"
    const val DESCRIPCION = "descripcion"

    //Names
    const val SIGN_IN_REQUEST = "signInRequest"
    const val SIGN_UP_REQUEST = "signUpRequest"

    //FireBasePost
    const val POST_COLLECTION = "posts"
    const val TIME_STAMP = "createdAt"
    const val POST_USER_UID = "user.uid"
    const val PAGE_SIZE = 5L
    const val POST_IMAGES = "images"

    //FireBasePostLikes
    const val POST_LIKES_COLLECTION = "postsLikes"
    const val LIKES = "likes"

    //FireBaseBookmarks
    const val POST_BOOKMARKS_COLLECTION = "postsBookmarks"
    const val BOOKMARKS = "bookmarks"

    //DataStore
    const val DARK_THEME_KEY = "dark_theme_value"

    //Posts
    const val POST_IMAGES_MAX_SIZE = 3

    //Storage Path
    const val POST_PATH = "post/"
}