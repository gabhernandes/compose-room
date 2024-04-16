package com.example.actionsrecord.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.actionsrecord.classes.MyAction
import com.example.actionsrecord.classes.MyDatabase
import com.example.actionsrecord.classes.byteArrayToBitmap
import com.example.actionsrecord.classes.clickedAction
import com.example.actionsrecord.classes.goBack

class ActionDetailsActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ActionDetailsScreen(myAction = clickedAction!!)
        }
    }

    override fun onResume() {
        super.onResume()
        if (goBack) {
            goBack = false
            onBackPressedDispatcher.onBackPressed()
        }
    }


}


@Composable
fun ActionDetailsScreen(myAction: MyAction) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),

        ) {

        item {
            Buttons()
            Spacer(modifier = Modifier.height(16.dp))
            ActionImage(id = myAction.id)
            Spacer(modifier = Modifier.height(16.dp))
            ActionInfo(myAction = myAction)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ActionImage(id: String?) {
    val db = MyDatabase.getDatabase(LocalContext.current)
    val dao = db.imageDataDao()

    id?.let {
        val imageData = dao.getImageDataById(id.toString())
        val byteArray = imageData?.imageByteArray
        val imageUri = byteArrayToBitmap(byteArray!!)
        Image(
            painter = rememberImagePainter(data = imageUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun Buttons() {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = { Text(text = "Details") },
            navigationIcon = {
                IconButton(onClick = {
                    (context as? Activity)?.onBackPressed()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Row {
            Button(onClick = {
                showDialog = true
            }) {
                Text("Delete")
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                val intent = Intent(context, EditActionActivity::class.java)
                context.startActivity(intent)

            }) {
                Text("Edit")
            }
        }
    }

    if (showDialog) {
        MyAlertAlertDialog(
            showDialog = true,
            onDismissRequest = { showDialog = false },
            onConfirmClick = {
                val db = MyDatabase.getDatabase(context)
                val dao = db.myActionDao()
                dao.deleteAction(clickedAction!!)
                Toast.makeText(context, "Action deleted", Toast.LENGTH_SHORT).show()
                (context as Activity).onBackPressed()
                showDialog = false
            }
        )
    }
}


@Composable
fun ActionInfo(myAction: MyAction) {
    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Name: " + myAction.name,
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = ("CPF (id): " + myAction.cpf),
            style = MaterialTheme.typography.caption
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Date of Birth: " + myAction.dateOfBirth,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Status: " + (if (myAction.isActive) "Active" else "Inactive"),
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "City",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = myAction.city,
            style = MaterialTheme.typography.body2
        )
    }
}


@Composable
fun MyAlertAlertDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .width(280.dp)
                    .padding(16.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Alert!",
                        style = MaterialTheme.typography.h6,
                        color = Color.Black
                    )
                    Text(
                        text = "Are you sure you want to delete it?",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                onDismissRequest()
                                onConfirmClick()
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = onDismissRequest) {
                            Text(text = "Cancel")
                        }
                    }
                }
            }
        }
    }
}



