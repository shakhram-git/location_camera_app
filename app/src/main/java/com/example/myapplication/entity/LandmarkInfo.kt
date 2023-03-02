package com.example.myapplication.entity

interface LandmarkInfo {
    val xid: String
    val name: String
    val city: String?
    val country: String
    val street: String?
    val houseNumber: String?
    val imageSource: String?
    val text: String
}