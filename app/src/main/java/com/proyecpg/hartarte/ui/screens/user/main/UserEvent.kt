package com.proyecpg.hartarte.ui.screens.user.main

import android.net.Uri

sealed class UserEvent {
    class UserEditClicked(val username : String, val description : String) : UserEvent()

    class UserEditPhotoClicked(val photo : Uri) : UserEvent()

    object UserOnLoadUser : UserEvent()
}