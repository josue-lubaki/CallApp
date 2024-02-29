package ca.josue_lubaki.vidtalk.data.datasourceimpl
import ca.josue_lubaki.vidtalk.data.api.findUser
import ca.josue_lubaki.vidtalk.data.datasource.LoginRemoteDataSource
import ca.josue_lubaki.vidtalk.domain.models.LoginStatus
import ca.josue_lubaki.vidtalk.utils.config.StreamVideoInitHelper
import io.getstream.video.android.datastore.delegate.StreamUserDataStore
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import javax.inject.Inject
import ca.josue_lubaki.vidtalk.domain.models.User as UserModel

internal class LoginRemoteDataSourceImpl @Inject constructor(
    private val dataStore : StreamUserDataStore
) : LoginRemoteDataSource {

    override suspend fun login(user: UserModel): LoginStatus {
        return try {
            val userLogged = findUser(
                email = user.email,
                password = user.password
            )

            if (userLogged != null) {
                val newUser = User(
                    id = userLogged.email,
                    name = userLogged.email,
                    role = "user",
                    image = userLogged.profileURL.orEmpty(),
                    type = UserType.Authenticated
                )
                dataStore.updateUser(newUser)
                StreamVideoInitHelper.loadSdk(dataStore)

                LoginStatus.Success
            } else {
                LoginStatus.Error("An error occurred")
            }
        } catch (e: Exception) {
            LoginStatus.Error(e.message ?: "An error occurred")
        }
    }
}