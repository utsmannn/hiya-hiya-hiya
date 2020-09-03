package com.utsman.hiyahiyahiya.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.utsman.hiyahiyahiya.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CustomFloatingActionButton(context: Context, attributeSet: AttributeSet) : FloatingActionButton(context, attributeSet) {

    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        super.setBackgroundTintMode(tintMode)
    }

    /*@SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_maps)
        //val canv = Canvas()
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val newCanvas = Canvas(newBitmap)

        val color: Int = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        newCanvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        newCanvas.drawCircle((bitmap.width/2).toFloat(), (bitmap.height/2).toFloat(), (bitmap.width/2).toFloat(), paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        newCanvas.drawBitmap(bitmap, rect, rect, paint)
        super.onDraw(newCanvas)
    }*/

    /*override fun draw(canvas: Canvas?) {
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_maps)
        super.draw(canvas)
    }*/

    /*override fun setBackgroundResource(resid: Int) {
        super.setBackgroundResource(R.drawable.bg_maps)
    }*/

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bg_maps)
        //val newB = getCroppedBitmap(bitmap)
        val paint = Paint()
        canvas?.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        //canvas?.drawBitmap(newB, matrix, Paint())
    }

    private suspend fun getBitmap(): Bitmap = suspendCoroutine {
        Picasso.get().load(R.drawable.bg_maps)
            .transform(CropCircleTransformation())
            .into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    it.resume(bitmap)
                }

            })
    }


    private fun getCroppedBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(), paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output
    }
}