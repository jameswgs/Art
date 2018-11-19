package net.slenderloris.art

import processing.core.PApplet
import processing.core.PVector
import processing.event.KeyEvent

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Lines4")
}

data class Particle(var pos: PVector, val colour: Int)

class Lines4 : PApplet() {

    private lateinit var points: List<Particle>
    private var frameNumber = 0

    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        val seed: Long = 4
        noiseSeed(seed)
        randomSeed(seed)

        background(0)
        colorMode(HSB, 255.0f)
        blendMode(ADD)

        points = (0..999).map {
            val spread = 10.0f
            val x = 600.0f + random(spread)
            val y = 600.0f + random(spread)
            val col = color(random(255.0f), 255.0f, 255.0f, 2.0f)
            Particle(PVector(x, y), col)
        }
    }

    override fun draw() {
        points.forEach { point ->
            stroke(point.colour)
            val forceScale = 1.0f
            val pos = point.pos
            val noiseVector = noiseVector(pos.x, pos.y, frameNumber.toFloat())
            val v2 = noiseVector * forceScale
            val oldPos = pos.copy()
            pos.add(v2)
            line(oldPos.x, oldPos.y, pos.x, pos.y)
        }
        frameNumber++
    }

    private fun noiseVector(x: Float, y: Float, z: Float): PVector {
        val distScale = 0.007f
        val noise = noise(x * distScale, y * distScale, z * distScale)
        val theta = noise * Math.PI.toFloat() * 20.0f
        return PVector(1.0f, 0.0f).rotate(theta)
    }

    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if(event.key == 's') {
            saveFrame("frame-#######.png")
        }
    }

}

private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
