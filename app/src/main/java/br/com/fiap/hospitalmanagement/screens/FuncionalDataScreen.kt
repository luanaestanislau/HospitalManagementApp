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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess

@Composable
fun FuncionalDataScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MediBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            HeaderIcon()
            FormsFuncionalData()

        }
    }
}

@Preview
@Composable
private fun FuncionalDataScreenPreview() {
    HospitalManagementTheme() {
        FuncionalDataScreen()
    }
}

@Composable
fun HeaderIcon(modifier: Modifier = Modifier) {
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
}

@Preview
@Composable
private fun HeaderIconPreview() {
    HospitalManagementTheme() {
        HeaderIcon()
    }
}

@Composable
fun FormsFuncionalData(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MediCardBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Confirme seus dados funcionais",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        FunctionalField(label = "Matrícula funcional", value = "matricula") {}
        FunctionalField(label = "Departamento", value = "department") { }
        FunctionalField(label = "Cargo / Função", value = "role") { }
        FunctionalField(
            label = "Registro profissional (CRF/CRM/etc.)",
            value = ""
        ) { }

        if (null ==  null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MediSuccess.copy(alpha = 0.15f))
                    .padding(10.dp)
            ) {
                Text(
                    text = "Matrícula encontrada na base do hospital. Dados conferidos com o RH.",
                    fontSize = 12.sp,
                    color = MediSuccess
                )
            }
        }
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
            text = "Confirmar e avançar",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
    Text(
        text = "Dados validados com o sistema de RH hospitalar via API interna. Matrícula inativa ou sem vínculo será recusada.",
        fontSize = 11.sp,
        color = MediSubtext,
        textAlign = TextAlign.Center,
    )
}

@Preview
@Composable
private fun FormsFuncionalDataPreview() {
    HospitalManagementTheme() {
        FormsFuncionalData()
    }
}
@Composable
private fun FunctionalField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MediPrimary,
                unfocusedBorderColor = MediSubtext,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}
