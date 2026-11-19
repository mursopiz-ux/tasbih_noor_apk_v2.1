package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.NoorChampagneGold

@Composable
fun TasbihNoorLogoBadge(
    size: Dp = 44.dp,
    modifier: Modifier = Modifier,
    withGlow: Boolean = false
) {
    Image(
        painter = painterResource(id = R.drawable.logo_tasbih_noor_medallion),
        contentDescription = "Logo Resmi TASBIH NOOR",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .then(
                if (withGlow) {
                    Modifier.shadow(elevation = 10.dp, shape = CircleShape, spotColor = NoorChampagneGold)
                } else {
                    Modifier
                }
            )
            .clip(CircleShape)
            .border(1.dp, NoorChampagneGold.copy(alpha = 0.45f), CircleShape)
    )
}
