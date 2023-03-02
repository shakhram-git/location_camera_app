package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.PhotoItemBinding
import com.example.myapplication.entity.PhotoModel
import java.text.SimpleDateFormat
import java.util.*

class PhotosAdapter : ListAdapter<PhotoModel, PhotosAdapter.PhotoViewHolder>(DiffCallBack()) {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            Glide.with(photoContainer)
                .load(item.photoUri.toUri())
                .into(photoContainer)
            photoDate.text = formatter.format(item.date)
        }
    }

    class PhotoViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root)

    private class DiffCallBack : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
            oldItem.photoUri == newItem.photoUri && oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel) =
            oldItem.photoUri == newItem.photoUri
    }
}