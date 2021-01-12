package com.example.jetpackcomposedemo.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.annotatedString
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.jetpackcomposedemo.R

@Composable
fun LoginScreen(openDashboard: () -> Unit) {
    JetPackComposeDemoTheme {
        ScrollableColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout {
                val (image, loginForm) = createRefs()
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.height(280.dp).constrainAs(image) {
                        top.linkTo(loginForm.top)
                        bottom.linkTo(loginForm.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    HeaderView()
                }
                Card(
                    shape = RoundedCornerShape(topLeft = 40.dp, topRight = 40.dp),
                    backgroundColor = ghost_white,
                    modifier = Modifier.fillMaxSize().padding(top = 100.dp)
                        .constrainAs(loginForm) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(30.dp)
                    ) {

                        val loginText = "Log in to your account."
                        val loginWord = "Log in"
                        val loginAnnotatedString = annotatedString {
                            append(loginText)
                            addStyle(
                                style = SpanStyle(
                                    color = dark_gray,
                                    fontFamily = fontFamily(font(R.font.helvetica_neue_regular))
                                ),
                                start = 0,
                                end = loginText.length
                            )
                            addStyle(
                                style = SpanStyle(
                                    color = colorPrimary,
                                    fontFamily = fontFamily(font(R.font.helvetica_neue_medium))
                                ),
                                start = 0,
                                end = loginWord.length
                            )
                        }

                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 10.dp, bottom = 20.dp),
                            text = loginAnnotatedString,
                            textAlign = TextAlign.Center,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Email Address",
                            style = MaterialTheme.typography.subtitle1.copy(color = gray),
                            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
                        )

                        CustomStyleTextField(
                            "Email Address",
                            R.drawable.ic_email,
                            KeyboardType.Email,
                            VisualTransformation.None
                        )

                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.subtitle1.copy(color = gray),
                            modifier = Modifier.padding(bottom = 10.dp, top = 20.dp)
                        )
                        CustomStyleTextField(
                            "Password",
                            R.drawable.ic_password,
                            KeyboardType.Password,
                            PasswordVisualTransformation()
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            text = "Forgot Password",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.subtitle2.copy(color = colorPrimary)
                        )
                        Button(
                            onClick = openDashboard,
                            modifier = Modifier.padding(top = 30.dp, bottom = 34.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                text = "Login",
                                color = Color.White,
                                style = MaterialTheme.typography.button
                            )
                        }

                        val signInText = "Don't have an account? Sign In"
                        val signInWord = "Sign In"
                        val signInAnnotatedString = annotatedString {
                            append(signInText)
                            addStyle(
                                style = SpanStyle(
                                    color = light_gray,
                                    fontFamily = fontFamily(font(R.font.helvetica_neue_regular))
                                ),
                                start = 0,
                                end = signInText.length
                            )
                            addStyle(
                                style = SpanStyle(
                                    color = colorPrimary,
                                    fontFamily = fontFamily(font(R.font.helvetica_neue_medium))
                                ),
                                start = signInText.indexOf(signInWord),
                                end = signInText.length
                            )
                        }

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = signInAnnotatedString,
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomStyleTextField(
    placeHolder: String,
    leadingIconId: Int,
    keyboardType: KeyboardType,
    visualTransformation: VisualTransformation
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        value = textState.value,
        onValueChange = { valueChanged ->
            textState.value = valueChanged
        },
        placeholder = { Text(text = placeHolder) },
        leadingIcon = {
            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Image(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp).size(18.dp),
                        bitmap = imageResource(id = leadingIconId),  // material icon
                        colorFilter = ColorFilter.tint(colorPrimary),
                    )
                    Canvas(
                        modifier = Modifier.preferredHeight(24.dp)
                    ) {
                        // Allows you to draw a line between two points (p1 & p2) on the canvas.
                        drawLine(
                            color = Color.LightGray,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 2.0F
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
        activeColor = colorPrimary,
        inactiveColor = Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        visualTransformation = visualTransformation
    )
    /* TextField(
         value = textState.value,
         onValueChange = { valueChanged ->
             textState.value = valueChanged
         },
         modifier = Modifier.fillMaxSize(),
         placeholder = Text(text = placeHolder, style = TextStyle(color = text_hint_color)),
         leadingIcon = Row(
             modifier = Modifier.fillMaxSize(),
             verticalAlignment = Alignment.CenterVertically,
             content = {
                 Image(
                     modifier = Modifier.padding(start = 10.dp, end = 10.dp).size(18.dp),
                     bitmap = imageResource(id = leadingIconId),  // material icon
                     colorFilter = ColorFilter.tint(colorPrimary),
                 )
                 Canvas(
                     modifier = Modifier.preferredHeight(24.dp)
                 ) {
                     // Allows you to draw a line between two points (p1 & p2) on the canvas.
                     drawLine(
                         color = Color.LightGray,
                         start = Offset(0f, 0f),
                         end = Offset(0f, size.height),
                         strokeWidth = 2.0F
                     )
                 }
             }
         ),
         activeColor = colorPrimary,
         inactiveColor = Color.Transparent,
         shape = RoundedCornerShape(10.dp),
         backgroundColor = Color.White,
         textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
         keyboardType = keyboardType,
         visualTransformation = visualTransformation
     )*/
}


@Composable
fun HeaderView() {
    Image(
        modifier = Modifier.fillMaxSize(),
        bitmap = imageResource(id = R.drawable.login_bg),
        contentScale = ContentScale.FillWidth
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Image(
            modifier = Modifier.wrapContentWidth(),
            bitmap = imageResource(id = R.drawable.flower_logo),
        )
        Text(
            text = "FloraGoGo",
            color = Color.White,
            style = TextStyle(
                fontSize = 40.sp,
                fontFamily = fontFamily(font(R.font.josefin_sans_semibold_italic)),
                letterSpacing = 2.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginScreen(openDashboard = {})
}