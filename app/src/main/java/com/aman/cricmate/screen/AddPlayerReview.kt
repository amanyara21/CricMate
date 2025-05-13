package com.aman.cricmate.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aman.cricmate.components.AppHeader
import com.aman.cricmate.viewModel.AddReviewViewModel

@Composable
fun AddPlayerReview(
    userId: String,
    viewModel: AddReviewViewModel = hiltViewModel(),
) {
    val reviewText = viewModel.reviewText
    val isSubmitting = viewModel.isSubmitting
    val errorMessage = viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppHeader(title = "Add Your Review")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = reviewText,
                onValueChange = { viewModel.reviewText = it },
                label = { Text("Enter your review") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 10,
                textStyle = LocalTextStyle.current.copy(
                    lineHeight = 20.sp
                ),
                shape = RoundedCornerShape(12.dp)
            )


            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.submitReview(userId) },
                enabled = !isSubmitting && reviewText.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Submit Review")
                }
            }
            viewModel.successMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Green)
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }
        }
    }
}
