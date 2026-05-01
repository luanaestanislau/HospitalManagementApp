package br.com.fiap.hospitalmanagement.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess

private val ALLOWED_DOMAINS = listOf(
    "hc.unicamp.br" to "Hospital das Clínicas",
    "einstein.br" to "Hospital Albert Einstein",
    "hcor.com.br" to "HCor",
    "incor.usp.br" to "InCor – USP",
    "hsl.org.br" to "Hospital Sírio-Libanês",
    "medistock.hosp.br" to "MediStock Hospital"
)

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
                color = MediBackground
    ) {
        val context = LocalContext.current
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val emailDomain = email.substringAfter("@", "")
        val recognizedDomain = ALLOWED_DOMAINS.find { it.first == emailDomain }
        val isBlockedPersonalEmail = emailDomain.isNotBlank() && recognizedDomain == null
                && (emailDomain.endsWith("gmail.com") || emailDomain.endsWith("outlook.com")
                || emailDomain.endsWith("yahoo.com") || emailDomain.endsWith("hotmail.com"))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            HeaderSectionLogin()
            Forms(
                email = email,
                onEmailChange = { 
                    email = it
                    errorMessage = null
                },
                password = password,
                onPasswordChange = {
                    password = it
                    errorMessage = null
                },
                showPassword = showPassword,
                onTogglePassword = { showPassword = !showPassword },
                recognizedDomain = recognizedDomain,
                isBlockedPersonalEmail = isBlockedPersonalEmail,
                errorMessage = errorMessage,
                onLoginClick = {
                    when {
                        email.isBlank() || password.isBlank() -> {
                            errorMessage = "Preencha todos os campos."
                        }
                        recognizedDomain == null -> {
                            errorMessage = "Domínio de e-mail não autorizado."
                        }
                        password.length < 4 -> {
                            errorMessage = "Senha inválida."
                        }
                        else -> {
                            val prefs = context.getSharedPreferences("medistock_prefs", Context.MODE_PRIVATE)
                            prefs.edit().putString("email", email).apply()
                            navController.navigate(Destination.FuncionalDataScreen.createRoute(email))
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    HospitalManagementTheme() {
        LoginScreen(rememberNavController(), modifier = Modifier)
    }
}


@Composable
fun HeaderSectionLogin(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MediIconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MediPrimary,
                modifier = Modifier.size(38.dp)
            )
        }
        Text(
            text = "Acesso Institucional",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Use o e-mail fornecido pelo hospital",
            fontSize = 13.sp,
            color = MediSubtext
        )
    }
}

@Preview
@Composable
private fun HeaderSectionLoginPreview() {
    HospitalManagementTheme() {
        HeaderSectionLogin()
    }
}

@Composable
fun Forms(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    showPassword: Boolean,
    onTogglePassword: () -> Unit,
    recognizedDomain: Pair<String, String>?,
    isBlockedPersonalEmail: Boolean,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MediCardBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "E-mail institucional",
            fontSize = 13.sp,
            color = MediSubtext,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            placeholder = { Text("usuario@hospital.br", color = MediSubtext) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MediPrimary,
                unfocusedBorderColor = MediSubtext,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        if (recognizedDomain != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MediSuccess.copy(alpha = 0.15f))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Domínio reconhecido: @${recognizedDomain.first} — ${recognizedDomain.second} ",
                    fontSize = 12.sp,
                    color = MediSuccess
                )
            }
        }

        Text(
            text = "Senha",
            fontSize = 13.sp,
            color = MediSubtext,
            fontWeight = FontWeight.Medium
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            visualTransformation = if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton (onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MediSubtext
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MediPrimary,
                unfocusedBorderColor = MediSubtext,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        Text(
            text = "E-mails pessoais (@gmail, @outlook) não são aceitos. O domínio precisa estar cadastrado pelo TI do hospital.",
            fontSize = 11.sp,
            color = MediSubtext
        )
    }

    if (isBlockedPersonalEmail) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MediError.copy(alpha = 0.15f))
                .padding(10.dp)
        ) {
            Text(
                text = "Exemplo de bloqueio: $email — domínio não autorizado. Contate o departamento de TI.",
                fontSize = 12.sp,
                color = MediError
            )
        }
    }
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            fontSize = 12.sp,
            color = MediError
        )
    }

    Button(
        onClick = onLoginClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MediPrimary)
    ) {
        Text(
            text = "Verificar e continuar",
            fontSize = 15.sp, fontWeight = FontWeight.SemiBold
        )
    }

    Text(
        text = "Acesso restrito · LGPD · Criptografia ponta a ponta",
        fontSize = 11.sp,
        color = MediSubtext
    )
}



@Preview
@Composable
private fun FormsPreview() {
    HospitalManagementTheme() {
        Forms(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            showPassword = false,
            onTogglePassword = {},
            recognizedDomain = null,
            isBlockedPersonalEmail = false,
            errorMessage = null,
            onLoginClick = {}
        )
    }
}



