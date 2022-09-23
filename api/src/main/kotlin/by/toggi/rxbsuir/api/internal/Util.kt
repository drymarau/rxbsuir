package by.toggi.rxbsuir.api.internal

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalCoroutinesApi::class)
internal suspend inline fun <R> Call.await(crossinline block: (Response) -> R): R =
    suspendCancellableCoroutine {
        it.invokeOnCancellation { cancel() }
        val callback = object : Callback {

            override fun onResponse(call: Call, response: Response) {
                try {
                    it.resume(response.use(block)) { response.close() }
                } catch (t: Throwable) {
                    it.resumeWithException(t)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                it.resumeWithException(e)
            }
        }
        enqueue(callback)
    }

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

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> Json.decodeFromResponseBody(body: ResponseBody): T =
    body.source().use(::decodeFromBufferedSource)

internal const val ApplicationJson = "application/json"
