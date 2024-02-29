package ca.josue_lubaki.vidtalk.data.api

import ca.josue_lubaki.vidtalk.domain.models.User

/**
 * created by Josue Lubaki
 * date : 2024-02-17
 * version : 1.0.0
 */


val data = listOf(
    User(
        email = "josuelubaki@gmail.com",
        password = "123456",
        profileURL = "https://ca.slack-edge.com/TRR87H161-U03ET6VHNSC-7c824e72f1ce-512"
    ),
    User(
        email = "jeremy@gmail.com",
        password = "123456",
        profileURL = "https://images.unsplash.com/photo-1556157382-97eda2d62296?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
)

fun findUser(email: String, password: String): User? {
    return data.find { it.email.lowercase() == email.lowercase() && it.password == password }
}