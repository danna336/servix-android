package com.servix.app.ui.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val PublishPrimary = Color(0xFF6A1B9A)
private val PublishDark = Color(0xFF4A148C)
private val PublishBackground = Color(0xFFF5F2F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagPublicarTrabajo(
    nombreCliente: String = "Cliente",
    onVolver: () -> Unit = {},
    onTrabajoExitoso: () -> Unit = {}
) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Electricidad") }
    var zona by remember { mutableStateOf("Chapinero, Bogotá") }
    var isSubiendo by remember { mutableStateOf(false) }

    // Opciones rápidas de categorías y zonas
    val categoriasDisponibles = listOf("Electricidad", "Plomería", "Electrodomésticos", "Limpieza", "Carpintería")
    val zonasBogota = listOf("Chapinero", "Usaquén", "Suba", "Teusaquillo", "Centro / Santa Fe", "Engativá")

    var expandedCat by remember { mutableStateOf(false) }
    var expandedZona by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar nueva solicitud", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PublishPrimary)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PublishBackground)
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "¿Qué servicio necesitas hoy?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PublishDark
            )
            Text(
                text = "Completa los detalles para que los profesionales en Bogotá puedan postularse rápidamente.",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del trabajo (ej. Reparación de cortocircuito)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PublishPrimary,
                    focusedLabelColor = PublishPrimary
                )
            )

            ExposedDropdownMenuBox(
                expanded = expandedCat,
                onExpandedChange = { expandedCat = !expandedCat }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría del servicio") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedCat,
                    onDismissRequest = { expandedCat = false }
                ) {
                    categoriasDisponibles.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                expandedCat = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedZona,
                onExpandedChange = { expandedZona = !expandedZona }
            ) {
                OutlinedTextField(
                    value = zona,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Zona / Localidad en Bogotá") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedZona,
                    onDismissRequest = { expandedZona = false }
                ) {
                    zonasBogota.forEach { z ->
                        DropdownMenuItem(
                            text = { Text(z) },
                            onClick = {
                                zona = z
                                expandedZona = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción detallada del problema") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (titulo.isBlank() || descripcion.isBlank()) {
                        Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isSubiendo = true
                    val auth = FirebaseAuth.getInstance()
                    val db = FirebaseFirestore.getInstance()
                    val clienteUid = auth.currentUser?.uid ?: "anonimo"

                    val trabajoData = hashMapOf(
                        "titulo" to titulo,
                        "categoria" to categoria,
                        "zona" to zona,
                        "descripcion" to descripcion,
                        "clienteUid" to clienteUid,
                        "clienteNombre" to nombreCliente,
                        "estado" to "DISPONIBLE",
                        "creadoEn" to System.currentTimeMillis()
                    )

                    db.collection("trabajos")
                        .add(trabajoData)
                        .addOnSuccessListener {
                            isSubiendo = false
                            Toast.makeText(context, "¡Trabajo publicado con éxito!", Toast.LENGTH_SHORT).show()
                            onTrabajoExitoso() // Regresa a la vista principal
                        }
                        .addOnFailureListener { e ->
                            isSubiendo = false
                            Toast.makeText(context, "Error al publicar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PublishPrimary),
                enabled = !isSubiendo
            ) {
                if (isSubiendo) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "Publicar Solicitud", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}