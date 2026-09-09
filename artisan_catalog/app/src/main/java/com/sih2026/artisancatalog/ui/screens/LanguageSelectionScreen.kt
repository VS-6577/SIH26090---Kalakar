package com.sih2026.artisancatalog.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.ui.components.AccessibleButton
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel

data class LanguageItem(
    val code: String,
    val letter: String,
    val name: String,
    val letterColor: Color
)

@Composable
fun LanguageSelectionScreen(
    viewModel: ProductViewModel,
    onNavigateToAuth: () -> Unit,
    modifier: Modifier = Modifier
) {
    val draftState by viewModel.draftState.collectAsState()
    val selectedCode = draftState.selectedLanguage

    val languages = listOf(
        LanguageItem("hi", "अ", "हिंदी (Hindi)", ArtisanGreen),
        LanguageItem("en", "A", "English", TerracottaPrimary),
        LanguageItem("bn", "অ", "বাংলা (Bengali)", Color(0xFFC0392B)),
        LanguageItem("te", "అ", "తెలుగు (Telugu)", TerracottaPrimary),
        LanguageItem("mr", "म", "मराठी (Marathi)", TerracottaPrimary),
        LanguageItem("ta", "அ", "தமிழ் (Tamil)", ArtisanGreen),
        LanguageItem("gu", "અ", "ગુજરાતી (Gujarati)", TerracottaPrimary),
        LanguageItem("kn", "ಅ", "ಕನ್ನಡ (Kannada)", Color(0xFF00796B))
    )

    val currentSelectedName = languages.find { it.code == selectedCode }?.name ?: "हिंदी (Hindi)"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 22.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Globe / Translation Header Icon
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(PureWhite)
                    .border(1.5.dp, TerracottaPrimary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌐",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title
            Text(
                text = "SELECT YOUR LANGUAGE / भाषा चुनें",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Choose the language you are most comfortable\nspeaking and reading",
                fontSize = 13.sp,
                color = TextBrownSecondary,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(26.dp))

            // 2-Column Grid of 8 Languages
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(languages, key = { it.code }) { item ->
                    val isSelected = item.code == selectedCode
                    LanguageCardItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = {
                            viewModel.selectLanguage(item.code)
                        }
                    )
                }
            }
        }

        // Bottom Confirmation & Continue Button
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = "$currentSelectedName selected",
                fontSize = 13.sp,
                color = TextBrownSecondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Green CTA Button: जारी रखें / Continue →
            AccessibleButton(
                text = "जारी रखें / Continue →",
                onClick = onNavigateToAuth,
                backgroundColor = ArtisanGreen
            )
        }
    }
}

@Composable
private fun LanguageCardItem(
    item: LanguageItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(105.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) TerracottaPrimary else Color(0xFFE8DFD5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Green Checkmark on top-right when selected
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(ArtisanGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = PureWhite,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.letter,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.letterColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDarkBrown,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
