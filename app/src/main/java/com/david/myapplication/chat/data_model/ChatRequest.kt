package com.david.myapplication.chat.data_model


class ChatRequest(val id:String,
                  val text:String,
                  val fromId:String,
                  val toId:String,
                  val timestamp:Long){
    constructor() : this("","","","",-1) }