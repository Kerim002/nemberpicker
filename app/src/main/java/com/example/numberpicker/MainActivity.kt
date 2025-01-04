package com.example.numberpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberpicker.ui.theme.NumberpickerTheme
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumberpickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NumberPickerDemo()
                    }
                }
            }
        }
    }
}
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun NumberPicker(
    minValue: Int,
    maxValue: Int,
    onValueChange: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    var selectedValue by remember { mutableStateOf(minValue) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerIndex = listState.firstVisibleItemIndex + 2 // Adjust for center alignment
            selectedValue = centerIndex.coerceIn(minValue, maxValue)
            onValueChange(selectedValue)
        }
    }

    Box(
        modifier = Modifier
            .height(150.dp)
            .width(120.dp)
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snapFlingBehavior, // Enables snapping behavior
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed((minValue..maxValue).toList()) { index, number ->
                Column {
                    Text(
                        text = number.toString(),
                        fontSize = if(number == selectedValue) 24.sp else 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            ,
                        color = if(number == selectedValue)Color.Black else Color.LightGray)
                }
            }
        }


    }
}

@Composable
fun NumberPickerDemo() {
    var pickedNumber by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Selected Number: $pickedNumber", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        NumberPicker(minValue = 0, maxValue = 20) { number ->
            pickedNumber = number
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberPickerPreview() {
    NumberpickerTheme {
        NumberPickerDemo()
    }
}
