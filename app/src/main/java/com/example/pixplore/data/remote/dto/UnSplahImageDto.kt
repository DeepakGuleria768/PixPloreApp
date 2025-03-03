package com.example.pixplore.data.remote.dto

//DTO stands for Data Transfer Object. It is a simple Java or Kotlin object (like a class)
// that is used to transfer data between different layers or parts of an application.

data class UnSplahImageDto(
    val id : String,
    val createdAt : String,
    val updatedAt : String,
    val width : Int,
    val height : Int
)


// This is what we get from the server
//[
//  {
//    "id": "LBI7cgq3pbM",
//    "created_at": "2016-05-03T11:00:28-04:00",
//    "updated_at": "2016-07-10T11:00:01-05:00",
//    "width": 5245,
//    "height": 3497
//  }
//  ]