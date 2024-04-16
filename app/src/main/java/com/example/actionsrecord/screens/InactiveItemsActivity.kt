package com.example.actionsrecord.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.actionsrecord.R
import com.example.actionsrecord.classes.AppRepository
import com.example.actionsrecord.classes.MainViewModel
import com.example.actionsrecord.classes.MainViewModelFactory
import com.example.actionsrecord.classes.MyAction
import com.example.actionsrecord.classes.MyDatabase
import com.example.actionsrecord.classes.byteArrayToBitmap
import com.example.actionsrecord.screens.ui.theme.ActionsRecordTheme

class InactiveItemsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var refreshContent by remember { mutableStateOf(false) } // State to trigger recomposition
            ActionsRecordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Display the inactive action list
                    InactiveActionListContent {
                        refreshContent =
                            !refreshContent // Trigger recomposition of the activity content
                    }
                }
            }
        }
    }
}

// InactiveActionListContent composable
@Composable
fun InactiveActionListContent(
    onSwitchToggled: () -> Unit // Callback to be invoked when the switch is toggled){}
) {
    val context = LocalContext.current
    val database = MyDatabase.getDatabase(context)
    val repository = AppRepository(database.myActionDao())
    val viewModel = viewModel(
        factory = MainViewModelFactory(repository)
    ) as MainViewModel

    viewModel.getAllInActiveActions()
    val inactiveActions by viewModel.actionList.observeAsState(emptyList())

    val db = MyDatabase.getDatabase(context)

    // Display the inactive action list
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Inactive Actions",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (inactiveActions.isEmpty()) {
            Text(text = "No inactive actions found")
        } else {
            InactiveActionList(viewModel, inactiveActions, db) { action ->
                // Handle action click (if needed)
                onSwitchToggled()
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun InactiveActionList(
    viewModel: MainViewModel,
    list: List<MyAction>,
    database: MyDatabase,
    onItemClick: (MyAction) -> Unit
) {
    LazyColumn {
        items(list) { action ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val imageDao = database.imageDataDao()
                val imageData = imageDao.getImageDataById(action.id)
                val toggleState = remember { mutableStateOf(action.isActive) }

                if (imageData.toString().isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = null,
                        modifier = Modifier
                            .size(68.dp)
                    )
                } else {
                    val byteArray = imageData!!.imageByteArray
                    val imageUri = byteArrayToBitmap(byteArray)
                    val painter = rememberImagePainter(data = imageUri)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(68.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                // Display action details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Name: ${action.name}", fontWeight = FontWeight.Bold)
                    Text(text = "Date of Birth: ${action.dateOfBirth}")
                }

                // Display spacer to push the switch to the end
                Spacer(modifier = Modifier.weight(1f))


                Column {
                    Text(text = "Status")
                    Switch(
                        checked = toggleState.value, // Change this to the state of the action
                        onCheckedChange = { isChecked ->
                            toggleState.value = isChecked

                            val newAction = MyAction(
                                action.id,
                                action.name,
                                action.dateOfBirth,
                                action.cpf,
                                action.city,
                                isChecked
                            )
                            viewModel.updateAction(newAction)
                            onItemClick(action)
                        }
                    )
                }
            }
            Divider()
        }
    }
}