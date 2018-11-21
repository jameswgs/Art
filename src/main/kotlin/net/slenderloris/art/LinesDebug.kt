package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.core.PVector
import processing.event.KeyEvent
import kotlin.math.sin

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.LinesDebug")
}

class LinesDebug : PApplet() {

    private var frameNumber = 0

    private lateinit var smokey: Smokey

    override fun settings() {
        fullScreen()
    }

    override fun setup() {

        val gfx = createGraphics(width, height)

        smokey = Smokey(this, gfx)

        val seed: Long = 5
        noiseSeed(seed)
        randomSeed(seed)
        background(0)

        smokey.setup()
    }

    override fun draw() {
        smokey.draw(frameNumber)
        blendMode(PConstants.REPLACE)
        image(smokey.gfx, 0.0f, 0.0f)



        frameNumber++
    }

    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if (event.key == 's') {
            saveFrame("frame-#######.png")
        }
    }

}

class Smokey(private val pApplet: PApplet, val gfx: PGraphics) {

    private lateinit var points: List<Particle>
    private var zoom = 2.0f
    private val noiseScaleStart = 0.0025f
    private val noiseScaleIncreaseFactor = 0.0025f
    private val timeScale = 0.3f
    private val forceScale = 0.25f
    private val bias = PVector(0.0f, 0.0f)
    private val spread = 150.0f

    fun setup() {

        gfx.beginDraw()
        gfx.background(0)
        gfx.colorMode(PConstants.HSB, 255.0f)
        gfx.blendMode(PConstants.ADD)

        points = emptyList()

        while(points.size < 10000) {
            points += createRndParticle1()
            points += createRndParticle2()
        }

        gfx.endDraw()
    }

    fun draw(frameNumber: Int) {

        gfx.beginDraw()

        val fFrameNo = frameNumber.toFloat()

        val thetaStart = -PConstants.HALF_PI
        val noiseScaleTheta = noiseScaleIncreaseFactor * fFrameNo + thetaStart
        val sinScale = 1.0f
        val noiseScaleFactor = (sinScale + 1.0f) + sin(noiseScaleTheta) * sinScale
        val noiseScale = noiseScaleStart * noiseScaleFactor

        points.forEach { point ->
            gfx.stroke(point.colour)
            val pos = point.pos
            val noiseVector = pApplet.noiseVector(pos.x, pos.y, fFrameNo * timeScale, noiseScale)
            val v2 = noiseVector * forceScale
            val oldPos = pos.copy()
            pos.add(v2).add(bias)
            drawLine(oldPos, pos)
        }

        gfx.endDraw()

    }

    private fun createRndParticle1(): Particle {
        val x = pApplet.random(spread) - spread / 2 - 100.0f
        val y = pApplet.random(spread) - spread / 2
        val col = gfx.color(0.0f, 255.0f, 255.0f, 3.0f)
        return Particle(PVector(x, y), col)
    }

    private fun createRndParticle2(): Particle {
        val x = pApplet.random(spread) - spread / 2 + 100.0f
        val y = pApplet.random(spread) - spread / 2
        val col = gfx.color(128.0f, 255.0f, 255.0f, 3.0f)
        return Particle(PVector(x, y), col)
    }

    private fun drawLine(oldPos: PVector, pos: PVector) {
        gfx.line(
                oldPos.x * zoom + gfx.width / 2,
                oldPos.y * zoom + gfx.height / 2,
                pos.x * zoom + gfx.width / 2,
                pos.y * zoom + gfx.height / 2
        )
    }

}


private fun PApplet.noiseVector(x: Float, y: Float, z: Float, noiseScale: Float): PVector {
    val noise = noise(x * noiseScale, y * noiseScale, z * noiseScale)
    val theta = noise * Math.PI.toFloat() * 20.0f
    return PVector(1.0f, 0.0f).rotate(theta)
}


private operator fun PVector.times(a: Float): PVector {
    return this.mult(a)
}
