package com.floatworld.diacsc.composes.addFirend

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.floatworld.diacsc.R
import com.floatworld.diacsc.addFriendsActivity.viewModel.AddFriendViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchText(modifier:Modifier){
    val interactionSource = remember { MutableInteractionSource() }
    val model: AddFriendViewModel = viewModel()
    val context= LocalContext.current
    BasicTextField(modifier= modifier
        .padding(5.dp),value =model.id.value ,
        onValueChange = { model.id.value=it }, textStyle = LocalTextStyle.current,
        singleLine = true,
        keyboardActions =  KeyboardActions(onDone = {
            model.getUser(model.id.value, context)
        })
    ){
        TextFieldDefaults.OutlinedTextFieldDecorationBox(
            value = model.id.value,
            innerTextField = it,
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = { Text(text = "请输入") },
            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(5.dp,5.dp,5.dp,5.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(textColor = Color.Gray,
                containerColor = Color(220,220,220),
                unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent)
        )
    }
}