package com.panyou.focusflow.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var showAddTask by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    
    var selectedTaskId by remember { mutableStateOf<String?>(null) }

    if (selectedTaskId != null) {
        TaskDetailSheet(
            taskId = selectedTaskId!!,
            viewModel = detailViewModel,
            onDismiss = { selectedTaskId = null }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FocusFlow") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddTask = !showAddTask }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(visible = showAddTask) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        TextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            label = { Text("Task Title") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {
                                if (newTaskTitle.isNotBlank()) {
                                    viewModel.addTask(newTaskTitle)
                                    newTaskTitle = ""
                                    showAddTask = false
                                }
                            }) {
                                Icon(Icons.Filled.Add, contentDescription = "Save Task")
                            }
                        }
                    }
                }
            }

            if (tasks.isEmpty()) {
                Text(
                    text = "No tasks yet. Create one!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onCheckedChange = { isChecked ->
                                viewModel.onTaskCheckedChange(task, isChecked)
                            },
                            onDelete = {
                                viewModel.onTaskDelete(task)
                            },
                            onClick = { selectedTaskId = task.id }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (task.isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Text(
                text = task.title,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                )
            )
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete Task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
