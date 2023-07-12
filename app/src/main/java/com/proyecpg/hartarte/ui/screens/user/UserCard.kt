package com.proyecpg.hartarte.ui.screens.user

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.screens.user.main.UserEvent
import com.proyecpg.hartarte.ui.screens.user.main.UserState
import com.proyecpg.hartarte.ui.screens.user.main.isScrolled
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.launch

@Composable
fun UserCard(
    userImage: String?,
    username: String?,
    userDescription: String?,
    userEditState: UserState,
    lazyListState: LazyListState,
    onSendDescription: (UserEvent) -> Unit
){
    var name by rememberSaveable(key = "name") { mutableStateOf(username?:"Usuario") }
    var description by rememberSaveable(key = "desc") { mutableStateOf(userDescription?:"¡Hola, soy un nuevo usuario!") }
    var isEditEnabled by rememberSaveable(key = "edit") { mutableStateOf(false) }

    val animatedSize: Dp by animateDpAsState(targetValue = if (!lazyListState.isScrolled) 230.dp else 170.dp)
    val scope = rememberCoroutineScope()
    var selectedImageUri : Uri? by rememberSaveable(key = "photo_edit") { mutableStateOf(null) }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){ uri ->
        if (uri != null){
            selectedImageUri = uri
            onSendDescription(UserEvent.UserEditPhotoClicked(selectedImageUri!!))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (!lazyListState.isScrolled) IntrinsicSize.Max else IntrinsicSize.Min)
            .clickable {
                scope.launch {
                    lazyListState.animateScrollToItem(0, 0)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(if (!lazyListState.isScrolled) 10.dp else 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            AsyncImage(
                model = userImage?: R.drawable.user_placeholder,
                contentDescription = "User image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(animatedSize)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        if (isEditEnabled) {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = username?:"Usuario",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                if(lazyListState.isScrolled && !isEditEnabled){
                    Button(
                        onClick = {
                            isEditEnabled = !isEditEnabled
                        },
                        modifier = Modifier
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit icon"
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = "Editar" )
                    }
                }
            }
        }

        if(!lazyListState.isScrolled || isEditEnabled ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if(isEditEnabled){

                    val descInfo: Triple<String, String, Boolean> = customTextInputField(
                        username = name,
                        description = description,
                        onSendDescription = onSendDescription
                    )
                    name = descInfo.first
                    description = descInfo.second
                    isEditEnabled = descInfo.third

                    onSendDescription(UserEvent.UserOnLoadUser)
                }
                else{
                    Text(
                        text = userDescription?:"¡Hola, soy un nuevo usuario!",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if(!lazyListState.isScrolled && !isEditEnabled){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        isEditEnabled = !isEditEnabled
                    },
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit icon"
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = "Editar" )
                }
            }
        }
    }
}

@Composable
fun NonUserCard(
    userImage: String?,
    username: String?,
    userDescription: String?,
    lazyListState: LazyListState
){
    val animatedSize: Dp by animateDpAsState(targetValue = if (!lazyListState.isScrolled) 230.dp else 170.dp)
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (!lazyListState.isScrolled) IntrinsicSize.Max else IntrinsicSize.Min)
            .clickable {
                scope.launch {
                    lazyListState.animateScrollToItem(0, 0)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(if (!lazyListState.isScrolled) 10.dp else 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = userImage ?: R.drawable.user_placeholder,
                contentDescription = "User image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(animatedSize)
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = username ?: "Usuario",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun customTextInputField(
    username: String,
    description: String,
    onSendDescription: (UserEvent) -> Unit
): Triple<String, String, Boolean> {

    var userEditText by remember { mutableStateOf(username) }
    var descriptionEditText by remember { (mutableStateOf(description)) }
    var editEnabled by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            value = userEditText,
            onValueChange = {
                if (it.length <= 35){
                    userEditText = it
                }
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp
            ),
            placeholder = {
                Text(
                    text = "Escribe un nombre..."
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            value = descriptionEditText,
            onValueChange = {
                if (it.length <= 250){
                    descriptionEditText = it
                }
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp
            ),
            placeholder = {
                Text(
                    text = "Escribe una descripción..."
                )
            },
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = descriptionEditText.length.toString() + "/250"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            maxLines = 3
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    editEnabled = !editEnabled
                },
                modifier = Modifier
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close icon"
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "Cerrar")
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    onSendDescription(
                        UserEvent.UserEditClicked(
                        username = userEditText,
                        description = descriptionEditText
                    ))
                    editEnabled = !editEnabled
                },
                modifier = Modifier
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send icon"
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = "Enviar" )
            }
        }
    }

    return Triple(userEditText, descriptionEditText, editEnabled)
}

@Preview
@Composable
fun PreviewUserCard(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            UserCard(
                userImage = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                username = "Username",
                userDescription = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                lazyListState = LazyListState(6, 6),
                onSendDescription = {},
                userEditState = UserState()
            )
        }
    }
}

@Preview
@Composable
fun PreviewNonUserCard(){
    HartarteTheme {
        Box(modifier = Modifier.padding(all = 10.dp)){
            NonUserCard(
                userImage = "https://cdn.discordapp.com/attachments/1029844385237569616/1116569644745097320/393368.png",
                username = "Username",
                userDescription = "Esta descripción tiene activado un ellipsis y un límite de 3 líneas para la descripción con el fin de que no se vea muy largo todo.",
                lazyListState = LazyListState(6, 6)
            )
        }
    }
}