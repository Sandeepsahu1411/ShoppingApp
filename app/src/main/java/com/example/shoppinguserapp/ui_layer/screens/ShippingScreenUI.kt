package com.example.shoppinguserapp.ui_layer.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.shoppinguserapp.R
import com.example.shoppinguserapp.country_api.CountryResponse
import com.example.shoppinguserapp.country_api.RetrofitInstance
import com.example.shoppinguserapp.domen_layer.data_model.CartModel
import com.example.shoppinguserapp.domen_layer.data_model.Products
import com.example.shoppinguserapp.ui_layer.navigation.Routes
import com.example.shoppinguserapp.ui_layer.viewmodel.AppViewModel

@Composable
fun ShippingScreenUI(
    navController: NavController,
    productId: String,
    productSize: String,
    productColor: String,
    productQty: String,
    viewModel: AppViewModel = hiltViewModel(),
) {

    val getCartState = viewModel.getCartState.collectAsStateWithLifecycle()
    val getCartData = getCartState.value.success

    val getProductByIdState = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val productData = getProductByIdState.value.success


    LaunchedEffect(productId) {
        viewModel.getProductsCart()
        if (productId.isNotEmpty()) {
            viewModel.getProductById(productId)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Shipping",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black

        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.clickable {
                navController.navigateUp()
            }) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                Modifier.size(15.dp)
            )
            Text(
                text = if (productId.isNotEmpty()) "Back to Product" else "Return to cart",
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        when {
            getCartState.value.isLoading || getProductByIdState.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            getCartState.value.error != null || getProductByIdState.value.error.isNotEmpty() -> {
                Toast.makeText(
                    LocalContext.current,
                    "Error: ${getCartState.value.error ?: getProductByIdState.value.error}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            getCartState.value.success.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        if (productId.isNotEmpty()) {
                            EachBuyProductDetail(
                                productSize,
                                productColor,
                                productQty,
                                productData
                            )
                        } else {
                            CartProductDetail(getCartData)
                        }
                    }

                    item {
                        ContactInformation()
                    }
                    item {
                        var selectedCountry by remember { mutableStateOf("") }
                        var selectedFlag by remember { mutableStateOf<String?>(null) }
                        CountryDropdownWithApi(
                            selectedCountry = selectedCountry,
                            selectedFlag = selectedFlag,
                            onCountrySelected = { country, flag ->
                                selectedCountry = country
                                selectedFlag = flag
                            }
                        )
                        ShippingAddress(viewModel)
                    }
                    item {
                        var selectedMethod by remember { mutableStateOf("Free") }
                        ShippingMethod(
                            selectedMethod = selectedMethod,
                            onMethodSelected = { selectedMethod = it })
                    }
                    item {
                        Button(
                            onClick = {
                                navController.navigate(Routes.PaymentScreen)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8c8585), contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Continue to Shipping",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun CartProductDetail(
    getCartData: List<CartModel>
) {
    val subTotal = getCartData.sumOf { it.finalPrice.toInt() * it.qty.toInt() }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {


        Column {
            getCartData.forEach { cartData ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SubcomposeAsyncImage(model = if (cartData.imageUrl.isNotEmpty()) cartData.imageUrl else R.drawable.product_frock,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp, 70.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        loading = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.LightGray)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.Center),
                                    color = Color.Red
                                )
                            }
                        })
                    Column(
                        modifier = Modifier.weight(0.7f),
                    ) {
                        Text(
                            text = cartData.name,
                            fontSize = 18.sp,
                            color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 15.sp
                        )
                        Text(
                            text = "Size : ${cartData.size}",
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Color : ",
                                fontSize = 16.sp,
                                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                            )
                            Box(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(5.dp))
                                    .size(20.dp)
                                    .background(hexToColor(cartData.color))
                            )
                        }


                    }

                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        Text(
                            text = "Rs: ${cartData.finalPrice}",
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                            fontWeight = FontWeight.Bold,

                            )
                        Text(
                            text = "Qty: ${cartData.qty}",
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        HorizontalDivider(thickness = 2.dp)
        Row {
            Text(text = "Sub Total", fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text(
                text = "Rs: $subTotal",
                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column {
            Row {
                Text(text = "Shipping Charge", fontSize = 18.sp, modifier = Modifier.weight(1f))
                Text(
                    text = if (subTotal > 3000) "Free" else "Rs: 100",
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Free shipping over ₹3000",
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color(0xFFF68B8B),
            )

        }
        HorizontalDivider(thickness = 2.dp)
        Row {
            Text(
                text = "Total",
                fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (subTotal > 3000) "Rs: $subTotal" else "Rs: ${subTotal + 100}",
                color = Color(0xFFf68b8b),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        HorizontalDivider(
            thickness = 1.dp, color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun EachBuyProductDetail(
    productSize: String,
    productColor: String,
    productQty: String,
    data: Products?
) {


    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        val subTotal = (data?.finalPrice?.toIntOrNull() ?: 0) * productQty.toInt()

        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SubcomposeAsyncImage(model = if (data?.image?.isEmpty() != true) data?.image else R.drawable.product_frock,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp, 70.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center),
                                color = Color.Red
                            )
                        }
                    })
                Column(
                    modifier = Modifier.weight(0.7f),
                ) {
                    Text(
                        text = data?.name ?: "NA",
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = "Size : $productSize",
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Color : ",
                            fontSize = 16.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        )
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(5.dp))
                                .size(20.dp)
                                .background(hexToColor(productColor))
                        )
                    }


                }

                Column(
                    modifier = Modifier.weight(0.3f),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "Rs: ${data?.finalPrice}",
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        fontWeight = FontWeight.Bold,

                        )
                    Text(
                        text = "Qty: $productQty",
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
            Spacer(modifier = Modifier.height(10.dp))

        }
        HorizontalDivider(thickness = 2.dp)
        Row {
            Text(text = "Sub Total", fontSize = 18.sp, modifier = Modifier.weight(1f))
            Text(
                text = "Rs: $subTotal",
                color = if (isSystemInDarkTheme()) Color.White else Color.DarkGray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column {
            Row {
                Text(
                    text = "Shipping Charge",
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (subTotal > 3000) "Free" else "Rs: 100",
                    color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Free shipping over ₹3000",
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color(0xFFF68B8B),
            )

        }
        HorizontalDivider(thickness = 2.dp)
        Row {
            Text(
                text = "Total",
                fontSize = 20.sp,
                color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.DarkGray,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (subTotal > 3000) "Rs: $subTotal" else "Rs: ${subTotal + 100}",
                color = Color(0xFFf68b8b),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}


@Composable
fun ContactInformation() {
    var email by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(modifier = Modifier.padding(bottom = 10.dp)) {
            Text(
                text = "Contact Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),
                modifier = Modifier.weight(1f)
            )
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp, fontFamily = FontFamily.SansSerif
                    )
                ) {
                    append("Already have an account? ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Gray

                    )
                ) {
                    append("Login")
                }

            })
        }
        CustomOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholderText = "Email",
            Modifier.fillMaxWidth()
        )


    }
}

@Composable
fun ShippingAddress(viewModel: AppViewModel) {

    val addShippingAddress = viewModel.addShippingState.collectAsStateWithLifecycle()
    val getShippingAddress = viewModel.getShippingState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    when {
        addShippingAddress.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        addShippingAddress.value.error != null -> {
            Toast.makeText(context, addShippingAddress.value.error, Toast.LENGTH_SHORT).show()
        }

        addShippingAddress.value.success != null -> {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = "Shipping Address",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            CustomOutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholderText = "First Name",
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next

            )
            CustomOutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholderText = "Last Name",
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next
            )
        }
        CustomOutlinedTextField(
            value = address,
            onValueChange = { address = it },
            placeholderText = "Address",
            Modifier.fillMaxWidth(),
            imeAction = ImeAction.Next

        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            CustomOutlinedTextField(
                value = city,
                onValueChange = { city = it },
                placeholderText = "City",
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next
            )
            CustomOutlinedTextField(
                value = pinCode,
                onValueChange = { pinCode = it },
                placeholderText = "Pin Code",
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Next
            )
        }
        CustomOutlinedTextField(
            value = contactNumber,
            onValueChange = {
                if (it.all { char -> char.isDigit() } && it.length <= 10) {
                    contactNumber = it
                } else {
                    Toast.makeText(context, "Bas 10 No. ho gye", Toast.LENGTH_SHORT).show()
                }
            },
            placeholderText = "Contact Number",
            Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Checkbox(onCheckedChange = { true }, checked = false)
            Text(
                text = "Save this information for next time",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Gray,

                )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                Row {
                    Text(text = "Contact", fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Change",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(thickness = 2.dp)
                Row {
                    Text(text = "Ship to", fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Change",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


            }
        }


    }

}

@Composable
fun ShippingMethod(selectedMethod: String, onMethodSelected: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Shipping Method",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color(0xFF5c5757),
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFF68B8B), shape = RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("Free") }) {
                    RadioButton(
                        selected = selectedMethod == "Free",
                        onClick = { onMethodSelected("Free") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Standard FREE delivery over Rs:4500",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.85f)

                    )
                    Text(
                        text = "Free",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.15f),
                        textAlign = TextAlign.End

                    )
                }
                HorizontalDivider(thickness = 2.dp)
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onMethodSelected("COD") }) {
                    RadioButton(
                        selected = selectedMethod == "COD",
                        onClick = { onMethodSelected("COD") },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF68B8B))
                    )
                    Text(
                        text = "Cash on delivery over Rs:4500 (Free Delivery, COD processing fee only)",
                        fontSize = 14.sp,
                        modifier = Modifier.weight(0.85f)
                    )

                    Text(
                        text = "100",
                        color = if (isSystemInDarkTheme()) Color(0xFFF68B8B) else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.15f),
                        textAlign = TextAlign.End

                    )
                }


            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdownWithApi(
    selectedCountry: String, selectedFlag: String?, onCountrySelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var countries by remember { mutableStateOf<List<CountryResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    // Fetch countries and sort A-Z
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            countries = RetrofitInstance.api.getAllCountries().sortedBy { it.name.common }
        } catch (e: Exception) {
            e.printStackTrace() // Handle error
        } finally {
            isLoading = false
        }
    }


    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd // Align dropdown to the right
    ) {
        // Dropdown Field with Selected Flag and Name
        OutlinedTextField(
            value = selectedCountry,
            onValueChange = {},
            label = { Text("Country / Region") },
            readOnly = true,
            leadingIcon = {
                if (selectedFlag != null) {
                    AsyncImage(
                        model = selectedFlag,
                        contentDescription = "Selected Country Flag",
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Dropdown",
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Gray,
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (!expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Dropdown",
                        tint = Color(0xFFF68B8B)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFFF68B8B),
                unfocusedIndicatorColor = Color(0xFFF68B8B),
            )

        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(250.dp) // Restrict width
                .heightIn(max = 300.dp) // Restrict height
        ) {
            if (isLoading) {
                DropdownMenuItem(text = { Text("Loading...") }, onClick = {})
            } else {
                countries.forEach { country ->
                    DropdownMenuItem(text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = country.flags.png,
                                contentDescription = "${country.name.common} Flag",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(country.name.common)
                        }
                    }, onClick = {
                        onCountrySelected(country.name.common, country.flags.png)
                        expanded = false
                    })
                }
            }
        }
    }
}


//@Composable
//fun CountryDropdown(selectedCountry: String, onCountrySelected: (String) -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//
//    // Dropdown Field
//    Box(modifier = Modifier.fillMaxWidth()) {
//        OutlinedTextField(
//            value = selectedCountry,
//            onValueChange = {},
//            label = { Text("Country / Region") },
//            readOnly = true,
//            trailingIcon = {
//                IconButton(onClick = { expanded = !expanded }) {
//                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Dropdown Menu with Country List
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            countryList.forEach { country ->
//                DropdownMenuItem(
//                    text = {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            AsyncImage(
//                                model = country.flagUrl,
//                                contentDescription = "${country.name} Flag",
//                                modifier = Modifier
//                                    .size(24.dp)
//                                    .padding(end = 8.dp)
//                            )
//                            Text(country.name)
//                        }
//                    },
//                    onClick = {
//                        onCountrySelected(country.name)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}
//
//
//data class Country(val name: String, val flagUrl: String)
//
//val countryList = listOf(
//    Country("South Africa", "https://flagcdn.com/za.png"),
//    Country("India", "https://flagcdn.com/in.png"),
//    Country("United States", "https://flagcdn.com/us.png"),
//    Country("Canada", "https://flagcdn.com/ca.png"),
//    Country("United Kingdom", "https://flagcdn.com/gb.png"),
//    Country("Australia", "https://flagcdn.com/au.png")
//    // Add more countries as needed
//)
