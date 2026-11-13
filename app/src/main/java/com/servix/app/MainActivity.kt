package com.servix.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.servix.app.ui.auth.PagLogin
import com.servix.app.ui.home.PagPrincipalUsuario
import com.servix.app.ui.home.PagPrincipalTrabajador

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var sesionIniciada by remember { mutableStateOf(false) }
            var rolActual by remember { mutableStateOf("CLIENTE") }
            var nombreActual by remember { mutableStateOf("Usuario") }

            if (!sesionIniciada) {
                PagLogin(
                    onLoginSuccess = { rolObtenido, nombreObtenido ->
                        rolActual = rolObtenido
                        nombreActual = nombreObtenido
                        sesionIniciada = true
                    }
                )
            } else {
                if (rolActual == "TRABAJADOR") {
                    PagPrincipalTrabajador(
                        nombreTrabajador = nombreActual,
                        onCambiarRolACliente = {
                            rolActual = "CLIENTE"
                        },
                        onLogout = {
                            sesionIniciada = false
                        }
                    )
                } else {
                    PagPrincipalUsuario(
                        nombreUsuario = nombreActual,
                        onCambiarRolATrabajador = {
                            rolActual = "TRABAJADOR"
                        },
                        onLogout = {
                            sesionIniciada = false
                        }
                    )
                }
            }
        }
    }
}