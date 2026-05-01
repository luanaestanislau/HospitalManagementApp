package br.com.fiap.hospitalmanagement.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.hospitalmanagement.R
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess

@Composable
fun InitialScreen(modifier: Modifier = Modifier) {
    Surface(
        modifier = Modifier
        .fillMaxSize(),
        color = MediBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 32.dp,
                    vertical = 48.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderSection()
            FeaturesSection()
            ActionSection(

            )
        }
    }
}

@Preview
@Composable
private fun InitialScreenPreview() {
    HospitalManagementTheme() {
        InitialScreen()
    }
}

@Composable
fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MediIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = stringResource(R.string.medistock_logo),
                    tint = MediPrimary,
                    modifier = Modifier.size(56.dp)
                )
            }
            Icon(
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                tint = MediPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.medistock),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = stringResource(R.string.medistock_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MediSubtext,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun HeaderSectionPreview() {
    HospitalManagementTheme() {
        HeaderSection()
    }
}

@Composable
private fun FeaturesSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FeatureItem(MediBlue, stringResource(R.string.feature_item))
        FeatureItem(MediPrimary, stringResource(R.string.feature_item2))
        FeatureItem(MediSuccess, stringResource(R.string.feature_item3))
    }
}

@Composable
private fun FeatureItem(color: Color, text: String) {
    Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier
             .fillMaxWidth()
             .clip(RoundedCornerShape(12.dp))
             .background(MediCardBg) // Fundo suave com a cor do tema
             .padding(horizontal = 16.dp, vertical = 14.dp)
     ) {
         Box(
             modifier = Modifier
                 .size(8.dp)
                 .clip(CircleShape)
                 .background(color)
         )
         androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(12.dp))
         Text(
             text = text,
             style = MaterialTheme.typography.bodyMedium,
             color = Color.White
         )
     }
}

@Preview
@Composable
private fun FeaturesSectionPreview() {
    HospitalManagementTheme() {
        FeaturesSection()
    }
}

@Composable
fun ActionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MediPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.signin),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp,
                Color.White.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = stringResource(R.string.learn_more),
                color = Color.White
            )
        }
        Text(
            text = stringResource(R.string.version_app),
            fontSize = 11.sp,
            color = MediSubtext
        )
    }
}

@Preview
@Composable
private fun ActionSectionPreview() {
    HospitalManagementTheme() {
        ActionSection()
    }
}