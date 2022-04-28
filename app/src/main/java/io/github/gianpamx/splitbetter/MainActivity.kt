package io.github.gianpamx.splitbetter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.gianpamx.splitbetter.app.AppAction.DisplayError
import io.github.gianpamx.splitbetter.app.AppActionFlow
import io.github.gianpamx.splitbetter.home.HomeScreen
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var appActions: AppActionFlow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setErrorHandler()

        setContent {
            MainContent()
        }
    }

    private fun setErrorHandler() {
        appActions
            .filterIsInstance<DisplayError>()
            .map { it.throwable }
            .onEach(Timber::e)
            .launchIn(lifecycleScope)
    }
}

@Composable
fun MainContent() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = useDarkIcons)
    }

    MdcTheme {
        ProvideWindowInsets {
            HomeScreen()
        }
    }
}

@Preview
@Composable
fun MainContentPreview() {
    MainContent()
}
