package com.sih2026.artisancatalog.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sih2026.artisancatalog.ui.components.KalakritiLogo
import com.sih2026.artisancatalog.ui.theme.PureWhite
import com.sih2026.artisancatalog.ui.theme.TerracottaPrimary
import com.sih2026.artisancatalog.ui.theme.TextBrownSecondary
import com.sih2026.artisancatalog.ui.theme.TextDarkBrown
import com.sih2026.artisancatalog.ui.theme.WarmBackground
import com.sih2026.artisancatalog.ui.viewmodel.ProductViewModel

enum class AuthStage {
    LOGIN, VERIFY_OTP, SIGNUP
}

@Composable
fun AuthScreen(
    viewModel: ProductViewModel,
    onAuthComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStage by remember { mutableStateOf(AuthStage.LOGIN) }
    val draftState by viewModel.draftState.collectAsState()

    var mobileNumber by remember { mutableStateOf(draftState.userMobileNumber) }
    var fullName by remember { mutableStateOf("Ram Kumar") }
    var dob by remember { mutableStateOf("15/08/1988") }
    var otpDigits by remember { mutableStateOf(listOf("4", "2", "8", "9")) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmBackground)
            .padding(horizontal = 28.dp, vertical = 24.dp)
    ) {
        when (currentStage) {
            AuthStage.LOGIN -> {
                LoginContent(
                    mobileNumber = mobileNumber,
                    onMobileNumberChange = {
                        mobileNumber = it
                        viewModel.setMobileNumber(it)
                    },
                    onSendOtp = {
                        if (mobileNumber.isBlank()) mobileNumber = "98765 43210"
                        viewModel.setMobileNumber(mobileNumber)
                        currentStage = AuthStage.VERIFY_OTP
                    },
                    onCreateAccountClick = { currentStage = AuthStage.SIGNUP }
                )
            }
            AuthStage.VERIFY_OTP -> {
                VerifyOtpContent(
                    mobileNumber = mobileNumber.ifBlank { "98765 43210" },
                    otpDigits = otpDigits,
                    onOtpChange = { index, digit ->
                        val updated = otpDigits.toMutableList()
                        if (index in updated.indices) {
                            updated[index] = digit
                            otpDigits = updated
                        }
                    },
                    onVerifySuccess = {
                        viewModel.loginSuccess()
                        onAuthComplete()
                    },
                    onResendOtp = {
                        // Mock resend
                    }
                )
            }
            AuthStage.SIGNUP -> {
                SignUpContent(
                    fullName = fullName,
                    onFullNameChange = { fullName = it },
                    mobileNumber = mobileNumber,
                    onMobileNumberChange = {
                        mobileNumber = it
                        viewModel.setMobileNumber(it)
                    },
                    dob = dob,
                    onDobChange = { dob = it },
                    onSignUp = {
                        viewModel.setMobileNumber(mobileNumber)
                        currentStage = AuthStage.VERIFY_OTP
                    },
                    onLoginClick = { currentStage = AuthStage.LOGIN }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STAGE 1: LOGIN CONTENT
// -------------------------------------------------------------
@Composable
private fun LoginContent(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Logo with wordmark
            KalakritiLogo(
                size = 110.dp,
                showWordmark = true,
                tagline = "Crafted by hands, Connected by technology"
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "WELCOME BACK",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkBrown,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Continue your journey with Kalakriti",
                fontSize = 13.sp,
                color = TextBrownSecondary
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Field Label: LOGIN
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "LOGIN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkBrown,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mobile number text field with +91 prefix
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = onMobileNumberChange,
                placeholder = { Text("Enter mobile number", color = TextBrownSecondary) },
                leadingIcon = {
                    Text(
                        text = "+91  |  ",
                        fontWeight = FontWeight.Bold,
                        color = TextDarkBrown,
                        modifier = Modifier.padding(start = 14.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    focusedBorderColor = TerracottaPrimary,
                    unfocusedBorderColor = Color(0xFFDCC8B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Terracotta SEND OTP button
            Button(
                onClick = onSendOtp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
            ) {
                Text(
                    text = "SEND OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = PureWhite
                )
            }
        }

        // Footer link
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { onCreateAccountClick() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "New artisan? ", color = TextBrownSecondary, fontSize = 14.sp)
            Text(text = "Create an account", color = TerracottaPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// STAGE 2: VERIFY OTP CONTENT
// -------------------------------------------------------------
@Composable
private fun VerifyOtpContent(
    mobileNumber: String,
    otpDigits: List<String>,
    onOtpChange: (Int, String) -> Unit,
    onVerifySuccess: () -> Unit,
    onResendOtp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // Logo mark only
            KalakritiLogo(
                size = 90.dp,
                showWordmark = false
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Verify OTP",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkBrown
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Enter the 4-digit OTP sent to +91 $mobileNumber",
                    fontSize = 14.sp,
                    color = TextDarkBrown
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // 4 Individual OTP Input Circles matching reference
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { i ->
                    val digit = otpDigits.getOrElse(i) { "" }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PureWhite)
                            .border(1.5.dp, if (digit.isNotBlank()) TerracottaPrimary else Color(0xFF2C2018), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = digit,
                            onValueChange = { newText ->
                                if (newText.length <= 1) onOtpChange(i, newText)
                            },
                            textStyle = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDarkBrown,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Resend OTP Link
            Row(
                modifier = Modifier.clickable { onResendOtp() },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Didn't receive OTP? ", fontSize = 13.sp, color = TextBrownSecondary)
                Text(text = "Resend OTP", fontSize = 13.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Verify & Proceed Button
            Button(
                onClick = onVerifySuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
            ) {
                Text(
                    text = "VERIFY & CONTINUE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = PureWhite
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

// -------------------------------------------------------------
// STAGE 3: CREATE YOUR ACCOUNT
// -------------------------------------------------------------
@Composable
private fun SignUpContent(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    dob: String,
    onDobChange: (String) -> Unit,
    onSignUp: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            KalakritiLogo(
                size = 90.dp,
                showWordmark = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create your account",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TerracottaPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Start your journey with Kalakriti",
                fontSize = 13.sp,
                color = TextBrownSecondary
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 1. Full name
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                placeholder = { Text("Full name", color = TextBrownSecondary) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    focusedBorderColor = TerracottaPrimary,
                    unfocusedBorderColor = Color(0xFFDCC8B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Mobile number
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = onMobileNumberChange,
                placeholder = { Text("Mobile number", color = TextBrownSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    focusedBorderColor = TerracottaPrimary,
                    unfocusedBorderColor = Color(0xFFDCC8B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Date of Birth with calendar icon
            OutlinedTextField(
                value = dob,
                onValueChange = onDobChange,
                placeholder = { Text("Date of Birth", color = TextBrownSecondary) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Select DOB",
                        tint = TextBrownSecondary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite,
                    focusedBorderColor = TerracottaPrimary,
                    unfocusedBorderColor = Color(0xFFDCC8B8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(28.dp))

            // SIGN UP button
            Button(
                onClick = onSignUp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
            ) {
                Text(
                    text = "SIGN UP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = PureWhite
                )
            }
        }

        // Already have an account? Login
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable { onLoginClick() },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ", color = TextBrownSecondary, fontSize = 14.sp)
            Text(text = "Login", color = Color(0xFF1976D2), fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
