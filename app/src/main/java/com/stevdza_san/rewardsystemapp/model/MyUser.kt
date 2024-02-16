package com.stevdza_san.rewardsystemapp.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

open class MyUser : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var ownerId: String = ""
    var name: String = ""
    var email: String = ""
    var picture: String = ""
    var coins: Int = 0
}