package by.toggi.rxbsuir

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class RxBsuirApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var okHttpClient: Provider<OkHttpClient>

    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .okHttpClient(okHttpClient::get)
        .diskCache {
            DiskCache.Builder()
                .directory(cacheDir.resolve("image_cache"))
                .maxSizeBytes(50 * 1024 * 1024)
                .build()
        }
        .respectCacheHeaders(!BuildConfig.DEBUG)
        .build()
}
