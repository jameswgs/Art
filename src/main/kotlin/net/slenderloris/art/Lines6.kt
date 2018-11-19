package net.slenderloris.art

import processing.core.PApplet
import processing.core.PVector
import processing.event.KeyEvent

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Lines6")
}

class Lines6 : PApplet() {

    private lateinit var points: List<Particle>
    private var frameNumber = 0
    private var zoom = 2.0f

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

        points = (0..9999).map {
            val spread = 400.0f
            val x = random(spread) - spread / 2
            val y = random(spread) - spread / 2
            val col = color(random(255.0f), 255.0f, 255.0f, 5.0f)
            Particle(PVector(x, y), col)
        }
    }

    override fun draw() {
        points.forEach { point ->
            stroke(point.colour)
            val forceScale = 1.0f
            val pos = point.pos
            val noiseVector = noiseVector(pos.x, pos.y, frameNumber.toFloat() * 1f)
            val v2 = noiseVector * forceScale
            val oldPos = pos.copy()
            pos.add(v2)
            drawLine(oldPos, pos)
        }
        frameNumber++
    }

    private fun drawLine(oldPos: PVector, pos: PVector) {
        line(
                oldPos.x * zoom + width / 2,
                oldPos.y * zoom + height / 2,
                pos.x * zoom + width / 2,
                pos.y * zoom + height / 2
        )
    }

    private fun noiseVector(x: Float, y: Float, z: Float): PVector {
        val distScale = 0.0025f
        val noise = noise(x * distScale, y * distScale, z * distScale)
        val theta = noise * Math.PI.toFloat() * 20.0f
        return PVector(1.0f, 0.0f).rotate(theta)
    }

    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if (event.key == 's') {
            saveFrame("frame-#######.png")
        }
    }

}

private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
