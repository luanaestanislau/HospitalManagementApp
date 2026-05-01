package br.com.fiap.hospitalmanagement.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediError
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess


@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
                color = MediBackground
    ) {
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
            Forms()
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    HospitalManagementTheme() {
        LoginScreen()
    }
}


@Composable
fun HeaderSectionLogin(modifier: Modifier = Modifier) {
    // Removed verticalScroll to fix "measured with infinity maximum height constraints" error.
    // Also removed redundant padding and spacer as they are already handled by the parent LoginScreen Column.
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
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
fun Forms(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
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
            value = "",
            onValueChange = {

            },
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

        if (null == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MediSuccess.copy(alpha = 0.15f))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Domínio reconhecido: ",
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
            value = "",
            onValueChange = {

            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,

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

    // Bloqueio de domínio pessoal (fora do card)
    if (null == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MediError.copy(alpha = 0.15f))
                .padding(10.dp)
        ) {
            Text(
                text = "Exemplo de bloqueio: .",
                fontSize = 12.sp,
                color = MediError
            )
        }
    }
    if (null != null) {
        Text(
            text = "",
            fontSize = 12.sp,
            color = MediError
        )
    }

    Button(
        onClick = {




        },
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
        Forms()
    }
}



