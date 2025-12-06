package com.cosmobond.watchface

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.view.doOnLayout

private const val MIN_TIME_SIZE_SP = 44
private const val MIN_DATE_SIZE_SP = 18
private const val MIN_MUSIC_SIZE_SP = 16

class LayoutSetupActivity : Activity() {
    private lateinit var layoutRepo: LayoutPreferencesRepository
    private lateinit var overlayContainer: FrameLayout
    private lateinit var slotOverlay: View
    private lateinit var slotButtons: ViewGroup
    private lateinit var timeView: TextView
    private lateinit var dateView: TextView
    private lateinit var musicView: TextView
    private lateinit var timeSizeValue: TextView
    private lateinit var dateSizeValue: TextView
    private lateinit var musicSizeValue: TextView
    private lateinit var previewPlaceholder: View
    private lateinit var videoView: VideoView
    private val handler = Handler(Looper.getMainLooper())
    private var videoPrepared = false
    private var activeDrag: ElementKind? = null
    private val anchors =
        listOf(
            PointF(0.25f, 0.25f),
            PointF(0.75f, 0.25f),
            PointF(0.25f, 0.75f),
            PointF(0.75f, 0.75f),
            PointF(0.5f, 0.5f),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_setup)
        layoutRepo = LayoutPreferencesRepository(applicationContext)

        overlayContainer = findViewById(R.id.overlay_container)
        slotOverlay = findViewById(R.id.slot_overlay)
        slotButtons = findViewById(R.id.slot_buttons)
        timeView = findViewById(R.id.time_preview)
        dateView = findViewById(R.id.date_preview)
        musicView = findViewById(R.id.music_preview)
        previewPlaceholder = findViewById(R.id.preview_placeholder)
        timeSizeValue = findViewById(R.id.time_size_value)
        dateSizeValue = findViewById(R.id.date_size_value)
        musicSizeValue = findViewById(R.id.music_size_value)
        videoView = findViewById(R.id.preview_video)

        val showDate = findViewById<CheckBox>(R.id.show_date_checkbox)
        val showSeconds = findViewById<CheckBox>(R.id.show_seconds_checkbox)
        val use24 = findViewById<CheckBox>(R.id.use_24_checkbox)
        val timeFontSpinner = findViewById<Spinner>(R.id.time_font_spinner)
        val dateFontSpinner = findViewById<Spinner>(R.id.date_font_spinner)
        val dateFormatSpinner = findViewById<Spinner>(R.id.date_format_spinner)
        val timeSizeSeek = findViewById<SeekBar>(R.id.time_size_seek)
        val dateSizeSeek = findViewById<SeekBar>(R.id.date_size_seek)
        val musicSizeSeek = findViewById<SeekBar>(R.id.music_size_seek)

        setupVideo()
        bindSpinners(timeFontSpinner, dateFontSpinner, dateFormatSpinner)
        bindSeekBars(timeSizeSeek, dateSizeSeek, musicSizeSeek)

        val prefs = layoutRepo.current()
        showDate.isChecked = prefs.showDate
        showSeconds.isChecked = prefs.showSeconds
        use24.isChecked = prefs.use24Hour

        showDate.setOnCheckedChangeListener { _, checked ->
            layoutRepo.setShowDate(checked)
            refreshPreview()
        }
        showSeconds.setOnCheckedChangeListener { _, checked ->
            layoutRepo.setShowSeconds(checked)
            refreshPreview()
        }
        use24.setOnCheckedChangeListener { _, checked ->
            layoutRepo.setUse24Hour(checked)
            refreshPreview()
        }

        setMovable(timeView, ElementKind.TIME)
        setMovable(dateView, ElementKind.DATE)
        setMovable(musicView, ElementKind.MUSIC)

        findViewById<Button>(R.id.save_layout_button).setOnClickListener {
            launchPicker()
        }

        overlayContainer.doOnLayout {
            applyPositions(layoutRepo.current())
            refreshPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(timeUpdater)
        refreshPreview()
        resumeVideo()
    }

    override fun onPause() {
        handler.removeCallbacks(timeUpdater)
        previewPlaceholder.visibility = View.VISIBLE
        videoView.pause()
        super.onPause()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        videoView.stopPlayback()
        layoutRepo.close()
        super.onDestroy()
    }

    private fun setupVideo() {
        videoPrepared = false
        previewPlaceholder.visibility = View.VISIBLE
        val uri = Uri.parse("android.resource://$packageName/${R.raw.beatbunny_idle}")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { player ->
            videoPrepared = true
            previewPlaceholder.visibility = View.GONE
            player.isLooping = true
            player.setVolume(0f, 0f)
            player.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            videoView.start()
        }
        videoView.setOnErrorListener { _, _, _ ->
            videoPrepared = false
            previewPlaceholder.visibility = View.VISIBLE
            true
        }
        videoView.setOnCompletionListener { videoView.start() }
    }

    private fun resumeVideo() {
        if (videoView.isPlaying) {
            previewPlaceholder.visibility = View.GONE
            return
        }
        if (videoPrepared) {
            previewPlaceholder.visibility = View.GONE
            videoView.start()
        } else {
            setupVideo()
        }
    }

    private fun bindSpinners(
        timeFontSpinner: Spinner,
        dateFontSpinner: Spinner,
        dateFormatSpinner: Spinner,
    ) {
        val fontLabels =
            listOf(
                getString(R.string.layout_setup_font_default),
                getString(R.string.layout_setup_font_mono),
                getString(R.string.layout_setup_font_serif),
            )
        val fontValues = listOf(FontVariant.DEFAULT, FontVariant.MONO, FontVariant.SERIF)
        val fontAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, fontLabels).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        timeFontSpinner.adapter = fontAdapter
        dateFontSpinner.adapter = fontAdapter

        val dateLabels =
            listOf(
                getString(R.string.layout_setup_date_short),
                getString(R.string.layout_setup_date_long),
                getString(R.string.layout_setup_date_numeric),
            )
        val dateValues =
            listOf(DateFormatVariant.SHORT, DateFormatVariant.LONG, DateFormatVariant.NUMERIC)
        val dateAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, dateLabels).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        dateFormatSpinner.adapter = dateAdapter

        val prefs = layoutRepo.current()
        timeFontSpinner.setSelection(fontValues.indexOf(prefs.timeFont).coerceAtLeast(0))
        dateFontSpinner.setSelection(fontValues.indexOf(prefs.dateFont).coerceAtLeast(0))
        dateFormatSpinner.setSelection(dateValues.indexOf(prefs.dateFormat).coerceAtLeast(0))

        timeFontSpinner.onItemSelectedListener =
            simpleSelectionListener { index ->
                layoutRepo.setTimeFont(fontValues[index])
                refreshPreview()
            }
        dateFontSpinner.onItemSelectedListener =
            simpleSelectionListener { index ->
                layoutRepo.setDateFont(fontValues[index])
                refreshPreview()
            }
        dateFormatSpinner.onItemSelectedListener =
            simpleSelectionListener { index ->
                layoutRepo.setDateFormat(dateValues[index])
                refreshPreview()
            }
    }

    private fun bindSeekBars(
        timeSeek: SeekBar,
        dateSeek: SeekBar,
        musicSeek: SeekBar,
    ) {
        val prefs = layoutRepo.current()
        timeSeek.progress = (prefs.timeTextSizeSp - MIN_TIME_SIZE_SP).toInt().coerceAtLeast(0)
        dateSeek.progress = (prefs.dateTextSizeSp - MIN_DATE_SIZE_SP).toInt().coerceAtLeast(0)
        musicSeek.progress = (prefs.musicTextSizeSp - MIN_MUSIC_SIZE_SP).toInt().coerceAtLeast(0)
        timeSizeValue.text = formatSize(prefs.timeTextSizeSp)
        dateSizeValue.text = formatSize(prefs.dateTextSizeSp)
        musicSizeValue.text = formatSize(prefs.musicTextSizeSp)

        timeSeek.setOnSeekBarChangeListener(
            simpleSeekListener { value ->
                val size = MIN_TIME_SIZE_SP + value
                layoutRepo.setTimeTextSize(size.toFloat())
                timeSizeValue.text = formatSize(size.toFloat())
                refreshPreview()
            },
        )
        dateSeek.setOnSeekBarChangeListener(
            simpleSeekListener { value ->
                val size = MIN_DATE_SIZE_SP + value
                layoutRepo.setDateTextSize(size.toFloat())
                dateSizeValue.text = formatSize(size.toFloat())
                refreshPreview()
            },
        )
        musicSeek.setOnSeekBarChangeListener(
            simpleSeekListener { value ->
                val size = MIN_MUSIC_SIZE_SP + value
                layoutRepo.setMusicTextSize(size.toFloat())
                musicSizeValue.text = formatSize(size.toFloat())
                refreshPreview()
            },
        )
    }

    private fun refreshPreview() {
        val prefs = layoutRepo.current()
        val nowText =
            CompanionTimeFormatter.formatTime(
                java.time.ZonedDateTime.now(),
                prefs.showSeconds,
                prefs.use24Hour,
            )
        timeView.text = nowText
        timeView.typeface = toTypeface(prefs.timeFont)
        timeView.textSize = prefs.timeTextSizeSp

        if (prefs.showDate) {
            dateView.visibility = View.VISIBLE
            dateView.text =
                CompanionDateFormatter.formatDate(
                    java.time.ZonedDateTime.now(),
                    prefs.dateFormat,
                )
            dateView.typeface = toTypeface(prefs.dateFont)
            dateView.textSize = prefs.dateTextSizeSp
        } else {
            dateView.visibility = View.GONE
        }

        musicView.text = getString(R.string.layout_setup_music_label)
        musicView.textSize = prefs.musicTextSizeSp
        musicView.typeface = Typeface.SANS_SERIF

        timeSizeValue.text = formatSize(prefs.timeTextSizeSp)
        dateSizeValue.text = formatSize(prefs.dateTextSizeSp)
        musicSizeValue.text = formatSize(prefs.musicTextSizeSp)

        applyPositions(prefs)
    }

    private fun applyPositions(prefs: LayoutPreferences) {
        place(timeView, prefs.timePosition)
        place(dateView, prefs.datePosition)
        place(musicView, prefs.musicPosition)
    }

    private fun formatSize(size: Float): String = getString(R.string.layout_setup_size_value, size.toInt())

    private fun place(view: View, point: PointF) {
        overlayContainer.post {
            val parentW = overlayContainer.width.takeIf { it > 0 } ?: return@post
            val parentH = overlayContainer.height.takeIf { it > 0 } ?: return@post
            val x = parentW * point.x - view.width / 2f
            val y = parentH * point.y - view.height / 2f
            view.x = x.coerceIn(0f, (parentW - view.width).toFloat())
            view.y = y.coerceIn(0f, (parentH - view.height).toFloat())
        }
    }

    private fun setMovable(view: TextView, kind: ElementKind) {
        view.setOnLongClickListener {
            activeDrag = kind
            val data = ClipData.newPlainText("label", kind.name)
            it.startDragAndDrop(data, View.DragShadowBuilder(it), null, 0)
            true
        }
        view.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                activeDrag = kind
            }
            if (activeDrag != kind) return@setOnTouchListener false
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    moveViewWithTouch(v, event)
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    finalizeMove(v as TextView, kind)
                    activeDrag = null
                    true
                }
                else -> false
            }
        }
        view.setOnClickListener {
            showOptions(kind)
        }
    }

    private fun moveViewWithTouch(v: View, event: MotionEvent) {
        val parentLoc = IntArray(2)
        overlayContainer.getLocationOnScreen(parentLoc)
        val newX = event.rawX - parentLoc[0] - v.width / 2
        val newY = event.rawY - parentLoc[1] - v.height / 2
        val maxX = overlayContainer.width - v.width
        val maxY = overlayContainer.height - v.height
        v.x = newX.coerceIn(0f, maxX.toFloat())
        v.y = newY.coerceIn(0f, maxY.toFloat())
    }

    private fun finalizeMove(view: TextView, kind: ElementKind) {
        val parentW = overlayContainer.width.takeIf { it > 0 } ?: return
        val parentH = overlayContainer.height.takeIf { it > 0 } ?: return
        val normalized = PointF((view.x + view.width / 2f) / parentW, (view.y + view.height / 2f) / parentH)
        when (kind) {
            ElementKind.TIME -> layoutRepo.setTimePosition(normalized)
            ElementKind.DATE -> layoutRepo.setDatePosition(normalized)
            ElementKind.MUSIC -> layoutRepo.setMusicPosition(normalized)
        }
        resolveOverlaps(view, kind)
    }

    private fun resolveOverlaps(moved: View, kind: ElementKind) {
        val others =
            listOf(
                ElementKind.TIME to timeView,
                ElementKind.DATE to dateView,
                ElementKind.MUSIC to musicView,
            ).filter { it.second != moved && it.second.visibility == View.VISIBLE }

        val movedRect = Rect()
        moved.getHitRect(movedRect)
        val overlapped = others.firstOrNull { (_, view) ->
            val r = Rect()
            view.getHitRect(r)
            Rect.intersects(movedRect, r)
        } ?: return

        showSlotChooser(overlapped.first, overlapped.second)
    }

    private fun showSlotChooser(kind: ElementKind, target: View) {
        slotOverlay.visibility = View.VISIBLE
        slotButtons.removeAllViews()
        anchors.forEachIndexed { index, point ->
            val button = Button(this)
            button.text = getString(R.string.layout_setup_slot_button, index + 1)
            button.setOnClickListener {
                place(target, point)
                when (kind) {
                    ElementKind.TIME -> layoutRepo.setTimePosition(point)
                    ElementKind.DATE -> layoutRepo.setDatePosition(point)
                    ElementKind.MUSIC -> layoutRepo.setMusicPosition(point)
                }
                slotOverlay.visibility = View.GONE
            }
            slotButtons.addView(button)
        }
        slotOverlay.setOnClickListener { slotOverlay.visibility = View.GONE }
    }

    private fun toTypeface(variant: FontVariant): Typeface =
        when (variant) {
            FontVariant.MONO -> Typeface.MONOSPACE
            FontVariant.SERIF -> Typeface.SERIF
            else -> Typeface.DEFAULT
        }

    private val timeUpdater: Runnable =
        object : Runnable {
            override fun run() {
                refreshPreview()
                handler.postDelayed(this, 1000L)
            }
        }

    private fun simpleSelectionListener(onSelect: (Int) -> Unit) =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                onSelect(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

    private fun simpleSeekListener(onChange: (Int) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) onChange(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        }

    private fun showOptions(kind: ElementKind) {
        when (kind) {
            ElementKind.TIME -> {
                val prefs = layoutRepo.current()
                AlertDialog.Builder(this)
                    .setTitle(R.string.layout_setup_time_options_title)
                    .setItems(
                        arrayOf(
                            toggleLabel(R.string.layout_setup_show_seconds, prefs.showSeconds),
                            toggleLabel(R.string.layout_setup_use_24, prefs.use24Hour),
                        ),
                    ) { _, which ->
                        when (which) {
                            0 -> layoutRepo.setShowSeconds(!prefs.showSeconds)
                            1 -> layoutRepo.setUse24Hour(!prefs.use24Hour)
                        }
                        refreshPreview()
                    }
                    .show()
            }
            ElementKind.DATE -> {
                val prefs = layoutRepo.current()
                AlertDialog.Builder(this)
                    .setTitle(R.string.layout_setup_date_options_title)
                    .setItems(
                        arrayOf(
                            toggleLabel(R.string.layout_setup_show_date, prefs.showDate),
                            getString(R.string.layout_setup_date_short),
                            getString(R.string.layout_setup_date_long),
                            getString(R.string.layout_setup_date_numeric),
                        ),
                    ) { _, which ->
                        when (which) {
                            0 -> layoutRepo.setShowDate(!prefs.showDate)
                            1 -> layoutRepo.setDateFormat(DateFormatVariant.SHORT)
                            2 -> layoutRepo.setDateFormat(DateFormatVariant.LONG)
                            3 -> layoutRepo.setDateFormat(DateFormatVariant.NUMERIC)
                        }
                        refreshPreview()
                    }
                    .show()
            }
            ElementKind.MUSIC -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.layout_setup_music_options_title)
                    .setMessage("Spotify is shown for BeatBunny. Future updates will add more providers.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }
    }

    private fun toggleLabel(stringId: Int, enabled: Boolean): String =
        "${getString(stringId)} (${if (enabled) "on" else "off"})"

    private fun launchPicker() {
        val intents =
            listOf(
                Intent("com.google.android.clockwork.home.action.CHANGE_WATCH_FACE"),
                Intent("com.google.android.clockwork.action.SET_WATCH_FACE"),
                Intent("android.service.wallpaper.LIVE_WALLPAPER_CHOOSER").addCategory(Intent.CATEGORY_DEFAULT),
            )
        for (intent in intents) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (intent.resolveActivity(packageManager) != null) {
                runCatching { startActivity(intent) }
                Toast.makeText(this, "Opening watch-face picker...", Toast.LENGTH_SHORT).show()
                return
            }
        }
        Toast.makeText(this, "Picker unavailable. Select CosmoBond from watch-face list.", Toast.LENGTH_LONG).show()
    }
}

private enum class ElementKind {
    TIME, DATE, MUSIC
}
