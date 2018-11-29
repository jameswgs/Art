package net.slenderloris.art

import processing.core.PConstants
import processing.core.PVector
import kotlin.math.cos
import kotlin.math.sin

fun makeHex(x: Float, y: Float, radius: Float, sides: Int): Shape {
    val verts = (0 until sides).map {
        val rads = PConstants.TWO_PI / sides.toFloat() * it.toFloat()
        PVector(sin(rads) * radius + x, cos(rads) * radius + y)
    }
    return Shape(verts)
}

fun makeRect(x: Float, y: Float, w: Float, h: Float): Shape {
    return Shape(listOf(
            PVector(x, y),
            PVector(x + w, y),
            PVector(x + w, y + h),
            PVector(x, y + h)
    ))
}