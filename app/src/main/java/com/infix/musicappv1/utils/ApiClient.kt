package com.infix.musicappv1.utils

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.infix.musicappv1.BuildConfig.BASE_URL
import com.infix.musicappv1.data.source.local.db.MusicDatabase
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val TAG = "com.infix.musicappv1.utils.ApiClient"

    @Volatile
    private var okHttpClient: OkHttpClient? = null

    @Volatile
    private var auth: FirebaseAuth? = null

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var musicDb: MusicDatabase? = null

    private var onLogoutListener: OnLogoutListener? = null

    fun init(auth: FirebaseAuth, musicDb: MusicDatabase) {
        this.auth = auth
        this.musicDb = musicDb
    }

    fun setOnLogoutListener(listener: OnLogoutListener) {
        this.onLogoutListener = listener
    }

    @Throws(Exception::class)
    fun getOkHttpClient(): OkHttpClient {
        return okHttpClient ?: synchronized(this) {
            okHttpClient ?: OkHttpClient.Builder()
                .addInterceptor { chain ->
                    if(auth == null)
                        throw Exception("FirebaseAuth is null!")

                    val originalRequest = chain.request()
                    val currentUser = auth?.currentUser

                    if (currentUser != null) {
                        val idToken = fetchFirebaseIdToken(forceRefresh = false)

                        if (!idToken.isNullOrEmpty()) {
                            val authenticatedRequest = originalRequest.newBuilder()
                                .header("Authorization", "Bearer $idToken")
                                .build()
                            Log.d(TAG, "ApiClient ${authenticatedRequest.url()}")
                            return@addInterceptor chain.proceed(authenticatedRequest)
                        }
                    }
                    chain.proceed(originalRequest)
                }
                .authenticator { _, response ->
                    Log.d(TAG, "Received 401 from Cloud Function. Retrying with fresh Token...")

                    //avoid loop when refresh invalid
                    if (responseCount(response) >= 2) {
                        Log.e(TAG, "Max retry count reached. Performing Logout.")
                        handleLogout()
                        return@authenticator null
                    }

                    val freshIdToken = fetchFirebaseIdToken(forceRefresh = true)

                    if (!freshIdToken.isNullOrEmpty()) {
                        Log.d(TAG, "Refreshed Firebase ID Token successfully!")
                        return@authenticator response.request()
                            .newBuilder()
                            .header("Authorization", "Bearer $freshIdToken")
                            .build()
                    } else {
                        handleLogout()
                        return@authenticator null
                    }
                }
                .build().also { okHttpClient = it }
        }
    }

    fun getRetrofitClient(): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build().also { retrofit = it }
        }
    }

    private fun fetchFirebaseIdToken(forceRefresh: Boolean): String? {
        val user = auth?.currentUser ?: return null
        return try {
            val tokenTask = user.getIdToken(forceRefresh)
            val result = Tasks.await(tokenTask)
            result.token
        } catch (e: Exception) {
            Log.e(TAG, "ApiClient", e)
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var priorResponse = response.priorResponse()
        while (priorResponse != null) {
            count++
            priorResponse = priorResponse.priorResponse()
        }
        return count
    }

    private fun handleLogout() {
        auth?.signOut()

        onLogoutListener?.onLogout()
    }

    fun interface OnLogoutListener {
        fun onLogout()
    }
}