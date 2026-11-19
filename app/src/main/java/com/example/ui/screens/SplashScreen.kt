package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CinzelFontFamily
import com.example.ui.theme.NoorChampagneGold
import com.example.ui.theme.NoorDeepRoyalBlue
import com.example.ui.theme.NoorIvory
import com.example.ui.theme.NoorMidnightNavy
import com.example.ui.theme.NoorSecondaryText
import com.example.ui.theme.NoorSoftGold
import kotlinx.coroutines.delay

@Composable
fun SplashLoadingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scaleAnim = remember { Animatable(0.88f) }
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(450, easing = FastOutSlowInEasing))
        scaleAnim.animateTo(1f, animationSpec = tween(550, easing = FastOutSlowInEasing))
        delay(650)
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        NoorMidnightNavy,
                        NoorDeepRoyalBlue.copy(alpha = 0.75f),
                        NoorMidnightNavy
                    )
                )
            )
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_tasbih_noor_medallion),
                contentDescription = "Logo TASBIH NOOR",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TASBIH NOOR",
                fontFamily = CinzelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                letterSpacing = 3.sp,
                color = NoorChampagneGold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A Peaceful Digital Dhikr Experience.",
                style = MaterialTheme.typography.bodyLarge,
                color = NoorSoftGold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Versi 2.01",
                style = MaterialTheme.typography.labelMedium,
                color = NoorSecondaryText
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(28.dp),
                color = NoorChampagneGold,
                trackColor = NoorIvory.copy(alpha = 0.12f),
                strokeWidth = 2.5.dp
            )
        }
    }
}
