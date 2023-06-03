package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.utils.isInteger

@Composable
fun EventDialog(
    errorMessage: String,
    onDismiss: (() -> Unit)? = null,
    showDialog: Boolean
) {
    if(showDialog){
        val errorText: String = if(isInteger(errorMessage)) LocalContext.current.getString(errorMessage.toInt()) else  errorMessage
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.error,
            onDismissRequest = { onDismiss?.invoke() },
            title = {
                Text(
                    stringResource(R.string.error),
                    modifier = Modifier.padding(start = 10.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = errorText,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = 16.sp
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = { onDismiss?.invoke() }) {
                    Text(
                        text = stringResource(id = R.string.accept),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onError,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        )
    }
}