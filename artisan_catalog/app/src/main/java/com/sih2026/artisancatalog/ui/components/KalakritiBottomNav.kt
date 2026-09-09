package com.sih2026.artisancatalog.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary

enum class NavTab {
    HOME, MARKET, SETTINGS, NONE
}

@Composable
fun KalakritiBottomNav(
    activeTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    onPlusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Floating Terracotta Plus Button (Separate from the 3 navigation tabs)
        Box(
            modifier = Modifier
                .size(58.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(TerracottaPrimary)
                .clickable { onPlusClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Product",
                tint = PureWhite,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Rounded White Navigation Capsule
        Card(
            modifier = Modifier
                .weight(1f)
                .height(58.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(58.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = activeTab == NavTab.HOME,
                    onClick = { onTabSelected(NavTab.HOME) }
                )

                NavTabItem(
                    label = "Market",
                    icon = Icons.Default.Storefront,
                    isSelected = activeTab == NavTab.MARKET,
                    onClick = { onTabSelected(NavTab.MARKET) }
                )

                NavTabItem(
                    label = "Settings",
                    icon = Icons.Default.Settings,
                    isSelected = activeTab == NavTab.SETTINGS,
                    onClick = { onTabSelected(NavTab.SETTINGS) }
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) TerracottaPrimary else TextBrownSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) TerracottaPrimary else TextBrownSecondary
        )
    }
}
