package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PVector
import processing.event.KeyEvent

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Lines7")
}

class Lines7 : PApplet() {

    private lateinit var points: List<Particle>
    private var frameNumber = 0
    private var zoom = 2.0f

    private val noiseScaleStart = 0.0025f
    private val noiseScaleIncreaseFactor = 0.0025f
    private val timeScale = 0.3f
    private val forceScale = 0.25f
    private val bias = PVector(0.0f, 0.0f)
    private val spread = 150.0f


    override fun settings() {
        size(1200, 1200)
    }

    override fun setup() {
        val seed: Long = 5
        noiseSeed(seed)
        randomSeed(seed)

        background(0)
        colorMode(HSB, 255.0f)
        blendMode(ADD)

        points = emptyList()

        while(points.size < 10000) {
            points += createRndParticle1()
            points += createRndParticle2()
        }
    }

    private fun createRndParticle1(): Particle {
        val x = random(spread) - spread / 2 - 100.0f
        val y = random(spread) - spread / 2
        val col = color(0.0f, 255.0f, 255.0f, 3.0f)
        return Particle(PVector(x, y), col)
    }

    private fun createRndParticle2(): Particle {
        val x = random(spread) - spread / 2 + 100.0f
        val y = random(spread) - spread / 2
        val col = color(128.0f, 255.0f, 255.0f, 3.0f)
        return Particle(PVector(x, y), col)
    }


    override fun draw() {

        val fFrameNo = frameNumber.toFloat()

        val thetaStart = -PConstants.HALF_PI
        val noiseScaleTheta = noiseScaleIncreaseFactor * fFrameNo + thetaStart
        val sinScale = 1.0f
        val noiseScaleFactor = (sinScale + 1.0f) + sin(noiseScaleTheta) * sinScale
        val noiseScale = noiseScaleStart * noiseScaleFactor

        println("noiseScaleTheta = $noiseScaleTheta, noiseScaleFactor = $noiseScaleFactor, noiseScale = $noiseScale")

        points.forEach { point ->
            stroke(point.colour)
            val pos = point.pos
            val noiseVector = noiseVector(pos.x, pos.y, fFrameNo * timeScale, noiseScale)
            val v2 = noiseVector * forceScale
            val oldPos = pos.copy()
            pos.add(v2).add(bias)
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

    private fun noiseVector(x: Float, y: Float, z: Float, noiseScale: Float): PVector {
        val noise = noise(x * noiseScale, y * noiseScale, z * noiseScale)
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
