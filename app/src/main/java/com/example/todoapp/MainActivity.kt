package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val listTugas = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Todo App") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Baris Tombol Aksi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { showDialog = true }) { Text("Tambah") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {}) { Text("Edit") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {}) { Text("Hapus") }
            }

            // Daftar Tugas Utama
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                items(listTugas) { tugas ->
                    TodoItem(tugas = tugas)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Dialog untuk Input Tugas Baru
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    inputText = ""
                },
                title = { Text("Tambah Tugas") },
                text = {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("Nama Tugas") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                listTugas.add(inputText)
                                inputText = ""
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Simpan")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            inputText = ""
                        }
                    ) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun TodoItem(tugas: String) {
    var isChecked by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = tugas)
    }
}
