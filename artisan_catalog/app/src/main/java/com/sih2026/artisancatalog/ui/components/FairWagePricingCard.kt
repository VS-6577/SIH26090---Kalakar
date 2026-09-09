package com.sih2026.artisancatalog.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import com.sih2026.artisancatalog.domain.model.PricingBreakdown
import com.sih2026.artisancatalog.ui.theme.ArtisanGreen
import com.sih2026.artisancatalog.ui.theme.ArtisanGreenBorder
import com.sih2026.artisancatalog.ui.theme.ArtisanGreenHover
import com.sih2026.artisancatalog.ui.theme.ArtisanGreenLight
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown

@Composable
fun FairWagePricingCard(
    pricing: PricingBreakdown,
    onAdjustPricing: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ArtisanGreenLight),
        border = BorderStroke(1.5.dp, ArtisanGreenBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.fair_wage_pricing),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArtisanGreenHover
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArtisanGreen.copy(alpha = 0.15f))
                        .clickable { onAdjustPricing() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⚙️ Adjust",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArtisanGreenHover
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Price Tag: ₹450
            Text(
                text = "${stringResource(R.string.currency_symbol)}${pricing.suggestedPrice.toInt()}",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ArtisanGreenHover,
                letterSpacing = (-1).sp
            )

            // "Your earnings"
            Text(
                text = stringResource(R.string.your_earnings),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextBrownSecondary
            )

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = ArtisanGreenBorder.copy(alpha = 0.6f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Breakdown: Material | Labor | Margin
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BreakdownItem(
                    label = stringResource(R.string.material),
                    amount = "₹${pricing.materialCost.toInt()}"
                )
                BreakdownItem(
                    label = stringResource(R.string.labor),
                    amount = "₹${pricing.laborCost.toInt()}"
                )
                BreakdownItem(
                    label = stringResource(R.string.margin),
                    amount = "₹${pricing.margin.toInt()}"
                )
            }
        }
    }
}

@Composable
private fun BreakdownItem(
    label: String,
    amount: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextBrownSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDarkBrown
        )
    }
}
