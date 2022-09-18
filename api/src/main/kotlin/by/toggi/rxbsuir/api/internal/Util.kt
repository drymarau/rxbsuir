package by.toggi.rxbsuir.api.internal

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
internal suspend fun Call.await(): Response = suspendCancellableCoroutine {
    it.invokeOnCancellation { cancel() }
    val callback = object : Callback {

        override fun onResponse(call: Call, response: Response) {
            it.resume(response) { response.close() }
        }

        override fun onFailure(call: Call, e: IOException) {
            it.resumeWithException(e)
        }
    }
    enqueue(callback)
}

internal suspend inline fun <R> Call.await(block: (Response) -> R) = await().use(block)

internal inline fun HttpUrl(block: HttpUrl.Builder.() -> Unit) =
    HttpUrl.Builder().apply(block).build()

internal inline fun HttpUrl(url: HttpUrl, block: HttpUrl.Builder.() -> Unit) =
    url.newBuilder().apply(block).build()

internal inline fun Request(block: Request.Builder.() -> Unit) =
    Request.Builder().apply(block).build()

internal inline fun Request.Builder.url(url: HttpUrl, block: HttpUrl.Builder.() -> Unit) =
    url(HttpUrl(url, block))

internal fun Request.Builder.accept(mimeType: String) = addHeader("Accept", mimeType)

internal inline fun OkHttpClient.newCall(block: Request.Builder.() -> Unit) =
    newCall(Request(block))

internal const val ApplicationJson = "application/json"
