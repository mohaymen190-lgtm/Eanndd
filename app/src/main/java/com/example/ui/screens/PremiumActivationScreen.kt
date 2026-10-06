package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.UserSession
import com.example.ui.theme.SelvaAccentGreen
import com.example.ui.theme.SelvaBorderLight
import com.example.ui.theme.SelvaGold
import com.example.ui.theme.SelvaGoldDark
import com.example.ui.theme.SelvaGoldLight
import com.example.ui.theme.SelvaLightBg
import com.example.ui.theme.SelvaLightGreen
import com.example.ui.theme.SelvaMintSoft
import com.example.ui.theme.SelvaPrimaryGreen
import com.example.ui.theme.SelvaTextMuted
import com.example.ui.theme.SelvaTextPrimary
import com.example.ui.theme.SelvaTextSecondary

@Composable
fun PremiumActivationScreen(
    userSession: UserSession,
    isVerifying: Boolean,
    isVerified: Boolean,
    errorMessage: String?,
    successMessage: String?,
    onActivatePremium: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val telegramBotUrl = "http://t.me/selvaappsbot"

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFAFCFA),
                            Color(0xFFF2F8F4),
                            Color(0xFFE9F4ED)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Hero Banner
                Card(
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.etisalat_premium_hero),
                            contentDescription = "بانر تفعيل بريميوم",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color(0xFF0C2415).copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SelvaGoldLight,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SelvaGold.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Diamond,
                                        contentDescription = null,
                                        tint = SelvaGoldDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "عضوية VIP",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = SelvaGoldDark
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "تفعيل خدمة عملاء إتصالات",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User Info Chip - Crisp Light Surface
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight),
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "المستخدم الحالي: ",
                            fontSize = 12.sp,
                            color = SelvaTextSecondary
                        )
                        Text(
                            text = userSession.email.ifBlank { userSession.phone },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SelvaPrimaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "لتشغيل التطبيق يتطلب تفعيل اشتراك بريميوم",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SelvaTextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "للحصول على وصول مباشر إلى بوت وممثلي خدمة عملاء إتصالات، قم بزيارة بوت التفعيل في تيليجرام ثم اضغط على زر تفعيل اشتراك بريميوم.",
                    fontSize = 13.sp,
                    color = SelvaTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp,
                    modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
                )

                // Instruction Card 1 - Light crisp card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "الخطوة الأولى: الانتقال إلى تيليجرام",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SelvaTextPrimary
                            )
                        }

                        Text(
                            text = "قم بفتح البوت على تيليجرام (@selvaappsbot) واضغط Start لإتمام تفعيل حسابك:",
                            fontSize = 13.sp,
                            color = SelvaTextSecondary
                        )

                        // Button 1: Open Telegram Bot
                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(telegramBotUrl))
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    // fallback
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("open_telegram_bot_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0088CC),
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "الانتقال إلى بوت التفعيل (@selvaappsbot)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // Bot & Token Reference
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF6FAF7),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDEADE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "رابط البوت: t.me/selvaappsbot",
                                    fontSize = 12.sp,
                                    color = SelvaPrimaryGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "رمز التحقق لحسابك: ${userSession.verificationCode}",
                                    fontSize = 12.sp,
                                    color = SelvaGoldDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Verification & Activation Action Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(SelvaLightGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null,
                                    tint = SelvaPrimaryGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = "الخطوة الثانية: تفعيل الاشتراك والتحقق",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SelvaTextPrimary
                            )
                        }

                        Text(
                            text = "بعد التوجه للبوت في تيليجرام، اضغط الزر بالأسفل للتحقق وفتح التطبيق فوراً:",
                            fontSize = 13.sp,
                            color = SelvaTextSecondary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Status messages
                        if (successMessage != null || isVerified) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SelvaLightGreen,
                                border = androidx.compose.foundation.BorderStroke(1.dp, SelvaAccentGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SelvaPrimaryGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = successMessage ?: "تم التحقق وتفعيل اشتراك بريميوم بنجاح! جاري الدخول...",
                                        color = SelvaPrimaryGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = Color(0xFFD32F2F),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        // THE MAIN REQUESTED INTERACTIVE BUTTON: "تفعيل اشتراك بريميوم"
                        Button(
                            onClick = onActivatePremium,
                            enabled = !isVerifying,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("activate_premium_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SelvaPrimaryGreen,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            if (isVerifying) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "جاري التحقق من التفعيل...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "تفعيل اشتراك بريميوم",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // VIP Benefits Preview - Calm soft background
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "مزايا اشتراك بريميوم VIP:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SelvaGoldDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = SelvaPrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("تواصل مباشر وفوري مع موظفي خدمة عملاء إتصالات", fontSize = 12.sp, color = SelvaTextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Speed, contentDescription = null, tint = SelvaPrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("استعلام رصيد وميجابايتس لحظي بدون انتظار", fontSize = 12.sp, color = SelvaTextPrimary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = SelvaPrimaryGreen, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("أولوية دعم قصوى وسرعة استجابة فائقة", fontSize = 12.sp, color = SelvaTextPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign out / Change Account
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SelvaTextSecondary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(text = "تسجيل الخروج أو استخدام حساب آخر", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Rights / Copyrights Footer Badge - "SELVA"
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SelvaLightGreen,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7E2CE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "حقوق SELVA",
                            tint = SelvaPrimaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "جميع الحقوق محفوظة © SELVA",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SelvaPrimaryGreen
                        )
                    }
                }
            }
        }
    }
}
