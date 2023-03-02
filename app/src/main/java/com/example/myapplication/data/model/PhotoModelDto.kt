package com.example.myapplication.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.entity.PhotoModel

@Entity(tableName = "photos")
data class PhotoModelDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "photo_uri")
    override val photoUri: String,
    override val date: Long
) : PhotoModel{
    companion object{
        fun fromPhotoModel(photoModel: PhotoModel): PhotoModelDto {
            return PhotoModelDto(
                photoUri = photoModel.photoUri,
                date = photoModel.date
            )
        }
    }
}
