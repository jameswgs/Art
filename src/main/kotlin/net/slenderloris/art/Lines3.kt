package net.slenderloris.art

import processing.core.PApplet
import processing.core.PVector

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Lines3")
}

class Lines3 : PApplet() {

    private val points = (0..999).map {
        val x = 575.0f + random(5.0f)
        val y = 575.0f + random(5.0f)
        PVector(x, y)
    }

    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        background(255)
        stroke(0x10000000)
        noiseSeed(2)
    }

    override fun draw() {
        points.forEach { point ->
            point(point.x, point.y)
            val forceScale = 1.0f
            point.add(noiseVector(point.x, point.y) * forceScale)
        }

    }

    private fun noiseVector(x: Float, y: Float): PVector {
        val distScale = 0.0007f
        val millis = millis().toFloat() * 0.0001f
        val noise = noise(x * distScale, y * distScale, millis)
        val theta = noise * Math.PI.toFloat() * 20.0f
        return PVector(1.0f, 0.0f).rotate(theta)
    }

}

private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
