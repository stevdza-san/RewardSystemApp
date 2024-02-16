package com.stevdza_san.rewardsystemapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.stevdza_san.rewardsystemapp.domain.DataStoreOperations
import com.stevdza_san.rewardsystemapp.model.PersistedUser
import com.stevdza_san.rewardsystemapp.util.Constants.AD_CONSENT
import com.stevdza_san.rewardsystemapp.util.Constants.PREFERENCES_NAME
import com.stevdza_san.rewardsystemapp.util.Constants.USER_EMAIL
import com.stevdza_san.rewardsystemapp.util.Constants.USER_NAME
import com.stevdza_san.rewardsystemapp.util.Constants.USER_PICTURE
import com.stevdza_san.rewardsystemapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationsImpl(context: Context) : DataStoreOperations {

    private object PreferencesKey {
        val name = stringPreferencesKey(name = USER_NAME)
        val email = stringPreferencesKey(name = USER_EMAIL)
        val picture = stringPreferencesKey(name = USER_PICTURE)
        val adConsent = booleanPreferencesKey(name = AD_CONSENT)
    }

    private val dataStore = context.dataStore

    override suspend fun persistTheUser(persistedUser: PersistedUser): RequestState<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKey.name] = persistedUser.name
                preferences[PreferencesKey.email] = persistedUser.email
                preferences[PreferencesKey.picture] = persistedUser.picture
            }
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }

    override suspend fun saveAdConsentState(optIn: Boolean): RequestState<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[PreferencesKey.adConsent] = optIn
            }
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }

    override suspend fun clearUser(): RequestState<Unit> {
        return try {
            dataStore.edit {
                it.remove(PreferencesKey.name)
                it.remove(PreferencesKey.email)
                it.remove(PreferencesKey.picture)
            }
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }

    override fun readPersistedUser(): Flow<PersistedUser?> {
        return dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { preferences ->
                val name = preferences[PreferencesKey.name]
                val email = preferences[PreferencesKey.email]
                val picture = preferences[PreferencesKey.picture]
                if (name != null && email != null && picture != null) {
                    PersistedUser(
                        name = name,
                        email = email,
                        picture = picture
                    )
                } else null
            }
    }

    override fun readAdConsentState(): Flow<Boolean> {
        return dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { preferences ->
                val adConsent = preferences[PreferencesKey.adConsent] ?: false
                adConsent
            }
    }

}