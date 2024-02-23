package com.example.meowlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.meowlearning.ui.theme.MeowlearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeowlearningTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(Color.Black)
                ) { innerPadding ->
                    Greeting(

                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun Greeting(modifier: Modifier) {
    var showDialogueBox by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingNoteId by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier
                .padding(bottom = 30.dp)
            .padding(top = 16.dp)

                .padding(vertical = 12.dp, horizontal = 24.dp),
            onClick = {
                //your onclick code
                showDialogueBox = true
            },
            border = BorderStroke(2.dp, Color.Red),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(text = "Add Notes Here", color = Color.DarkGray)
        }
//        Button(
//            onClick = { showDialogueBox = true },
//            modifier = Modifier
//                .padding(bottom = 30.dp)
//                .
////                .background(Color.Cyan)
//                .padding(vertical = 12.dp, horizontal = 24.dp)
//        ) {
//            Text(text = "Add Note", color = Color.White)
//        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(notes) { note ->
                NoteItem(
                    item = note,
                    onDelete = { notes = notes.filter { it.id != note.id } },
                    onEdit = {
                        title = note.title
                        content = note.content
                        editingNoteId = note.id
                        showDialogueBox = true
                    }
                )
            }
        }
    }

    if (showDialogueBox) {
        AlertDialog(
            title = { Text(text = if (editingNoteId == -1) "Add Note" else "Edit Note") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        label = { Text(text = "Title") }
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        label = { Text(text = "Content") }
                    )
                }
            },
            onDismissRequest = { showDialogueBox = false },
            confirmButton = {
                Button(onClick = {
                    if (editingNoteId == -1) {
                        notes = notes + Note(
                            id = notes.size,
                            title = title,
                            content = content
                        )
                    } else {
                        notes = notes.map {
                            if (it.id == editingNoteId) {
                                it.copy(title = title, content = content)
                            } else {
                                it
                            }
                        }
                    }
                    title = ""
                    content = ""
                    editingNoteId = -1
                    showDialogueBox = false
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
}

@Composable
fun NoteItem(item: Note, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
        ,
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "Title: ${item.title}",
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Text(
                text = "Content: ${item.content}",
                modifier = Modifier.padding(bottom = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

data class Note(val id: Int, var title: String, var content: String)