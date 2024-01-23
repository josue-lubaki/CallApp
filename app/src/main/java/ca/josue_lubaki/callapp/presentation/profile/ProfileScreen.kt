package ca.josue_lubaki.callapp.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

/**
 * created by Josue Lubaki
 * date : 2024-01-22
 * version : 1.0.0
 */

@Composable
fun ProfileScreen(
    onNavigateToRoute: (String) -> Unit,
) {

    Content(
        onNavigateToRoute = onNavigateToRoute
    )
}

@Composable
private fun Content(
    onNavigateToRoute: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.headlineMedium
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun ProfilePreview() {
    Content(
        onNavigateToRoute = {}
    )
}