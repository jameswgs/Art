package net.slenderloris.art

import processing.core.PApplet
import processing.core.PVector

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Lines3")
}

class Lines3 : PApplet() {

    private val points: List<PVector>
    private var frameNumber = 0

    init {
        val seed: Long = 4

        noiseSeed(seed)
        randomSeed(seed)

        points = (0..999).map {
            val spread = 1000.0f
            val x = 575.0f + random(spread)
            val y = 575.0f + random(spread)
            PVector(x, y)
        }
    }

    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        background(255)
        stroke(0x10000000)
    }

    override fun draw() {
        points.forEach { point ->
            point(point.x, point.y)
            val forceScale = 1.0f
            point.add(noiseVector(point.x, point.y, frameNumber.toFloat()) * forceScale)
        }
        frameNumber ++
    }

    private fun noiseVector(x: Float, y: Float, z: Float): PVector {
        val distScale = 0.0006f
        val noise = noise(x * distScale, y * distScale, z * distScale)
        val theta = noise * Math.PI.toFloat() * 50.0f
        return PVector(1.0f, 0.0f).rotate(theta)
    }

}

private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
