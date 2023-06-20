package com.proyecpg.hartarte.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.proyecpg.hartarte.ui.screens.main.MainViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SideBar(
    username: String,
    imageURL: String,
    onUserCardClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    saveDarkThemeValue: (Boolean) -> Unit,
    viewModel: MainViewModel,
    switchState: Boolean
){
    val state = viewModel.state

    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .height(100.dp)
                .padding(all = 5.dp)
                .clickable {
                    onUserCardClick()
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = imageURL,
                    contentDescription = username,
                    modifier = Modifier
                        .size(85.dp, 85.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Username",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Divider(color = MaterialTheme.colorScheme.secondary)

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (state.darkThemeValue) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = "Theme icon",
                    modifier = Modifier.padding(start = 16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = if (state.darkThemeValue) "Cambiar al modo claro" else "Cambiar al modo oscuro",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = switchState,
                onCheckedChange = {
                    onCheckedChange(it)
                    saveDarkThemeValue(it)
                },
                modifier = Modifier.padding(end = 25.dp),
                thumbContent = {
                    Icon(
                        modifier = Modifier
                            .size(SwitchDefaults.IconSize),
                        imageVector = if (switchState) Icons.Rounded.DarkMode else Icons.Rounded.LightMode,
                        contentDescription = "Switch Icon"
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }

        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text(text = "Sobre la app") },
            selected = false,
            onClick = { /* TODO: Mostrar ventana de info */ },
            icon = { Icon(imageVector = Icons.Outlined.Info, contentDescription = "About us") }
        )

        Spacer(Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text(text = "Cerrar sesión") },
            selected = false,
            onClick = { /* TODO: Cerrar sesión */ },
            icon = { Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Log out") }
        )
    }
}