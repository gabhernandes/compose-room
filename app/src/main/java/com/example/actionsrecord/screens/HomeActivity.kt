package com.example.actionsrecord.screens

import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.actionsrecord.classes.AppRepository
import com.example.actionsrecord.classes.MainViewModel
import com.example.actionsrecord.classes.MainViewModelFactory
import com.example.actionsrecord.classes.MyAction
import com.example.actionsrecord.classes.MyDatabase
import com.example.actionsrecord.classes.clickedAction

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeActivity() {
    val context = LocalContext.current as ComponentActivity
    val database = MyDatabase.getDatabase(context)
    val repository = AppRepository(database.myActionDao())
    val viewModel = viewModel(
        factory = MainViewModelFactory(repository)
    ) as MainViewModel

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleState = lifecycle.observeAsState()

    DisposableEffect(key1 = lifecycleState.value) {
        if (lifecycleState.value == Lifecycle.Event.ON_RESUME) {
            viewModel.getAllActiveActions()
        }
        onDispose { }
    }

    viewModel.getAllActiveActions()
    val actionList by viewModel.actionList.observeAsState(emptyList())

    val db = MyDatabase.getDatabase(context)

    val actionList2 = remember { mutableStateListOf<MyAction>() }
    actionList2.addAll(actionList)

    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val searchQuery = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery.value,
                        onValueChange = { searchQuery.value = it },
                        placeholder = { Text("Search actions...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    // Add a button to open the inactive list activity
                    IconButton(onClick = {
                        val intent = Intent(context, InactiveItemsActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Inactive List")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues) then (Modifier.padding(16.dp))
        ) {
            // Only fetch and show actions that match the search query
            val filteredList = if (searchQuery.value.isEmpty()) {
                actionList.filter {
                    it.isActive
                }
            } else {
                actionList.filter {
                    (it.name.contains(searchQuery.value, ignoreCase = true) ||
                            it.dateOfBirth.contains(searchQuery.value, ignoreCase = true)) &&
                            it.isActive
                }
            }

            actionList2.clear()
            actionList2.addAll(filteredList)

            Text(text = "List of Active Actions")
            Spacer(modifier = Modifier.height(16.dp))
            MyActionList(viewModel, actionList2, db) { action: MyAction ->
                run {
                    clickedAction = action
                    val intent = Intent(context, ActionDetailsActivity::class.java)
                    context.startActivity(intent)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    title.value = ""
                    description.value = ""
                    val intent = Intent(context, AddActionActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Add, contentDescription = "+ Add New ")
                Text(text = "Add New")
            }
        }
    }
}


@Composable
fun Lifecycle.observeAsState(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsState.addObserver(observer)
        onDispose {
            this@observeAsState.removeObserver(observer)
        }
    }
    return state
}
