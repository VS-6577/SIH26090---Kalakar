package com.sih2026.artisancatalog.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.R
import com.sih2026.artisancatalog.ui.theme.BeigeCard
import com.sih2026.artisancatalog.ui.theme.BeigeCardBorder
import com.sih2026.artisancatalog.ui.theme.InputBeige
import com.sih2026.artisancatalog.ui.theme.MicRed
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown

@Composable
fun MicrophoneSection(
    isListening: Boolean,
    transcribedText: String,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onTextChange: (String) -> Unit,
    onSimulateDemoVoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.25f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Large Microphone Button Container
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(110.dp)
        ) {
            // Ripple background when recording
            if (isListening) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(MicRed.copy(alpha = 0.25f))
                )
            }

            // Central Mic Button
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(if (isListening) MicRed else TerracottaPrimary)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onStartListening()
                                tryAwaitRelease()
                                onStopListening()
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mic),
                    contentDescription = "Microphone",
                    modifier = Modifier.size(42.dp),
                    tint = PureWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // "Hold to speak" text label
        Text(
            text = if (isListening) stringResource(R.string.listening) else stringResource(R.string.hold_to_speak),
            style = TextStyle(
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = if (isListening) MicRed else TextDarkBrown
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // "Voice Description" section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.voice_description),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkBrown
                )
            )

            // Demo Voice Shortcut for easy testing
            Text(
                text = stringResource(R.string.demo_voice_btn),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TerracottaPrimary,
                modifier = Modifier
                    .clickable { onSimulateDemoVoice() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Large rounded input/output area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = InputBeige),
            border = BorderStroke(1.dp, BeigeCardBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                if (transcribedText.isEmpty()) {
                    Text(
                        text = stringResource(R.string.voice_hint),
                        fontSize = 14.sp,
                        color = TextBrownSecondary,
                        lineHeight = 20.sp
                    )
                }
                BasicTextField(
                    value = transcribedText,
                    onValueChange = onTextChange,
                    textStyle = TextStyle(
                        fontSize = 15.sp,
                        color = TextDarkBrown,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
