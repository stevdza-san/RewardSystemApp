package com.stevdza_san.rewardsystemapp.data

import com.stevdza_san.rewardsystemapp.domain.MongoRepository
import com.stevdza_san.rewardsystemapp.model.MyUser
import com.stevdza_san.rewardsystemapp.util.Constants.APP_ID
import com.stevdza_san.rewardsystemapp.util.RequestState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.log.RealmLog
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.DownloadingRealmTimeOutException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.seconds

object MongoDB : MongoRepository {
    private var app: App? = null
    private var mongoUser: User? = null
    private lateinit var realm: Realm

    override fun configureTheRealm(): RequestState<Unit> {
        app = App.create(APP_ID)
        mongoUser = app?.currentUser
        return if (mongoUser != null) {
            try {
                val config = SyncConfiguration.Builder(
                    mongoUser!!,
                    setOf(MyUser::class)
                )
                    .waitForInitialRemoteData(60.seconds)
                    .initialSubscriptions { sub ->
                        add(
                            query = sub.query<MyUser>(query = "ownerId == $0", mongoUser!!.id),
                            name = "MyUser's subscription"
                        )
                    }
                    .build()
                realm = Realm.open(config)
                RealmLog.level = LogLevel.ALL
                RequestState.Success(data = Unit)
            } catch (e: DownloadingRealmTimeOutException) {
                RequestState.Error(message = "Failed to download the data. Check the internet connection.")
            } catch (e: Exception) {
                RequestState.Error(message = e.message.toString())
            }
        } else {
            RequestState.Error(message = "MongoDB User is null.")
        }
    }

    override fun getUser(): Flow<RequestState<MyUser>> {
        return if (mongoUser != null) {
            try {
                realm.query<MyUser>(query = "ownerId == $0", mongoUser!!.id)
                    .find()
                    .asFlow()
                    .catch { flow { emit(RequestState.Error("${it.message}")) } }
                    .map {
                        val result = it.list.firstOrNull()
                        if (result != null) {
                            RequestState.Success(data = result)
                        } else {
                            RequestState.Error(
                                message = "User doesn't exist. " +
                                        "Report this issue."
                            )
                        }
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error("${e.message}")) }
            }
        } else {
            flow { emit(RequestState.Error("MongoDB User is null.")) }
        }
    }

    override suspend fun reset() {
        App.create(APP_ID).currentUser?.logOut()
        App.create(APP_ID).currentUser?.remove()
        realm.close()
        app = null
        mongoUser = null
    }
}