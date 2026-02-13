package com.panyou.focusflow.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.panyou.focusflow.data.local.entity.Task
import com.panyou.focusflow.ui.taskdetail.TaskDetailSheet
import com.panyou.focusflow.ui.taskdetail.TaskDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    detailViewModel: TaskDetailViewModel,
    modifier: Modifier = Modifier
) {
    // Revert to stable collectAsState
    val tasks by viewModel.tasks.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    var selectedTaskId by remember { mutableStateOf<String?>(null) }

    if (selectedTaskId != null) {
        TaskDetailSheet(
            taskId = selectedTaskId!!,
            viewModel = detailViewModel,
            onDismiss = { selectedTaskId = null }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "FocusFlow",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { viewModel.addTask("New Task ${tasks.size + 1}") },
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (tasks.isEmpty()) {
                Text(
                    text = "Hilt & Room Active. No tasks yet.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks, key = { it.id }) { task ->
                        ListItem(
                            headlineContent = { Text(task.title) },
                            leadingContent = {
                                Checkbox(
                                    checked = task.isCompleted,
                                    onCheckedChange = { viewModel.onTaskCheckedChange(task, it) }
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { viewModel.onTaskDelete(task) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
