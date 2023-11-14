package com.floatworld.diacsc.composes.connection
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomEdit(modifier: Modifier=Modifier,value:String,onChange:(String)->Unit){
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(modifier= modifier
        .padding(5.dp),value =value , onValueChange = onChange, textStyle = LocalTextStyle.current
    ){
        TextFieldDefaults.OutlinedTextFieldDecorationBox(
            value = value,
            innerTextField = it,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            placeholder = { Text(text = "请输入")},
            contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(5.dp,5.dp,5.dp,5.dp),
            colors = outlinedTextFieldColors(unfocusedBorderColor = Color.Transparent, focusedBorderColor = Color.Transparent)
        )
    }
}

//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(width = 300.dp, height = 500.dp)){
//        //CustomEdit(value = , onChange = )
//    }
//}