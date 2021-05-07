package com.achocallaromero.ventas.recycler

data class Producto(
        var codigo: String,
        var nombre: String,
        var descripcion: String,
        var precio: Double,
        var url_imagen: String,
        var like: Int,
        var dislike: Int
) {
}