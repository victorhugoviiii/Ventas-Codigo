package com.achocallaromero.ventas.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.achocallaromero.ventas.R

class ProductoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var imagen: ImageView = itemView.findViewById(R.id.imagen)
    var nombre: TextView = itemView.findViewById(R.id.nombre)
    var descripcion: TextView = itemView.findViewById(R.id.descripcion)
    var precio: TextView = itemView.findViewById(R.id.precio)
    var likes: TextView = itemView.findViewById(R.id.likes)
    var dislikes: TextView = itemView.findViewById(R.id.dislikes)

}