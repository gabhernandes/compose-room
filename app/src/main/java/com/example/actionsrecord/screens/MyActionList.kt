package com.example.actionsrecord.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.actionsrecord.R
import com.example.actionsrecord.classes.MainViewModel
import com.example.actionsrecord.classes.MyAction
import com.example.actionsrecord.classes.MyDatabase
import com.example.actionsrecord.classes.byteArrayToBitmap

@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyActionList(
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
                    .padding(3.dp)
                    .clickable { onItemClick(action) },
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Name: ${action.name}", fontWeight = FontWeight.Bold)
                    Text(text = "Date of Birth: ${(action.dateOfBirth)}")
                }
                Spacer(modifier = Modifier.weight(1f))

                Column {
                    Text(text = "Status")
                    Switch(
                        checked = toggleState.value,
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
                        }
                    )
                }
            }
            Divider(modifier = Modifier.padding(vertical = 1.dp))
        }
    }
}

