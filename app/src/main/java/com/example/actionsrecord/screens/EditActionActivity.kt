package com.example.actionsrecord.screens

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.actionsrecord.classes.AppRepository
import com.example.actionsrecord.classes.ImageData
import com.example.actionsrecord.classes.MainViewModel
import com.example.actionsrecord.classes.MainViewModelFactory
import com.example.actionsrecord.classes.MyAction
import com.example.actionsrecord.classes.MyDatabase
import com.example.actionsrecord.classes.byteArrayToBitmap
import com.example.actionsrecord.classes.clickedAction
import com.example.actionsrecord.classes.goBack
import com.example.actionsrecord.classes.uriToByteArray

class EditActionActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewActivityUI()
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun NewActivityUI() {

        val name = remember { mutableStateOf(clickedAction!!.name) }
        val dateOfBirth = remember { mutableStateOf(clickedAction!!.dateOfBirth) }
        val cpf = remember { mutableStateOf(clickedAction!!.cpf) }
        val city = remember { mutableStateOf(clickedAction!!.city) }
        val toggleState = remember { mutableStateOf(clickedAction!!.isActive) }
        val context = LocalContext.current
        val database = MyDatabase.getDatabase(context)
        val repository = AppRepository(database.myActionDao())
        val viewModel = viewModel(
            factory = MainViewModelFactory(repository)
        ) as MainViewModel
        val imageDao = database.imageDataDao()
        val imageData = imageDao.getImageDataById(clickedAction!!.id)
        val byteArray = imageData!!.imageByteArray
        val imageUri = byteArrayToBitmap(byteArray)
        val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                if (uri != null) {
                    selectedImageUri.value = uri
                }
            }
        )


        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Edit Action") },
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
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                if (selectedImageUri.value != null) {
                    val painter = rememberImagePainter(data = selectedImageUri.value)

                    Image(
                        painter = painter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )
                } else {
                    val painter = rememberImagePainter(data = imageUri)

                    Image(
                        painter = painter,
                        contentDescription = "Placeholder Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

                    )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = dateOfBirth.value,
                    onValueChange = { dateOfBirth.value = it },
                    label = { Text("Date of birth") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

                    )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = cpf.value,
                    onValueChange = { cpf.value = it },
                    label = { Text("CPF (id)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),

                    )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = city.value,
                    onValueChange = { city.value = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "InActive", modifier = Modifier.padding(end = 8.dp))
                    Switch(
                        checked = toggleState.value,
                        onCheckedChange = { isChecked ->
                            toggleState.value = isChecked
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = "Active")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    val myAction = MyAction(
                        clickedAction!!.id,
                        name.value,
                        dateOfBirth.value,
                        cpf.value,
                        city.value,
                        isActive = toggleState.value,
                    )

                    if (name.value.isNotEmpty() && city.value.isNotEmpty()) {

                        if (selectedImageUri.value != null) {
                            imageDao.insertImageData(
                                ImageData(
                                    myAction.id, uriToByteArray(
                                        context.contentResolver,
                                        selectedImageUri.value!!
                                    )!!
                                )
                            )

                            viewModel.updateAction(myAction)
                            clickedAction = myAction
                            goBack = true
                            Toast.makeText(context, "Action Updated", Toast.LENGTH_SHORT).show()
                            (context as? Activity)?.onBackPressed()

                        } else {
                            viewModel.updateAction(myAction)
                            clickedAction = myAction
                            goBack = true
                            Toast.makeText(context, "Action Updated", Toast.LENGTH_SHORT).show()
                            (context as? Activity)?.onBackPressed()

                        }

                    } else {
                        Toast.makeText(context, "Place give all inputs", Toast.LENGTH_SHORT).show()
                    }

                }) {
                    Text("Update")
                }

            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun NewActivityPreview() {
        NewActivityUI()

    }

}
