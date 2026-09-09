package com.sih2026.artisancatalog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.BeigeCard
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown

@Composable
fun LanguageSelectorDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.language_select_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LanguageOption(
                    title = "English",
                    subTitle = "English language",
                    isSelected = currentLanguage == "en",
                    onClick = { onLanguageSelected("en") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                LanguageOption(
                    title = "हिन्दी",
                    subTitle = "Hindi language",
                    isSelected = currentLanguage == "hi",
                    onClick = { onLanguageSelected("hi") }
                )
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.ok),
                color = TerracottaPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(8.dp)
            )
        },
        containerColor = BeigeCard,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun LanguageOption(
    title: String,
    subTitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isSelected) TerracottaPrimary.copy(alpha = 0.12f) else PureWhite)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = TerracottaPrimary,
                unselectedColor = TextDarkBrown.copy(alpha = 0.6f)
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown
            )
            Text(
                text = subTitle,
                fontSize = 12.sp,
                color = TextDarkBrown.copy(alpha = 0.7f)
            )
        }
    }
}
