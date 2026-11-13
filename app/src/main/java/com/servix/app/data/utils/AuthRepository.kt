package com.servix.app.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun registrarUsuario(
    email: String,
    pass: String,
    nombre: String,
    rol: String,
    onSuccess: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, pass)
        .addOnSuccessListener { authResult ->
            val userId = authResult.user?.uid ?: return@addOnSuccessListener

            val usuarioData = hashMapOf(
                "uid" to userId,
                "nombre" to nombre,
                "email" to email,
                "rol" to rol,
                "creadoEn" to System.currentTimeMillis()
            )

            db.collection("usuarios").document(userId)
                .set(usuarioData)
                .addOnSuccessListener { onSuccess(rol, nombre) }
                .addOnFailureListener { e -> onError(e.message ?: "Error al guardar el perfil") }
        }
        .addOnFailureListener { e -> onError(e.message ?: "Error al crear la cuenta") }
}

fun iniciarSesion(
    email: String,
    pass: String,
    rolSeleccionado: String,
    onSuccess: (String, String) -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.signInWithEmailAndPassword(email, pass)
        .addOnSuccessListener { authResult ->
            val userId = authResult.user?.uid
            if (userId == null) {
                onError("Error de autenticación")
                return@addOnSuccessListener
            }

            val updates = hashMapOf<String, Any>("rol" to rolSeleccionado)
            db.collection("usuarios").document(userId)
                .update(updates)
                .addOnSuccessListener {
                    db.collection("usuarios").document(userId).get()
                        .addOnSuccessListener { document ->
                            val nombreGuardado = document.getString("nombre") ?: "Usuario"
                            onSuccess(rolSeleccionado, nombreGuardado)
                        }
                        .addOnFailureListener {
                            onSuccess(rolSeleccionado, "Usuario")
                        }
                }
                .addOnFailureListener {
                    val usuarioData = hashMapOf(
                        "uid" to userId,
                        "email" to email,
                        "nombre" to "Usuario",
                        "rol" to rolSeleccionado,
                        "creadoEn" to System.currentTimeMillis()
                    )
                    db.collection("usuarios").document(userId).set(usuarioData)
                        .addOnSuccessListener { onSuccess(rolSeleccionado, "Usuario") }
                        .addOnFailureListener { onSuccess(rolSeleccionado, "Usuario") }
                }
        }
        .addOnFailureListener { e ->
            onError(e.message ?: "Correo o contraseña incorrectos")
        }
}