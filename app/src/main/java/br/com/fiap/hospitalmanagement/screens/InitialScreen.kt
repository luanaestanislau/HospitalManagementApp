package br.com.fiap.hospitalmanagement.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.hospitalmanagement.R
import br.com.fiap.hospitalmanagement.navigation.Destination
import br.com.fiap.hospitalmanagement.ui.theme.HospitalManagementTheme
import br.com.fiap.hospitalmanagement.ui.theme.MediBackground
import br.com.fiap.hospitalmanagement.ui.theme.MediBlue
import br.com.fiap.hospitalmanagement.ui.theme.MediCardBg
import br.com.fiap.hospitalmanagement.ui.theme.MediIconBg
import br.com.fiap.hospitalmanagement.ui.theme.MediPrimary
import br.com.fiap.hospitalmanagement.ui.theme.MediSubtext
import br.com.fiap.hospitalmanagement.ui.theme.MediSuccess

@Composable
fun InitialScreen(navController: NavController) {
    Surface(
        modifier = Modifier
        .fillMaxSize(),
        color = MediBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 28.dp,
                    vertical = 60.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HeaderSection()
            FeaturesSection()
            ActionSection(navController)
        }
    }
}

@Preview
@Composable
private fun InitialScreenPreview() {
    HospitalManagementTheme() {
        InitialScreen(rememberNavController())
    }
}

@Composable
fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(130.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MediIconBg),
            contentAlignment = Alignment.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = stringResource(R.string.medistock_logo),
                    tint = MediPrimary,
                    modifier = Modifier.size(64.dp)
                )
                Icon(
                    imageVector = Icons.Default.CheckCircleOutline,
                    contentDescription = null,
                    tint = MediPrimary,
                    modifier = Modifier
                        .size(28.dp)
                        .offset(x = 6.dp, y = 6.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.medistock),
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 32.sp
            )
        )
        Text(
            text = stringResource(R.string.medistock_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MediSubtext,
            textAlign = TextAlign.Center,
            fontSize = 22.sp
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
             .background(MediCardBg)
             .border(
                 width = 1.dp,
                 color = Color.White.copy(alpha = 0.1f)
             )
             .padding(horizontal = 20.dp, vertical = 18.dp)
     ) {
         Box(
             modifier = Modifier
                 .size(10.dp)
                 .clip(CircleShape)
                 .background(color)
         )
         Spacer(modifier = Modifier.size(14.dp))
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
fun ActionSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate(Destination.LoginScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MediPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.signin),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp,
                Color.White.copy(alpha = 0.5f)
            )
        ) {
            Text(
                text = stringResource(R.string.learn_more),
                color = Color.White,
                fontSize = 16.sp
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
        ActionSection(rememberNavController())
    }
}