package com.sih2026.artisancatalog.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.BeigeCard
import com.sih2026.artisancatalog.ui.theme.BeigeCardBorder
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown

@Composable
fun PhotoCard(
    imagePath: String?,
    onPhotoActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasPhoto = !imagePath.isNullOrBlank()
    val cardShape = RoundedCornerShape(24.dp)

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Photo Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clickable { onPhotoActionClick() },
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = BeigeCard),
            border = BorderStroke(1.5.dp, if (hasPhoto) TerracottaPrimary else BeigeCardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (hasPhoto) {
                    // Display captured product photo
                    AsyncImage(
                        model = imagePath,
                        contentDescription = "Captured Product Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(cardShape),
                        contentScale = ContentScale.Crop
                    )

                    // Studio Tag badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .background(TerracottaPrimary, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.studio_photo_tag),
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Large obvious camera icon for low-literacy clarity
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(BeigeCard, RoundedCornerShape(48.dp))
                                .border(2.dp, TerracottaPrimary, RoundedCornerShape(48.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_camera),
                                contentDescription = "Camera Icon",
                                modifier = Modifier.size(54.dp),
                                tint = TerracottaPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Green Action Button: CLICK PHOTO / CHANGE PHOTO
        AccessibleButton(
            text = if (hasPhoto) stringResource(R.string.change_photo) else stringResource(R.string.click_photo),
            onClick = onPhotoActionClick,
            backgroundColor = ArtisanGreen,
            icon = painterResource(id = R.drawable.ic_camera)
        )
    }
}
