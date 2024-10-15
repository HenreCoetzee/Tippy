package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalculatorScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TipCalculatorScreen(modifier: Modifier = Modifier) {
    var baseAmount by remember { mutableStateOf("") } // To store the base amount input
    var tipPercentage by remember { mutableStateOf(15.0) } // Default tip percentage
    var showSplitSlider by remember { mutableStateOf(false) } // Toggle slider visibility
    var numberOfPeople by remember { mutableStateOf(1) } // Default to 1 person for splitting

    // Function to calculate tip
    val calculateTip: (Double, Double) -> Double = { amount, tip ->
        (amount * tip) / 100
    }

    // Function to format numbers into RSA currency (R)
    fun formatCurrency(amount: Double): String {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA")) // RSA locale for Rand
        return currencyFormatter.format(amount)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // Increased spacing between fields
    ) {
        // Title for the app
        Text(
            text = "Tippy",
            fontSize = 30.sp, // Increased font size for the title
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp) // Spacing below the title
        )

        // Input for the base amount
        TextField(
            value = baseAmount,
            onValueChange = { baseAmount = it },
            label = { Text("Enter base amount") },
            modifier = Modifier.fillMaxWidth()
        )

        // Slider for tip percentage
        Text(text = "Tip Percentage: ${tipPercentage.toInt()}%", fontSize = 20.sp) // Increased font size
        Slider(
            value = tipPercentage.toFloat(),
            onValueChange = { tipPercentage = it.toDouble() },
            valueRange = 0f..30f, // Tip percentage range from 0% to 30%
            modifier = Modifier.fillMaxWidth()
        )

        // Calculating tip and total amount
        val baseAmountDouble = baseAmount.toDoubleOrNull() ?: 0.0
        val tip = calculateTip(baseAmountDouble, tipPercentage)
        val totalAmount = baseAmountDouble + tip

        // Display the calculated tip
        Text(text = "Tip: ${formatCurrency(tip)}", fontSize = 20.sp) // Increased font size

        // Display the final amount (base amount + tip)
        Text(text = "Final Amount: ${formatCurrency(totalAmount)}", fontSize = 20.sp) // Increased font size

        // Button to toggle the split slider
        Button(onClick = { showSplitSlider = !showSplitSlider }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Split Bill", fontSize = 20.sp) // Increased font size
        }

        if (showSplitSlider) {
            Spacer(modifier = Modifier.height(16.dp)) // Increased space between the button and the slider

            // Slider to choose number of people (1 to 10)
            Text(text = "Number of People: $numberOfPeople", fontSize = 20.sp) // Increased font size
            Slider(
                value = numberOfPeople.toFloat(),
                onValueChange = { numberOfPeople = it.toInt() },
                valueRange = 1f..10f,
                steps = 9, // 9 steps between 1 and 10
                modifier = Modifier.fillMaxWidth()
            )

            // Calculate and display the split amount
            val amountPerPerson = if (numberOfPeople > 0) totalAmount / numberOfPeople else totalAmount
            Text(text = "Amount per Person: ${formatCurrency(amountPerPerson)}", fontSize = 20.sp) // Increased font size
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipCalculatorPreview() {
    TipCalculatorTheme {
        TipCalculatorScreen()
    }
}


