package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.ConnectionStatus
import com.example.data.model.LoginConfig
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
import com.example.ui.theme.UserBubbleColor
import com.example.ui.viewmodel.ChatUiState
import com.example.ui.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAccountDialog by remember { mutableStateOf(false) }
    var showClearHistoryDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            topBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(SelvaPrimaryGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = "خدمة العملاء",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "خدمة عملاء إتصالات",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = SelvaTextPrimary
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = SelvaGoldLight,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SelvaGold.copy(alpha = 0.3f))
                                        ) {
                                            Text(
                                                text = "VIP",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 10.sp,
                                                color = SelvaGoldDark,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        val isConnected = uiState.connectionStatus is ConnectionStatus.Connected
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (isConnected) SelvaAccentGreen else Color.Gray)
                                        )
                                        Text(
                                            text = when (val s = uiState.connectionStatus) {
                                                is ConnectionStatus.Connected -> "متصل (${s.dial})"
                                                is ConnectionStatus.Connecting -> s.step
                                                is ConnectionStatus.Error -> "خطأ في الاتصال"
                                                ConnectionStatus.Disconnected -> "غير متصل"
                                            },
                                            fontSize = 11.sp,
                                            color = SelvaTextSecondary
                                        )
                                        Text(
                                            text = "• SELVA",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SelvaPrimaryGreen
                                        )
                                    }
                                }
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.fetchNewMessages() },
                                modifier = Modifier.testTag("refresh_button")
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = "تحديث الرسائل", tint = SelvaTextSecondary)
                            }
                            IconButton(
                                onClick = { showClearHistoryDialog = true },
                                modifier = Modifier.testTag("clear_history_button")
                            ) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = "مسح سجل المحادثة", tint = SelvaTextSecondary)
                            }
                            IconButton(
                                onClick = { showAccountDialog = true },
                                modifier = Modifier.testTag("account_button")
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "الملف الشخصي والحساب", tint = SelvaPrimaryGreen)
                            }
                            IconButton(
                                onClick = { showSettingsDialog = true },
                                modifier = Modifier.testTag("settings_button")
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = "الإعدادات", tint = SelvaTextSecondary)
                            }
                            if (uiState.connectionStatus is ConnectionStatus.Connected) {
                                IconButton(
                                    onClick = { viewModel.disconnect() },
                                    modifier = Modifier.testTag("disconnect_button")
                                ) {
                                    Icon(Icons.Default.PowerSettingsNew, contentDescription = "قطع الاتصال", tint = Color(0xFFD32F2F))
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                }
            },
            modifier = modifier
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SelvaLightBg)
                    .padding(paddingValues)
                    .imePadding()
            ) {
                // Copyright & History Notice Banner
                Surface(
                    color = SelvaLightGreen,
                    modifier = Modifier.fillMaxWidth(),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, SelvaBorderLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = null,
                                tint = SelvaPrimaryGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "سجل المحادثات محفوظ محلياً (Room DB) 💾",
                                fontSize = 11.sp,
                                color = SelvaTextSecondary
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "حقوق SELVA",
                                tint = SelvaPrimaryGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "SELVA ©",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SelvaPrimaryGreen
                            )
                        }
                    }
                }

                if (uiState.connectionStatus is ConnectionStatus.Disconnected) {
                    DisconnectedCard(
                        onConnect = { viewModel.connect() },
                        onOpenSettings = { showSettingsDialog = true },
                        dial = uiState.loginConfig.dial
                    )
                }

                AnimatedVisibility(
                    visible = uiState.errorBanner != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    uiState.errorBanner?.let { msg ->
                        Surface(
                            color = Color(0xFFFFEBEE),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFC62828)
                                )
                                IconButton(onClick = { viewModel.clearBanners() }) {
                                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = Color(0xFFC62828))
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = uiState.successBanner != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    uiState.successBanner?.let { msg ->
                        Surface(
                            color = SelvaLightGreen,
                            border = androidx.compose.foundation.BorderStroke(1.dp, SelvaAccentGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SelvaPrimaryGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.clearBanners() }) {
                                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = SelvaPrimaryGreen)
                                }
                            }
                        }
                    }
                }

                if (uiState.connectionStatus is ConnectionStatus.Connecting) {
                    val step = (uiState.connectionStatus as ConnectionStatus.Connecting).step
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SelvaLightGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp,
                                color = SelvaPrimaryGreen
                            )
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = SelvaPrimaryGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    items(uiState.messages, key = { it.id }) { message ->
                        MessageBubble(
                            message = message,
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Etisalat Chat", message.content))
                                Toast.makeText(context, "تم نسخ النص", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                QuickChipsRow(
                    onSelect = { chipText ->
                        if (uiState.connectionStatus !is ConnectionStatus.Connected) {
                            viewModel.connect()
                        }
                        viewModel.sendMessage(chipText)
                    }
                )

                ChatInputBar(
                    text = uiState.inputText,
                    onTextChange = { viewModel.updateInputText(it) },
                    onSend = { viewModel.sendMessage() },
                    isSending = uiState.isSending,
                    enabled = true
                )
            }
        }

        if (showClearHistoryDialog) {
            AlertDialog(
                onDismissRequest = { showClearHistoryDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFD32F2F))
                        Spacer(Modifier.width(8.dp))
                        Text("مسح سجل المحادثات", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                text = {
                    Text("هل أنت متأكد من رغبتك في مسح كافة الرسائل المخزنة محلياً في هذا الجهاز؟ لا يمكن استرجاعها بعد الحذف.", fontSize = 13.sp)
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.clearChatHistory()
                            showClearHistoryDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                    ) {
                        Text("نعم، مسح السجل")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearHistoryDialog = false }) {
                        Text("إلغاء", color = SelvaTextSecondary)
                    }
                }
            )
        }

        if (showAccountDialog) {
            AccountDialog(
                uiState = uiState,
                onDismiss = { showAccountDialog = false },
                onClearHistory = {
                    showAccountDialog = false
                    showClearHistoryDialog = true
                },
                onLogout = {
                    showAccountDialog = false
                    viewModel.logout()
                }
            )
        }

        if (showSettingsDialog) {
            SettingsDialog(
                config = uiState.loginConfig,
                onDismiss = { showSettingsDialog = false },
                onSave = { updated: LoginConfig ->
                    viewModel.updateConfig(updated)
                }
            )
        }
    }
}

@Composable
private fun AccountDialog(
    uiState: ChatUiState,
    onDismiss: () -> Unit,
    onClearHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = SelvaPrimaryGreen)
                Spacer(Modifier.width(8.dp))
                Text("بيانات الحساب واشتراك بريميوم", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SelvaTextPrimary)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = SelvaLightBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = "البريد الإلكتروني:", fontSize = 12.sp, color = SelvaTextSecondary)
                        Text(
                            text = uiState.userSession.email.ifBlank { "غير محدد" },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SelvaTextPrimary
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(text = "رقم خط إتصالات:", fontSize = 12.sp, color = SelvaTextSecondary)
                        Text(
                            text = uiState.userSession.phone,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SelvaPrimaryGreen
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(text = "حالة الاشتراك:", fontSize = 12.sp, color = SelvaTextSecondary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = SelvaGoldDark, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "VIP Premium مفعل ✅",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SelvaPrimaryGreen
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(text = "الرسائل المحفوظة بالسجل:", fontSize = 12.sp, color = SelvaTextSecondary)
                        Text(
                            text = "${uiState.messages.size} رسالة مخزنة محلياً 💾",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SelvaTextPrimary
                        )
                    }
                }

                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/selvaappsbot"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC))
                ) {
                    Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("فتح بوت تيليجرام (@selvaappsbot)", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onClearHistory,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE57373))
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("مسح سجل المحادثات من الجهاز", color = Color(0xFFD32F2F), fontSize = 12.sp)
                }

                // Dedicated Copyright Note inside Account Dialog
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SelvaLightGreen,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7E2CE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "حقوق التطوير والملكية محفوظة لـ SELVA ©",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SelvaPrimaryGreen
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onLogout,
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("تسجيل الخروج")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إغلاق", color = SelvaTextSecondary)
            }
        }
    )
}

@Composable
private fun DisconnectedCard(
    onConnect: () -> Unit,
    onOpenSettings: () -> Unit,
    dial: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(SelvaLightGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = SelvaPrimaryGreen,
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "مرحباً بك في بوت خدمة عملاء إتصالات مصر VIP",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SelvaTextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "ابدأ جلسة محادثة مباشرة مع بوت خدمة العملاء برقم الخط ($dial)",
                style = MaterialTheme.typography.bodySmall,
                color = SelvaTextSecondary,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onConnect,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("start_chat_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SelvaPrimaryGreen)
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("الاتصال والبدء", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("open_settings_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = "تعديل البيانات", tint = SelvaTextSecondary)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    onCopy: () -> Unit
) {
    val isUser = !message.isBot
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(SelvaPrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.HeadsetMic,
                    contentDescription = "Bot",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) UserBubbleColor else Color.White,
            border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight),
            shadowElevation = if (isUser) 1.dp else 1.5.dp,
            modifier = Modifier
                .widthIn(max = 290.dp)
                .testTag(if (isUser) "user_message_bubble" else "bot_message_bubble")
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                if (!isUser) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "خدمة العملاء (Etisalat)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SelvaPrimaryGreen
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = SelvaPrimaryGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(Modifier.height(3.dp))
                }

                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else SelvaTextPrimary,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 10.sp,
                        color = if (isUser) Color.White.copy(alpha = 0.8f) else SelvaTextMuted
                    )
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "نسخ الرسالة",
                            tint = if (isUser) Color.White.copy(alpha = 0.8f) else SelvaTextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        if (isUser) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF334A38)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickChipsRow(
    onSelect: (String) -> Unit
) {
    val chips = listOf(
        "1-العربية",
        "عايز اكلم خدمه العملاء",
        "الاستعلام عن الرصيد",
        "عروض باقات الإنترنت",
        "كود معرفة رقم الخط",
        "تحدث مع ممثل الخدمة"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(chips) { chip ->
            SuggestionChip(
                onClick = { onSelect(chip) },
                label = { Text(chip, fontSize = 12.sp, color = SelvaTextPrimary) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = Color.White
                ),
                border = SuggestionChipDefaults.suggestionChipBorder(
                    enabled = true,
                    borderColor = SelvaPrimaryGreen.copy(alpha = 0.35f)
                ),
                modifier = Modifier.testTag("quick_chip_${chip.take(5)}")
            )
        }
    }
}

@Composable
private fun ChatInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean,
    enabled: Boolean
) {
    Surface(
        color = Color.White,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, SelvaBorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("اكتب رسالتك لخدمة العملاء...", fontSize = 14.sp, color = SelvaTextMuted) },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 50.dp)
                    .testTag("message_input_field"),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4,
                enabled = enabled,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SelvaPrimaryGreen,
                    unfocusedBorderColor = SelvaBorderLight,
                    focusedTextColor = SelvaTextPrimary,
                    unfocusedTextColor = SelvaTextPrimary,
                    focusedContainerColor = Color(0xFFFAFCFA),
                    unfocusedContainerColor = Color(0xFFFAFCFA)
                )
            )

            FilledIconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isSending,
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = SelvaPrimaryGreen,
                    disabledContainerColor = SelvaPrimaryGreen.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .size(48.dp)
                    .testTag("send_message_button")
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "إرسال",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
