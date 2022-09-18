package by.toggi.rxbsuir.api

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

inline fun MockWebServer.enqueue(block: MockResponse.() -> Unit) =
    enqueue(MockResponse(block))

inline fun MockResponse(block: MockResponse.() -> Unit) = MockResponse().apply(block)

inline fun MockWebServer.takeRequest(block: (RecordedRequest) -> Unit) = block(takeRequest())
