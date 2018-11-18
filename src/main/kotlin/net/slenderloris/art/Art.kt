package net.slenderloris.art

import processing.core.PApplet
import processing.core.PVector

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Art")
}

class Art : PApplet() {

    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        fill(120f, 50f, 240f)
    }

    override fun draw() {
        background(255)
        val step = 50
        val halfStep = step / 2
        ( 0 until width ).step(step).forEach { x ->
            ( 0 until height).step(step).forEach { y ->
                drawLine( (x+halfStep).toFloat(), (y+halfStep).toFloat(), step.times(0.5f))
            }
        }
    }

    private fun drawLine(xPos: Float, yPos: Float, size: Float) {
        val millis = millis().toFloat() * 0.001f
        val vec = PVector(1.0f, 0.0f)
        val distScale = 0.1f / size
        val noise = noise(xPos * distScale, yPos * distScale, millis)
        vec.rotate(noise * Math.PI.toFloat() * 2.0f ) * size
        line(xPos, yPos, xPos + vec.x, yPos + vec.y)
    }

}

private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
