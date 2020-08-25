package com.utsman.hiyahiyahiya.utils.url_utils

import androidx.annotation.WorkerThread
import com.utsman.hiyahiyahiya.utils.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException
import java.util.*

object UrlFetcher {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getThumbnail(url: String?): UrlPreview? {
        try {
            val doc = Jsoup.connect(url).get()
            val element = doc.select("meta")

            forceGettingImage(element, doc)

            var subtitle = getMetaTag(doc, "description")
            if (subtitle?.isNotEmpty() == false) {
                subtitle = getMetaTag(doc, "og:description")
            }

            var newImg = getMetaTag(doc, "og:image")
            if (newImg.isNullOrEmpty()) {
                newImg = forceGettingImage(element, doc)
            }

            logi("img found -> $newImg")

            val title = doc.title()
            return UrlPreview(url, newImg, title, subtitle)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return null
        } catch (e: HttpStatusException) {
            e.printStackTrace()
            return null
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    private fun forceGettingImage(element: Elements, doc: Document): String? {
        val imageUrlElement = element.map { it.attr("content") }
        val imageUrl = imageUrlElement.find {
            it.toLowerCase(Locale.getDefault()).contains(".jpg") || it.toLowerCase(Locale.getDefault()).contains(".png")
        }
        if (imageUrl.isNullOrEmpty()) {
            val imageUrl2 = try {
                doc.select("img").first { !it.attr("src").contains("svg") }.attr("src")
            } catch (e: NullPointerException) {
                null
            }

            return if (imageUrl2?.isNotEmpty() == true) {
                imageUrl2
            } else {
                UrlUtil.extractUrl(imageUrlElement.toString())
            }
        }

        return null
    }

    private fun getMetaTag(document: Document, attr: String): String? {
        var elements: Elements = document.select("meta[name=$attr]")
        for (element in elements) {
            val s: String? = element.attr("content")
            if (s != null) return s
        }

        elements = document.select("meta[property=$attr]")
        for (element in elements) {
            val s: String? = element.attr("content")
            if (s != null && !s.contains("svg")) return s
        }

        return null
    }
}