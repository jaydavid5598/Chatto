package com.david.myapplication.model.user_model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid:String ,val username:String, val profileImageUrl:String,val password:String):Parcelable{
    constructor():this("","","","")
}