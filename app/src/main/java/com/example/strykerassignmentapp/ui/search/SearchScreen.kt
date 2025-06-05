package com.example.strykerassignmentapp.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.strykerassignmentapp.data.response.QuestionItem
import com.example.strykerassignmentapp.feature.SearchScreenViewModel
import com.example.strykerassignmentapp.utils.toFormattedDate

@Composable
fun SearchScreen(
    viewModel: SearchScreenViewModel,
    onItemClick: (QuestionItem) -> Unit,
    modifier: Modifier
){
    val state = viewModel.uiState

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            label = { Text("Search StackOverflow") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        state.message?.let {
            Text(
                text = it,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        state.error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(top = 16.dp),
            content = {
                items(state.results){ question ->
                    QuestionItemCard(
                        question = question,
                        onItemClick = { onItemClick(question) }
                    )
                }
            }
        )
    }
}

@Composable
fun QuestionItemCard(
    question: QuestionItem,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Author: ${question.owner.displayName}")
            Text("Created On: ${question.creationDate.toFormattedDate()}")
            Text("Link: ${question.link}")
        }
    }
}
