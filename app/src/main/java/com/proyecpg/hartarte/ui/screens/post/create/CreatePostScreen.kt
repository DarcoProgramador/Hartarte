package com.proyecpg.hartarte.ui.screens.post.create

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.proyecpg.hartarte.R
import com.proyecpg.hartarte.ui.theme.HartarteTheme
import com.proyecpg.hartarte.utils.Constants.POST_IMAGES_MAX_SIZE

@Composable
fun CreatePostScreen(
    onReturn: () -> Unit
){
    //There'll be all the post info that the user provides
    var postInfo: Triple<List<Uri>, String, String>

    Scaffold(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp),
        topBar = {
            CreatePostTopAppBar(
                onClick = onReturn
            )
        },
        floatingActionButton = {
            Button(
                onClick = {
                    /* TODO: Use postInfo data */
                },
                enabled = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(67.dp)
                    .fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Crear publicación",
                    fontSize = 20.sp
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        postInfo = createPostScreenContent(innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostTopAppBar(
    onClick: () -> Unit
){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Crear publicación",
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Return icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun createPostScreenContent(
    paddingValues: PaddingValues
): Triple<List<Uri>, String, String> {

    var title by remember{ mutableStateOf("") }
    var description by remember{ mutableStateOf("") }
    val pagerState = rememberPagerState(initialPage = 0)

    //Images from gallery
    var selectedImageUris by rememberSaveable(key = "photos") { mutableStateOf<List<Uri>>(emptyList()) }
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(3),
        onResult = { uris -> selectedImageUris = uris }
    )

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ){
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                HorizontalPager(
                    count = selectedImageUris.size.coerceAtLeast(1),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable {
                            if (selectedImageUris.size < POST_IMAGES_MAX_SIZE) {
                                multiplePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Límite de imágenes alcanzado",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        },
                    state = pagerState,
                    verticalAlignment = Alignment.CenterVertically
                ) { page ->

                    val imageUri = selectedImageUris.getOrNull(page)

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUri)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .crossfade(true)
                            .scale(Scale.FILL)
                            .build(),
                        contentDescription = "Carousel image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Delete image icon
                    if (selectedImageUris.isNotEmpty()){
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopEnd
                        ){
                            IconButton(onClick = {
                                selectedImageUris = selectedImageUris.toMutableList().apply {
                                    removeAt(page)
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Delete image")
                            }
                        }
                    }
                }
            }

            //Current image
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(selectedImageUris.size){
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(shape = CircleShape)
                            .size(5.dp)
                            .background(if (pagerState.currentPage == it) Color.DarkGray else Color.LightGray)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Seleccione hasta tres imágenes",
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            title = customTextInputField(
                placeholder = "Descipción de la publicación",
                height = 56,
                maxLength = 50,
                maxLines = 1,
                focusManager = focusManager,
                isLastTextField = false
            )

            Spacer(modifier = Modifier.height(15.dp))

            description = customTextInputField(
                placeholder = "Descipción de la publicación",
                height = 280,
                maxLength = 2000,
                maxLines = null,
                focusManager = focusManager,
                isLastTextField = true
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
    }

    return Triple(selectedImageUris, title, description)
}

@Composable
fun customTextInputField(
    placeholder: String,
    height: Int,
    maxLength: Int,
    maxLines: Int?,
    focusManager: FocusManager,
    isLastTextField: Boolean
): String {

    var text by remember { (mutableStateOf("")) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        value = text,
        onValueChange = {
            if (it.length <= maxLength){
                text = it
            }
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 16.sp
        ),
        placeholder = {
            Text(
                text = placeholder
            )
        },
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = text.length.toString() + "/" + maxLength.toString()
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if(isLastTextField) ImeAction.Default else ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { if (text.isNotEmpty()) focusManager.moveFocus(FocusDirection.Down) }
        ),
        maxLines = maxLines ?: 50
    )

    return text
}

@Preview(showBackground  = true)
@Composable
fun PreviewCreatePostScreen(){
    HartarteTheme {
        Box(modifier = Modifier.fillMaxSize()){
            CreatePostScreen(
                onReturn = {}
            )
        }
    }
}
