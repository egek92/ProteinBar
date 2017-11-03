package us.egek.proteinbar

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.annotation.RestrictTo
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP
import java.lang.IllegalStateException

/**
 * Created by ege on 03/11/2017.
 */

class Protein private constructor(private val builder: Builder) {

  enum class Type private constructor(@param:ColorInt val color: Int?, @param:DrawableRes private val iconResId: Int?,
      @param:ColorInt val standardTextColor: Int?) {
    DEFAULT(null, null, null), SUCCESS(Color.parseColor("#4CAF50"), R.drawable.ic_check_black_24dp,
        Color.WHITE),
    ERROR(Color.parseColor("#FF5252"), R.drawable.ic_clear_black_24dp,
        Color.WHITE),
    INFO(Color.parseColor("#03A9F4"), R.drawable.ic_info_outline_black_24dp,
        Color.WHITE),
    WARNING(Color.parseColor("#FFEB3B"), R.drawable.ic_error_outline_black_24dp,
        Color.BLACK);

    fun getIcon(context: Context): Drawable? {
      return if (iconResId == null) null else Bar.tintDrawable(
          ContextCompat.getDrawable(context, iconResId)!!,
          standardTextColor!!)

    }
  }

  private fun make(): Snackbar {

    val snackbar = Snackbar.make(builder.view!!, builder.text, builder.duration)

    if (builder.actionClickListener == null) {
      builder.actionClickListener = View.OnClickListener { }
    }
    snackbar.setAction(builder.actionText, builder.actionClickListener)
    if (builder.actionTextColor == null) {
      builder.actionTextColor = builder.type.standardTextColor
    }
    if (builder.actionTextColors != null) {
      snackbar.setActionTextColor(builder.actionTextColors)
    } else if (builder.actionTextColor != null) {
      snackbar.setActionTextColor(builder.actionTextColor!!)
    }

    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    if (builder.backgroundColor == null) builder.backgroundColor = builder.type.color
    if (builder.backgroundColor != null) snackbarLayout.setBackgroundColor(
        builder.backgroundColor!!)

    val actionText = snackbarLayout.findViewById<View>(
        android.support.design.R.id.snackbar_action) as TextView
    if (builder.actionTextSize != null) {
      if (builder.actionTextSizeUnit != null) {
        actionText.setTextSize(builder.actionTextSizeUnit!!, builder.actionTextSize!!)
      } else {
        actionText.textSize = builder.actionTextSize!!
      }
    }
    if (builder.actionTextTypefaceStyle != null) {
      actionText.setTypeface(actionText.typeface, builder.actionTextTypefaceStyle!!)
    }

    val text = snackbarLayout.findViewById<View>(
        android.support.design.R.id.snackbar_text) as TextView

    if (builder.textSize != null) {
      if (builder.textSizeUnit != null) {
        text.setTextSize(builder.textSizeUnit!!, builder.textSize!!)
      } else {
        text.textSize = builder.textSize!!
      }
    }
    if (builder.textTypefaceStyle != null) {
      text.setTypeface(text.typeface, builder.textTypefaceStyle!!)
    }

    if (builder.textColor == null) builder.textColor = builder.type.standardTextColor
    if (builder.textColors != null) {
      text.setTextColor(builder.textColors)
    } else if (builder.textColor != null) text.setTextColor(builder.textColor!!)
    text.maxLines = builder.maxLines
    text.gravity = if (builder.centerText) Gravity.CENTER else Gravity.CENTER_VERTICAL
    if (builder.centerText && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
      text.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    if (builder.icon == null) {
      builder.icon = builder.type.getIcon(builder.view!!.context)
    }
    if (builder.icon != null) {
      var transparentHelperDrawable: Drawable? = null
      if (builder.centerText && TextUtils.isEmpty(builder.actionText)) {
        transparentHelperDrawable = Bar.makeTransparentDrawable(builder.view!!.context,
            builder.icon!!.intrinsicWidth, builder.icon!!.intrinsicHeight)
      }
      text.setCompoundDrawablesWithIntrinsicBounds(builder.icon, null, transparentHelperDrawable,
          null)
      text.compoundDrawablePadding = text.resources.getDimensionPixelOffset(R.dimen.icon_padding)
    }

    return snackbar
  }

  @RestrictTo(LIBRARY_GROUP)
  @IntDef(LENGTH_INDEFINITE.toLong(), LENGTH_SHORT.toLong(), LENGTH_LONG.toLong())
  @IntRange(from = 1)
  @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
  internal annotation class Duration

  class Builder {

    var view: View? = null
    var type = Type.DEFAULT
    var duration = Snackbar.LENGTH_SHORT
    var text: CharSequence = ""
    var textResId = 0
    var textColor: Int? = null
    var textColors: ColorStateList? = null
    var textSizeUnit: Int? = null
    var textSize: Float? = null
    var textTypefaceStyle: Int? = null
    var actionTextSizeUnit: Int? = null
    var actionTextSize: Float? = null
    var actionText: CharSequence = ""
    var actionTextResId = 0
    var actionTextTypefaceStyle: Int? = null
    var actionClickListener: View.OnClickListener? = null
    var actionTextColor: Int? = null
    var actionTextColors: ColorStateList? = null
    var maxLines = Integer.MAX_VALUE
    var centerText = false
    var icon: Drawable? = null
    var iconResId = 0
    var backgroundColor: Int? = null

    fun setActivity(activity: Activity): Builder {
      return setView((activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0))
    }

    private fun setView(view: View): Builder {
      this.view = view
      return this
    }

    fun setText(@StringRes resId: Int): Builder {
      this.textResId = resId
      return this
    }

    fun setText(text: CharSequence): Builder {
      this.textResId = 0
      this.text = text
      return this
    }

    fun setTextColor(@ColorInt color: Int): Builder {
      this.textColor = color
      return this
    }

    fun setTextColor(colorStateList: ColorStateList): Builder {
      this.textColor = null
      this.textColors = colorStateList
      return this
    }

    fun setTextSize(textSize: Float): Builder {
      this.textSizeUnit = null
      this.textSize = textSize
      return this
    }

    fun setTextSize(unit: Int, textSize: Float): Builder {
      this.textSizeUnit = unit
      this.textSize = textSize
      return this
    }

    fun setTextTypefaceStyle(style: Int): Builder {
      this.textTypefaceStyle = style
      return this
    }

    fun centerText(): Builder {
      this.centerText = true
      return this
    }

    fun setActionTextColor(colorStateList: ColorStateList): Builder {
      this.actionTextColor = null
      this.actionTextColors = colorStateList
      return this
    }

    fun setActionTextColor(@ColorInt color: Int): Builder {
      this.actionTextColor = color
      return this
    }

    fun setActionText(@StringRes resId: Int): Builder {
      this.actionTextResId = resId
      return this
    }

    fun setActionText(text: CharSequence): Builder {
      this.textResId = 0
      this.actionText = text
      return this
    }

    fun setActionTextSize(textSize: Float): Builder {
      this.actionTextSizeUnit = null
      this.actionTextSize = textSize
      return this
    }

    fun setActionTextSize(unit: Int, textSize: Float): Builder {
      this.actionTextSizeUnit = unit
      this.actionTextSize = textSize
      return this
    }

    fun setActionTextTypefaceStyle(style: Int): Builder {
      this.actionTextTypefaceStyle = style
      return this
    }

    fun setActionClickListener(listener: View.OnClickListener): Builder {
      this.actionClickListener = listener
      return this
    }

    fun setMaxLines(maxLines: Int): Builder {
      this.maxLines = maxLines
      return this
    }

    fun setDuration(@Duration duration: Int): Builder {
      this.duration = duration
      return this
    }

    fun setIcon(@DrawableRes resId: Int): Builder {
      this.iconResId = resId
      return this
    }

    fun setIcon(drawable: Drawable): Builder {
      this.icon = drawable
      return this
    }

    fun setBackgroundColor(@ColorInt color: Int): Builder {
      this.backgroundColor = color
      return this
    }

    fun build(): Snackbar {
      return make()
    }

    fun success(): Snackbar {
      type = Type.SUCCESS
      return make()
    }

    fun info(): Snackbar {
      type = Type.INFO
      return make()
    }

    fun warning(): Snackbar {
      type = Type.WARNING
      return make()
    }

    fun error(): Snackbar {
      type = Type.ERROR
      return make()
    }

    private fun make(): Snackbar {
      if (view == null) {
        throw IllegalStateException(
            "Set an Activity first")
      }
      if (textResId != 0) text = view!!.resources.getText(textResId)
      if (actionTextResId != 0) actionText = view!!.resources.getText(actionTextResId)
      if (iconResId != 0) icon = ContextCompat.getDrawable(view!!.context, iconResId)
      return Protein(this).make()
    }
  }

  companion object {

    fun builder(): Builder {
      return Builder()
    }

    const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE
    const val LENGTH_SHORT = Snackbar.LENGTH_SHORT
    const val LENGTH_LONG = Snackbar.LENGTH_LONG
  }
}

internal object Bar {

  fun tintDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
    var mDrawable = drawable
    mDrawable = DrawableCompat.wrap(mDrawable)
    mDrawable = mDrawable.mutate()
    DrawableCompat.setTint(mDrawable, color)
    return mDrawable
  }

  fun makeTransparentDrawable(context: Context, width: Int, height: Int): Drawable {
    val conf = Bitmap.Config.ARGB_8888
    val bmp = Bitmap.createBitmap(width, height, conf)
    return BitmapDrawable(context.resources, bmp)
  }
}
