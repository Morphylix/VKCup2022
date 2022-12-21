package com.morphylix.android.dzentest.widget

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withScale
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.math.MathUtils.lerp
import com.morphylix.android.dzentest.R
import com.morphylix.android.dzentest.utils.dp

private const val TAG = "CustomButton"

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {


    sealed class UIState {
        object Unpressed : UIState()
        object Pressed : UIState()
        object Animating : UIState()
    }

    private var fraction: Float = 0f

    private var a: TypedArray? =
        context.obtainStyledAttributes(attrs, R.styleable.CustomButton, defStyle, 0)
    private val iconDrawable = a!!.getDrawable(R.styleable.CustomButton_android_icon)
    private val drawableID = a!!.getResourceId(R.styleable.CustomButton_android_icon, 0)

    private val iconBitmap = iconDrawable!!.toBitmap()

    private val radius = 32 * dp()

    private val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
    }

    private val srcInMode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private val drawableCanvas = Canvas(this.iconBitmap)
    private val argbEvaluator = ArgbEvaluator()
    private var fromBgColor: Int = Color.parseColor("#f6f5f8")
    private var toBgColor: Int = a!!.getColor(R.styleable.CustomButton_android_backgroundTint, 0)
    private var fromBmColor: Int = a!!.getColor(R.styleable.CustomButton_android_color, 0)
    private var toBmColor: Int = Color.parseColor("#f6507d")

    private fun getRoundRectPaint() = paint.apply {
        val bgFraction = lerp(0.0f, 1.0f, 0.0f, fraction)

        color = argbEvaluator.evaluate(bgFraction, fromBgColor, toBgColor) as Int
    }

    private fun getBitmapPaint() = paint.apply {
        val bgFraction = lerp(0.0f, 1.0f, 0.0f, fraction)
        color = argbEvaluator.evaluate(bgFraction, fromBmColor, toBmColor) as Int
    }

    private fun configureColors(drawableID: Int) {
        val nightModeFlags = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                Log.i("NIGHT", "NIGHT")
                when (drawableID) {
                    R.drawable.baseline_close_black_24dp -> {
                        fromBgColor = Color.parseColor("#7C7C7C")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                    R.drawable.baseline_done_black_24dp -> {
                        fromBgColor = Color.parseColor("#7C7C7C")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                    R.drawable.baseline_settings_backup_restore_black_24dp -> {
                        fromBgColor = Color.parseColor("#7C7C7C")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                }
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                Log.i("DAY", "DAY")
                when (drawableID) {
                    R.drawable.baseline_close_black_24dp -> {
                        fromBgColor = Color.parseColor("#E6E6E6")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                    R.drawable.baseline_done_black_24dp -> {
                        fromBgColor = Color.parseColor("#E6E6E6")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                    R.drawable.baseline_settings_backup_restore_black_24dp -> {
                        fromBgColor = Color.parseColor("#E6E6E6")
                        toBmColor = Color.parseColor("#5A5A5A")
                    }
                }
            }
        }
    }

    private fun tintBitmap() {
        // change to src in
        paint.xfermode = srcInMode
        drawableCanvas.drawRect(
            0f,
            0f,
            this.iconBitmap.width.toFloat(),
            this.iconBitmap.height.toFloat(),
            getBitmapPaint()
        )
        paint.xfermode = null
    }

    override fun onDraw(canvas: Canvas) {
        configureColors(drawableID)


        // round rect
        getRoundRectPaint().also {
            // bound
            val left = 0f
            val top = 0f
            val right = width.toFloat()
            val bottom = height.toFloat()
            // scale
            val scaleX = lerp(1f, 0.8f, 1f, fraction)
            val scaleY = scaleX
            val pivotX = width / 2f
            val pivotY = height / 2f
            canvas.withScale(scaleX, scaleY, pivotX, pivotY) {
                canvas.drawRoundRect(left, top, right, bottom, radius, radius, it)
            }
        }

        // icon
        getBitmapPaint().also { paint ->
            val scaleX = lerp(1f, 0.6f, 1f, fraction)
            val scaleY = scaleX
            val pivotX = width / 2f
            val pivotY = height / 2f
            canvas.withScale(scaleX, scaleY, pivotX, pivotY) {
                val left = width / 2f - iconBitmap.width / 2f
                val top = height / 2f - iconBitmap.height / 2f
                tintBitmap()
                canvas.drawBitmap(iconBitmap, left, top, paint)
            }
        }
    }

    private var uiState: UIState = UIState.Unpressed

    fun setUIState(uiState: UIState, isAnim: Boolean) {
        if (this.uiState == UIState.Animating) {
            clearAnimation()
        }
        if (isAnim) {
            runAnimation().apply {
                doOnEnd {
                    this@CustomButton.uiState = uiState
                }
            }
        }
    }

    private fun runAnimation(): ValueAnimator {
        return ValueAnimator.ofFloat(0f, 1f).apply {
            addUpdateListener {
                fraction = it.animatedValue as Float
                invalidate()
            }
            interpolator = FastOutSlowInInterpolator()
            doOnStart {
                this@CustomButton.uiState = UIState.Animating
            }
            duration = 300L
            start()
        }
    }

    private fun lerp(a: Float, b: Float, c: Float, fraction: Float): Float {
        return if (fraction <= 0.5f) {
            lerp(a, b, fraction * 2)
        } else {
            val tempFraction = fraction - 0.5f
            lerp(b, c, tempFraction * 2)
        }
    }
}