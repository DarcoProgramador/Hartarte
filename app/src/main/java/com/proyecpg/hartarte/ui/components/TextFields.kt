package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.proyecpg.hartarte.R

@Composable
fun customTextField(
    placeholder: String,
    focusManager: FocusManager
): String {

    var text by remember { (mutableStateOf("")) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        value = text,
        onValueChange = { if (it != " ") text = it },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        placeholder = {
            Text(
                text = placeholder
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        maxLines = 1
    )

    return text
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun customPasswordField(
    placeholder: String,
    focusManager: FocusManager,
    isLastField: Boolean
): String {

    var password by remember { (mutableStateOf("")) }
    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        value = password,
        onValueChange = { if (it != " ") password = it },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        placeholder = {
            Text(
                text = placeholder
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                painterResource(id = R.drawable.ic_visibility_on)
            else painterResource(id = R.drawable.ic_visibility_off)

            val description = if (passwordVisible)
                stringResource(id = R.string.hide_password)
            else stringResource(id = R.string.show_password)

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(painter = image, description)
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        maxLines = 1
    )

    return password
}