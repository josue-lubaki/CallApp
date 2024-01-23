package ca.josue_lubaki.callapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

sealed class BottomItem(
    val label: @Composable() (() -> Unit),
    val icon: @Composable () -> Unit,
    val route : String
) {
    data object Home : BottomItem(
        {
            Text(text = "Home")
        },
        {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Home"
            )
        },
        route = "home"
    )

    data object Call : BottomItem(
        {
            Text(text = "Call")
        },
        {
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = "Call"
            )
        },
        route = "call"
    )

    data object Chat : BottomItem(
        {
            Text(text = "Chat")
        },
        {
            Icon(
                imageVector = Icons.Filled.Message,
                contentDescription = "Chat"
            )
        },
        route = "chat"
    )

    data object Profile : BottomItem(
        {
            Text(text = "Profile")
        }, {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile"
            )
        },
        route = "profile"
    )
}
