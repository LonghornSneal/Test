package com.cosmobond.watchface

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.xmlpull.v1.XmlPullParser
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class WatchFaceFormatV2Test {
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun watchFaceXmlDefinesAnalogSceneAndSlots() {
        val document = parseXmlResource(R.raw.watchface)
        val root = document.documentElement
        assertEquals("WatchFace", root.nodeName)
        assertEquals("480", root.getAttribute("width"))
        assertEquals("480", root.getAttribute("height"))

        val analogClocks = document.getElementsByTagName("AnalogClock")
        assertTrue("AnalogClock node missing", analogClocks.length >= 1)

        val complicationSlots = document.getElementsByTagName("ComplicationSlot")
        assertEquals("Unexpected complication slot count", 4, complicationSlots.length)
    }

    @Test
    fun configurationsExposePaletteAndSecondsToggle() {
        val document = parseXmlResource(R.raw.watchface)
        val colorConfigs = document.getElementsByTagName("ColorConfiguration")
        assertEquals(1, colorConfigs.length)
        val colorConfig = colorConfigs.item(0) as Element
        assertEquals("accentPalette", colorConfig.getAttribute("id"))

        val colorOptions = colorConfig.getElementsByTagName("ColorOption")
        assertEquals(3, colorOptions.length)

        val booleanConfigs = document.getElementsByTagName("BooleanConfiguration")
        assertEquals(1, booleanConfigs.length)
        val secondsToggle = booleanConfigs.item(0) as Element
        assertEquals("showSecondHand", secondsToggle.getAttribute("id"))
    }

    @Test
    fun watchFaceInfoMetadataFlagsPresent() {
        val parser = context.resources.getXml(R.xml.watch_face_info)
        var preview: String? = null
        var category: String? = null
        var available: String? = null
        var multiple: String? = null
        var editable: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG) {
                when (parser.name) {
                    "Preview" -> preview = parser.getAttributeValue(null, "value")
                    "Category" -> category = parser.getAttributeValue(null, "value")
                    "AvailableInRetail" -> available = parser.getAttributeValue(null, "value")
                    "MultipleInstancesAllowed" -> multiple = parser.getAttributeValue(null, "value")
                    "Editable" -> editable = parser.getAttributeValue(null, "value")
                }
            }
            parser.next()
        }
        parser.close()

        val previewName = decodeResourceReference(preview)
        assertEquals("${context.packageName}:drawable/watchface_preview", previewName)
        assertEquals("CATEGORY_EMPTY", category)
        assertEquals("true", available)
        assertEquals("true", multiple)
        assertEquals("true", editable)
    }

    private fun parseXmlResource(resId: Int): Document {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = false
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)

        val builder = factory.newDocumentBuilder()
        context.resources.openRawResource(resId).use { stream ->
            return builder.parse(stream)
        }
    }

    private fun decodeResourceReference(raw: String?): String? {
        if (raw == null) return null
        val numeric = raw.removePrefix("@").toIntOrNull()
        return numeric?.let { id -> context.resources.getResourceName(id) } ?: raw
    }
}
