package com.servix.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val UserPurplePrimary = Color(0xFF6A1B9A)
private val UserPurpleDark = Color(0xFF4A148C)
private val UserBackground = Color(0xFFF5F2F9)
private val UserCardBg = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagPrincipalUsuario(
    nombreUsuario: String = "Usuario",
    onCambiarRolATrabajador: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var menuExpandido by remember { mutableStateOf(false) }
    var navegandoAPublicar by remember { mutableStateOf(false) }
    var trabajadorSeleccionadoParaVer: String? by remember { mutableStateOf(null) }

    when {
        navegandoAPublicar -> {
            PagPublicarTrabajo(
                nombreCliente = nombreUsuario,
                onVolver = { navegandoAPublicar = false },
                onTrabajoExitoso = { navegandoAPublicar = false }
            )
        }
        trabajadorSeleccionadoParaVer != null -> {
            PagPerfilTrabajadorDetalle(
                nombreTrabajador = trabajadorSeleccionadoParaVer ?: "Carlos Pérez",
                onVolver = { trabajadorSeleccionadoParaVer = null },
                onAbrirChat = { nombre ->
                    // TODO: Conectar con vista de chat en tiempo real
                }
            )
        }
        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "Hola, $nombreUsuario",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Buscando servicios en Bogotá",
                                    fontSize = 12.sp,
                                    color = Color(0xFFD1C4E9)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = UserPurplePrimary),
                        actions = {
                            // Notificaciones
                            IconButton(onClick = { /* TODO: Notificaciones */ }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notificaciones",
                                    tint = Color.White
                                )
                            }

                            Box {
                                IconButton(onClick = { menuExpandido = true }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Opciones",
                                        tint = Color.White
                                    )
                                }
                                DropdownMenu(
                                    expanded = menuExpandido,
                                    onDismissRequest = { menuExpandido = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Cambiar a modo Trabajador") },
                                        onClick = {
                                            menuExpandido = false
                                            onCambiarRolATrabajador()
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(
                                        text = { Text("Cerrar Sesión") },
                                        onClick = {
                                            menuExpandido = false
                                            onLogout()
                                        }
                                    )
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navegandoAPublicar = true },
                        containerColor = UserPurplePrimary,
                        contentColor = Color.White
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Publicar trabajo")
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(UserBackground)
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = UserPurpleDark),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "¿Necesitas ayuda en casa?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Publica una solicitud y conecta con profesionales calificados en Bogotá al instante.",
                                    fontSize = 13.sp,
                                    color = Color(0xFFD1C4E9)
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Profesionales destacados",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = UserPurpleDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(2) { index ->
                        val nombreTrabajador = if (index == 0) "Carlos Pérez" else "Mateo Rodríguez"
                        val oficio = if (index == 0) "Electricista Certificado" else "Técnico en Plomería"
                        val zona = if (index == 0) "Chapinero, Bogotá" else "Usaquén, Bogotá"

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = UserCardBg),
                            shape = RoundedCornerShape(14.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .clip(CircleShape)
                                            .background(UserBackground),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = nombreTrabajador.take(1),
                                            fontWeight = FontWeight.Bold,
                                            color = UserPurplePrimary,
                                            fontSize = 18.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = nombreTrabajador,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = oficio,
                                            fontSize = 13.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Surface(
                                        color = Color(0xFFFFF8E1),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = "Calificación",
                                                tint = Color(0xFFFFC107),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "4.9",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.DarkGray
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Zona: $zona",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = { trabajadorSeleccionadoParaVer = nombreTrabajador },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = UserPurplePrimary)
                                    ) {
                                        Text("Ver Perfil", fontWeight = FontWeight.Bold)
                                    }
                                    OutlinedButton(
                                        onClick = { /* TODO: Abrir chat directo */ },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = UserPurplePrimary)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Chat",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}