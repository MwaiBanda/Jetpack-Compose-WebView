package com.mwaibanda.lesson08

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mwaibanda.lesson08.ui.theme.Lesson08Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.core.app.NotificationCompat
import android.media.RingtoneManager
import android.net.Uri
import android.app.PendingIntent
import android.app.NotificationManager
import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lesson08Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    var showMenu by remember { mutableStateOf(false) }
    var showWebContent by remember { mutableStateOf(false) }
    var url by remember { mutableStateOf( TextFieldValue() ) }
    val CHANNEL_ID = "my_channnel_02"
    val context = LocalContext.current
    val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Android ATC Notificcation")
        .setContentText("Check Android ATC New Course")

    val sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    mBuilder.setSound(sound)
    Column {
        TopAppBar(
            title = {Text("Lesson-08", color = Color.White)},
            actions = {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_vertical_dots), contentDescription = "")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(onClick = {
                            val resultIntent = Intent(context, MainActivity::class.java)
                            val resultPendingIntent =
                                PendingIntent.getActivity(context, 0, resultIntent, 0)
                            mBuilder.setContentIntent(resultPendingIntent)

                            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                            mNotificationManager.notify(R.string.ID, mBuilder.build())
                        }) {
                            Text(text = "Courses Info")
                        }
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(text = "Exams Info")
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Spacer(modifier = Modifier.width(17.dp))
            TextField(value = url, onValueChange = { url = it })
            Spacer(modifier = Modifier.width(17.dp))
            Button(onClick = { showWebContent = true }, Modifier.size(width = 75.dp, height = 55.dp)) {
                Text(text = "GO")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (showWebContent)
            WebViewContent("https://" + url.text)
        else
            Column(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Text(text = "Enter URL to Redirect", color = Color.Gray, fontSize = 35.sp)
            }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(urlToRender: String) {
    AndroidView(factory = {
        WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = WebViewClient()
            loadUrl(urlToRender)
        }
    }, update = {
        it.loadUrl(urlToRender)
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lesson08Theme {
        MainContent()
    }
}

sealed class MenuAction(val label: String, @DrawableRes val icon: Int) {
    object Dropdown : MenuAction("Menu", R.drawable.ic_vertical_dots)
}