package pro.lj.roomer.data

import java.io.Serializable

data class User(
        val name : String = "NULL",
        val number : String = "NULL",
        val email : String = "NULL"
) : Serializable