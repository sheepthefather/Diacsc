package com.floatworld.diacsc.composes.mainCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.floatworld.diacsc.R

@Composable
fun NavgationBar(modifier:Modifier=Modifier,content:@Composable ()->Unit){
    Layout(content = content, modifier = modifier){measurables,constrains->
        val num=measurables.size
        val newConstranis=constrains.copy(maxWidth = constrains.maxWidth/num)
        val placeables=measurables.map {measurable ->
            measurable.measure(newConstranis)
        }
        val height=placeables.maxOf { it.height }

        layout(width = constrains.maxWidth, height = height){
            var x=0
            placeables.forEach {placeable ->
                placeable.placeRelative(x = x*(constrains.maxWidth/num)+
                        (constrains.maxWidth/num)/2-(placeable.width/2),
                    y=0)
                x++
            }
        }
    }
}

//@Preview
//@Composable
//fun test(){
//    Box(modifier = Modifier.size(350.dp, 800.dp)){
//        NavgationBar(modifier=Modifier.background(Color(255,255,255))){
//            NavgationItem(painter = painterResource(id = R.drawable.baseline_3p_24),
//                contentDescription = "", text = "test")
//            NavgationItem(painter = painterResource(id = R.drawable.baseline_3p_24),
//                contentDescription = "", text = "test")
//        }
//    }
//
//}


