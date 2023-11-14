package com.floatworld.diacsc.composes.mainCompose

import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.addFriendsActivity.AddFriendActivity
import com.floatworld.diacsc.viewModel.MainViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun AddIconMenu(isExpanded:MutableState<Boolean>){
    val context= LocalContext.current
    val model:MainViewModel= viewModel()
    DropdownMenu(expanded = isExpanded.value, onDismissRequest = { isExpanded.value=false }) {
        DropdownMenuItem(text = { Text("添加好友") },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_person_add_24),
                contentDescription = "添加好友")},
            onClick = {
                val intent=Intent(context, AddFriendActivity::class.java)
                context.startActivity(intent)
            })
        DropdownMenuItem(text = { Text("扫一扫") },
            leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                contentDescription = "扫一扫")},
            onClick = {

                val options= GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                    .build()

                val scanner= GmsBarcodeScanning.getClient(context,options)
                scanner.startScan().addOnSuccessListener {
                    model.QRAddFriend(it.rawValue)
                }

            })
    }
}

//@Preview
//@Composable
//fun test(){
//
//}