package com.servix.app.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.servix.app.utils.iniciarSesion
import com.servix.app.utils.registrarUsuario

private val LoginPurplePrimary = Color(0xFF6A1B9A)
private val LoginPurpleDark = Color(0xFF4A148C)
private val LoginBackgroundColor = Color(0xFFF5F2F9)

@Composable
fun PagLogin(
    onLoginSuccess: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var esRegistro by remember { mutableStateOf(false) }
    var rolSeleccionado by remember { mutableStateOf("CLIENTE") }
    var isCargando by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LoginBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (esRegistro) "Registro en Servix" else "Bienvenido a Servix",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = LoginPurpleDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (esRegistro) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { rolSeleccionado = "CLIENTE" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == "CLIENTE") LoginPurplePrimary else Color.Gray
                    )
                ) {
                    Text("Cliente")
                }
                Button(
                    onClick = { rolSeleccionado = "TRABAJADOR" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (rolSeleccionado == "TRABAJADOR") LoginPurplePrimary else Color.Gray
                    )
                ) {
                    Text("Trabajador")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Por favor llena los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isCargando = true

                    if (esRegistro) {
                        if (nombre.isBlank()) {
                            Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                            isCargando = false
                            return@Button
                        }
                        registrarUsuario(
                            email = email,
                            pass = password,
                            nombre = nombre,
                            rol = rolSeleccionado,
                            onSuccess = { rol, nom ->
                                isCargando = false
                                Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(rol, nom) // Envía rol y nombre al MainActivity
                            },
                            onError = { error ->
                                isCargando = false
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    } else {
                        iniciarSesion(
                            email = email,
                            pass = password,
                            rolSeleccionado = rolSeleccionado,
                            onSuccess = { rol, nom ->
                                isCargando = false
                                Toast.makeText(context, "Bienvenido, $nom", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(rol, nom)
                            },
                            onError = { error ->
                                isCargando = false
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LoginPurplePrimary),
                enabled = !isCargando
            ) {
                if (isCargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = if (esRegistro) "Registrarse" else "Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = { esRegistro = !esRegistro }) {
                Text(
                    text = if (esRegistro) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate",
                    color = LoginPurplePrimary
                )
            }
        }
    }
}