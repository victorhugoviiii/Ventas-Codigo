package com.achocallaromero.ventas.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.achocallaromero.ventas.R
import com.bumptech.glide.Glide

class RecyclerAdapter(
        val context: Context,
        val productos: ArrayList<Producto>
): RecyclerView.Adapter<ProductoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoHolder {
        val inflateView = LayoutInflater.from(context).inflate(R.layout.item_producto,parent,false)
        return ProductoHolder(inflateView)
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ProductoHolder, position: Int) {
        holder.nombre.text = productos[position].nombre
        holder.descripcion.text = productos[position].descripcion
        holder.precio.text = productos[position].precio.toString()
        holder.likes.text = productos[position].like.toString()
        holder.dislikes.text = productos[position].dislike.toString()
        //
        Glide.with(context).load("https://picsum.photos/200/300").into( holder.imagen );
        //Glide.with(context).load(productos[position].url_imagen).into( holder.imagen );
    }
}