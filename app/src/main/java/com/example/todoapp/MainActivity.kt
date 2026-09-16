package com.example.todoapp

import Penampung
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
    val dataPenampung = remember { mutableStateListOf<Penampung>() }

    var showDialog by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }

    var showHapus by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { showDialog = true }) { Text("Tambah") }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { showHapus = true }) { Text("Hapus") }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                items(dataPenampung, key = { it.id }) { item ->
                    TodoItem(
                        id = item.id,
                        tugas = item.value,
                        onEdit = { newValue ->
                            val index = dataPenampung.indexOfFirst { it.id == item.id }
                            if (index != -1) {
                                dataPenampung[index] = dataPenampung[index].copy(value = newValue)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

//        FUNGSI TAMBAH
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
                                val newId = (dataPenampung.maxOfOrNull { it.id } ?: 0) + 1
                                dataPenampung.add(Penampung(newId, inputText))
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

//        FUNGSI HAPUS
        if (showHapus) {
            val selectedForDelete = remember { mutableStateListOf<Penampung>() }

            AlertDialog(
                onDismissRequest = {
                    showHapus = false
                },
                title = { Text("Hapus Tugas") },
                text = {
                    if (dataPenampung.isEmpty()) {
                        Text("Tidak ada tugas untuk dihapus.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(dataPenampung, key = { it.id }) { item ->
                                var isItemChecked by remember { mutableStateOf(false) }

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
                                    Checkbox(
                                        checked = isItemChecked,
                                        onCheckedChange = { checked ->
                                            isItemChecked = checked
                                            if (checked) {
                                                selectedForDelete.add(item)
                                            } else {
                                                selectedForDelete.remove(item)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = item.value)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dataPenampung.removeAll(selectedForDelete)
                            showHapus = false
                        }
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showHapus = false
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
fun TodoItem(
    id: Int,
    tugas: String,
    onEdit: (String) -> Unit = {}
) {
    var isChecked by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }
    var textEdit by remember { mutableStateOf(tugas) }

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
        Text(
            text = tugas,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            textEdit = tugas
            showEdit = true
        }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

    if (showEdit) {
        AlertDialog(
            onDismissRequest = { showEdit = false },
            title = { Text("Edit Tugas") },
            text = {
                OutlinedTextField(
                    value = textEdit,
                    onValueChange = { textEdit = it },
                    label = { Text("Nama Tugas") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (textEdit.isNotBlank()) {
                            onEdit(textEdit)
                            showEdit = false
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEdit = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
