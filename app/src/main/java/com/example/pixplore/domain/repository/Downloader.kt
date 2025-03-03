package com.example.pixplore.domain.repository

interface Downloader {


    fun downlaodFile(url:String,fileName:String?)
}