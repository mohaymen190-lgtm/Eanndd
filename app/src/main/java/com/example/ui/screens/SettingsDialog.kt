package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LoginConfig
import com.example.ui.theme.SelvaBorderLight
import com.example.ui.theme.SelvaLightBg
import com.example.ui.theme.SelvaLightGreen
import com.example.ui.theme.SelvaPrimaryGreen
import com.example.ui.theme.SelvaTextPrimary
import com.example.ui.theme.SelvaTextSecondary

@Composable
fun SettingsDialog(
    config: LoginConfig,
    onDismiss: () -> Unit,
    onSave: (LoginConfig) -> Unit
) {
    var email by remember { mutableStateOf(config.email) }
    var password by remember { mutableStateOf(config.password) }
    var dial by remember { mutableStateOf(config.dial) }
    var chatbotToken by remember { mutableStateOf(config.chatbotToken) }
    var model by remember { mutableStateOf(config.model) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = SelvaPrimaryGreen)
                Text("إعدادات حساب إتصالات", fontWeight = FontWeight.Bold, color = SelvaTextPrimary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "يمكنك تعديل بيانات تسجيل الدخول أو رقم الخط أو رمز التوكن للاتصال بـ Etisalat Chatbot API.",
                    style = MaterialTheme.typography.bodySmall,
                    color = SelvaTextSecondary
                )

                OutlinedTextField(
                    value = dial,
                    onValueChange = { dial = it },
                    label = { Text("رقم الخط (Dial)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = SelvaPrimaryGreen) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SelvaPrimaryGreen,
                        unfocusedBorderColor = SelvaBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("setting_dial_input")
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("البريد الإلكتروني / اسم المستخدم") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SelvaPrimaryGreen) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SelvaPrimaryGreen,
                        unfocusedBorderColor = SelvaBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("setting_email_input")
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("كلمة المرور (اختياري للتوكن)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SelvaPrimaryGreen,
                        unfocusedBorderColor = SelvaBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("setting_password_input")
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("طراز الجهاز (Model)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SelvaPrimaryGreen,
                        unfocusedBorderColor = SelvaBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("setting_model_input")
                )

                OutlinedTextField(
                    value = chatbotToken,
                    onValueChange = { chatbotToken = it },
                    label = { Text("رمز التوكن المباشر (Chatbot JWT)") },
                    leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = SelvaPrimaryGreen) },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SelvaPrimaryGreen,
                        unfocusedBorderColor = SelvaBorderLight
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("setting_token_input")
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rights Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SelvaLightGreen,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7E2CE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = SelvaPrimaryGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "جميع الحقوق محفوظة © SELVA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SelvaPrimaryGreen
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        config.copy(
                            email = email,
                            password = password,
                            dial = dial,
                            chatbotToken = chatbotToken,
                            model = model
                        )
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SelvaPrimaryGreen),
                modifier = Modifier.testTag("save_settings_button")
            ) {
                Text("حفظ التغييرات")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_settings_button")
            ) {
                Text("إلغاء", color = SelvaTextSecondary)
            }
        }
    )
}
