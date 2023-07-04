package com.proyecpg.hartarte.ui.screens.user

import android.net.Uri

sealed class UserEvent {
    class UserEditClicked(val username : String, val descipcion : String) : UserEvent()

    class UserEditPhotoClicked(val photo : Uri) : UserEvent()

    object UserOnLoadUser : UserEvent()

    class  UserPostLikeClicked(val postId : String, val Liked : Boolean ): UserEvent()

    class  UserPostBookmarkCliked(val postId : String, val Bookmarked : Boolean ): UserEvent()
}