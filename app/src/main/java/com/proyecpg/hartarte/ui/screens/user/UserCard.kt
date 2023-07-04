package com.proyecpg.hartarte.ui.screens.user

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import kotlinx.coroutines.launch

@Composable
fun UserCard(
    userImage: String?,
    username: String,
    userDescription: String?,
    userEditState: UserState,
    lazyListState: LazyListState,
    onSendDescription: (UserEvent) -> Unit
){
    val animatedSize: Dp by animateDpAsState(targetValue = if (!lazyListState.isScrolled) 230.dp else 170.dp)
    val scope = rememberCoroutineScope()


    var description by rememberSaveable(key = "desc") { mutableStateOf(userDescription?:"¡Hola, soy un nuevo usuario!") }
    var isEditEnabled by rememberSaveable(key = "edit") { mutableStateOf(false) }

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
                modifier = Modifier
                    .size(animatedSize)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(30.dp))

                if(lazyListState.isScrolled){
                    Button(
                        onClick = {
                            isEditEnabled = !isEditEnabled
                        },
                        modifier = Modifier
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if(isEditEnabled) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = "Edit icon"
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(text = if(isEditEnabled) "Cerrar" else "Editar" )
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
                    val descInfo: Pair<String, Boolean> = customTextInputField(
                        description = description,
                        onSendDescription = onSendDescription,
                        username = username
                    )
                    description = descInfo.first
                    isEditEnabled = descInfo.second
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

        if(!lazyListState.isScrolled){
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
                        imageVector = if(isEditEnabled) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = "Edit icon"
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(text = if(isEditEnabled) "Cerrar" else "Editar" )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun customTextInputField(
    description: String?,
    username: String,
    onSendDescription: (UserEvent) -> Unit
): Pair<String, Boolean> {

    var desciptionEditText by remember { (mutableStateOf(description?:"")) }
    var editEnabled by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
        value = desciptionEditText,
        onValueChange = {
            if (it.length <= 250){
                desciptionEditText = it
            }
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = "Escribe una descipción..."
            )
        },
        trailingIcon = {
            if (desciptionEditText.isNotEmpty()){
                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onSendDescription(UserEvent.UserEditClicked(
                            descipcion = desciptionEditText,
                            username = username
                        ))
                        editEnabled = !editEnabled
                    }
                ){
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send description",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = desciptionEditText.length.toString() + "/250"
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

    return Pair(desciptionEditText, editEnabled)
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