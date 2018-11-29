package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.event.KeyEvent
import kotlin.math.pow
import kotlin.math.sin

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Waves1")
}

class Waves1 : PApplet() {

    private var frameNumber = 0
    private lateinit var gfx: PGraphics
    private lateinit var waves: Waves

    override fun settings() {
        fullScreen()
    }

    override fun setup() {

        val seed: Long = 5
        noiseSeed(seed)
        randomSeed(seed)
        background(0)

        gfx = createGraphics(width, height)

        waves = Waves(gfx, this)
    }

    override fun draw() {

        waves.draw(frameNumber.toFloat())
        blendMode(PConstants.REPLACE)
        image(gfx, 0.0f, 0.0f)

        frameNumber++
    }


    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if (event.key == 's') {
            saveFrame("frame-#######.png")
        }
    }

}

class Wave(private val amplitude: Float, private val wavelength: Float, private val velocity: Float) {

    fun get(x: Float, time: Float): Float {
        val x1 = x + time * velocity
        return sin(x1 / wavelength) * amplitude
    }

}

class Waves(private val gfx: PGraphics, private val pApplet: PApplet) {

    private val waves = (0 until 6).map {
        val p = 2.0f.pow(it.toFloat())
        val ra = pApplet.random(0.5f, 1.0f)
        val rw = pApplet.random(0.5f, 1.0f)
        val velocity = pApplet.random(-1.0f, 1.0f)
        val amplitude = 150.0f * ra / p
        val wavelength = 200.0f * rw / p
        Wave(amplitude, wavelength, velocity)
    }

    fun draw(frame: Float) {

        gfx.draw {

            background(0)

            colorMode(PConstants.HSB, 1.0f)

            val startY = height / 4
            val endY = (height / 4) * 3
            val startX = width / 4
            val endX = (width / 4) * 3

            (startY..endY).step(50).forEach { iy ->

                val fadeCol = unitDistance(startY, endY, iy)
                stroke(color(fadeCol))
                fill(color(1.0f, 1.0f, 1.0f, fadeCol))


                gfx.shape {

                    vertex(startX.toFloat(), iy + 1000.0f)

                    (startX..endX).step(2).map { ix ->
                        val x = ix.toFloat()
                        val offSetFrame = frame - iy.toFloat() * 0.5f
                        val w = waves.fold(0.0f) { acc, wave -> acc + wave.get(x, offSetFrame) }
                        val y = iy + w * attenuation(x)
                        vertex(x, y)
                    }

                    vertex(endX.toFloat(), iy + 1000.0f)
                }

            }

        }

    }

    private fun unitDistance(a: Int, b: Int, x: Int): Float {
        return (x - a).toFloat() / (b - a).toFloat()
    }

    private fun PGraphics.attenuation(x: Float): Float {
        val fl = x / width.toFloat()
        val f2 = (fl * PConstants.TWO_PI) - PConstants.HALF_PI
        val f3 = (1.0f + sin(f2)) * -0.5f
        return f3.pow(4.0f)
    }

}