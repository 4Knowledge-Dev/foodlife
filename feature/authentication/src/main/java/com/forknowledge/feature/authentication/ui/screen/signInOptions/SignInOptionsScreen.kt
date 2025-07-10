package com.forknowledge.feature.authentication.ui.screen.signInOptions

import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.forknowledge.core.data.datatype.LoginResultType
import com.forknowledge.core.ui.R.drawable
import com.forknowledge.core.ui.theme.Green91C747
import com.forknowledge.core.ui.theme.GreyA7A6A6
import com.forknowledge.core.ui.theme.GreyEBEBEB
import com.forknowledge.core.ui.theme.openSansFamily
import com.forknowledge.feature.authentication.R

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SignInOptionScreen(
    viewModel: GoogleSignInViewModel,
    signInWithEmailClicked: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var exoPlayer: ExoPlayer? = null
    val videoUri =
        ("android.resource://" + context.packageName + "/" + R.raw.food_life_background_video).toUri()
    var playbackPosition = 0L

    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    var lifecycleEvent by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycleEvent = event
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (loginState) {
        LoginResultType.SUCCESS_NEW_USER -> onNavigateToOnboarding()
        LoginResultType.SUCCESS_OLD_USER -> onNavigateToHome()
        LoginResultType.FAIL -> {
            Toast.makeText(
                context,
                stringResource(R.string.authentication_sign_in_fail),
                Toast.LENGTH_SHORT
            ).show()
        }

        LoginResultType.NONE -> { /* Do nothing */
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(it).also { playerView ->
                    playerView.apply {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        useController = false
                    }
                }
            },
            update = {
                when (lifecycleEvent) {
                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer = ExoPlayer.Builder(context).build().also { player ->
                            it.player = player
                            player.setMediaItem(MediaItem.fromUri(videoUri), playbackPosition)
                            player.repeatMode = Player.REPEAT_MODE_ONE
                            player.play()
                            player.prepare()
                        }
                    }

                    Lifecycle.Event.ON_PAUSE -> {
                        exoPlayer?.let { player ->
                            playbackPosition = player.currentPosition
                            player.release()
                        }
                        exoPlayer = null
                    }

                    else -> Unit
                }
            }
        )

        SignInUiSection(
            onSignInWithEmailClicked = signInWithEmailClicked,
            onSignInWithGoogleClicked = { viewModel.signInWithGoogle(context) }
        )
    }
}

@Composable
internal fun SignInUiSection(
    onSignInWithEmailClicked: () -> Unit,
    onSignInWithGoogleClicked: () -> Unit
) {
    //onSignInWithAvailableCredentials()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 64.dp,
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = stringResource(id = R.string.authentication_sign_in_option_welcome),
            fontSize = 34.sp,
            textAlign = TextAlign.Start,
            fontFamily = openSansFamily,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            lineHeight = 40.sp
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(id = R.string.authentication_sign_in_option_introduction),
            fontFamily = openSansFamily,
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        SignInOptionButton(
            buttonTextColor = Color.White,
            buttonBackground = Green91C747,
            buttonIcon = drawable.ic_email_filled,
            buttonText = stringResource(id = R.string.authentication_sign_in_option_email),
            onClicked = { onSignInWithEmailClicked() }
        )

        SignInOptionButton(
            buttonBackground = GreyEBEBEB,
            buttonIcon = R.drawable.img_google_logo,
            buttonText = stringResource(id = R.string.authentication_sign_in_option_google),
            onClicked = { onSignInWithGoogleClicked() }
        )

        //TermAndPolicyText()
    }
}

@Composable
fun SignInOptionButton(
    @ColorRes buttonTextColor: Color = Color.Black,
    @ColorRes buttonBackground: Color,
    @DrawableRes buttonIcon: Int,
    buttonText: String,
    onClicked: () -> Unit
) {

    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(color = buttonBackground)
            .clickable { onClicked() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = buttonIcon),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = buttonText,
            fontFamily = openSansFamily,
            color = buttonTextColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun TermAndPolicyText() {
    val uriHandler = LocalUriHandler.current
    val termAndPolicy =
        stringResource(id = R.string.authentication_sign_in_option_term_and_policy)
    val annotatedLinkText = buildAnnotatedString {
        val termOfUseString = "Term of use"
        val termStartIndex = termAndPolicy.indexOf(termOfUseString)
        val termEndIndex = termStartIndex + termOfUseString.length

        val privacyPolicyString = "Privacy Policy"
        val policyStartIndex = termAndPolicy.indexOf(privacyPolicyString)
        val policyEndIndex = policyStartIndex + privacyPolicyString.length

        append(termAndPolicy)
        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = termStartIndex,
            end = termEndIndex
        )
        addStyle(
            style = SpanStyle(textDecoration = TextDecoration.Underline),
            start = policyStartIndex,
            end = policyEndIndex
        )
        addLink(
            url = LinkAnnotation.Url(""),
            termStartIndex,
            termEndIndex
        )
        addLink(
            url = LinkAnnotation.Url(""),
            start = policyStartIndex,
            end = policyEndIndex
        )
    }

    ClickableText(
        text = annotatedLinkText,
        style = TextStyle(
            fontFamily = openSansFamily,
            color = GreyA7A6A6,
            textAlign = TextAlign.Center
        )
    ) {
        annotatedLinkText
            .getUrlAnnotations(0, termAndPolicy.length)
            .forEach { annotation ->
                uriHandler.openUri(annotation.item.url)
            }
    }
}

@Preview(name = "Sign in with Email")
@Composable
fun SignInWithEmailButtonPreview() {
    SignInOptionButton(
        buttonTextColor = Color.White,
        buttonBackground = Green91C747,
        buttonIcon = drawable.ic_email_filled,
        buttonText = stringResource(id = R.string.authentication_sign_in_option_email),
        onClicked = {}
    )
}

@Preview(name = "Sign in with Google")
@Composable
fun SignInWithGoogleButtonPreview() {
    SignInOptionButton(
        buttonBackground = GreyEBEBEB,
        buttonIcon = R.drawable.img_google_logo,
        buttonText = stringResource(id = R.string.authentication_sign_in_option_google),
        onClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SignInUiSectionPreview() {
    SignInUiSection(
        onSignInWithEmailClicked = { },
        onSignInWithGoogleClicked = { }
    )
}
