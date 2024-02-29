package ca.josue_lubaki.vidtalk.domain.models

data class User(
    val email: String,
    val password: String,
    val profileURL : String? = null
)