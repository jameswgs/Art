package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.event.KeyEvent

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Subdivision1")
}

class Subdivision1 : PApplet() {

    private var frameNumber = 0
    private lateinit var subD1: SubD1
    private lateinit var gfx: PGraphics

    override fun settings() {
        fullScreen()
    }

    override fun setup() {

        val seed: Long = 11
        noiseSeed(seed)
        randomSeed(seed)
        background(0)

        gfx = createGraphics(width, height)

        subD1 = SubD1(gfx, this)
    }

    override fun draw() {

        if(subDivideLargestOnDraw) {
            subD1.subDivideLargest()
        }

        subD1.draw()
        blendMode(PConstants.REPLACE)
        image(gfx, 0.0f, 0.0f)

        frameNumber++
    }

    private var subDivideLargestOnDraw: Boolean = false

    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if (event.key == 's') {
            saveFrame("frame-#######.png")
        }
        if (event.key == 'p') {
            subD1.subDivide()
        }
        if (event.key == 'l') {
            subDivideLargestOnDraw = true
        }
    }

    override fun keyReleased(event: KeyEvent) {
        super.keyReleased(event)
        if (event.key == 'l') {
            subDivideLargestOnDraw = false
        }
    }

}

class SubD1(private val gfx: PGraphics, private val pApplet: PApplet) {

    private var shapes = emptyList<Shape>()

    init {
        with(gfx) {
            //val rect = makeRect(50.0f, 50.0f, width.toFloat() / 2 - 100.0f, height.toFloat() - 100.0f)
            val hex0 = makeHex(width * 0.2f, height * 0.5f, width * 0.15f, 6)
            val hex1 = makeHex(width * 0.5f, height * 0.5f, width * 0.15f, 8)
            val hex2 = makeHex(width * 0.8f, height * 0.5f, width * 0.15f, 10)
            shapes = listOf(hex0, hex1, hex2)
            colorMode(PConstants.HSB, 1.0f)
        }
    }

    fun draw() {
        gfx.draw {
            background(0)
            //stroke(0xFF000000.toInt())
            //strokeWeight(5.0f)
            noStroke()
            shapes.forEach { shape ->
                shape.draw(this)
            }
        }
    }

    fun subDivide() {
        shapes = shapes.flatMap {
            it.subDivideWithRndColor()
        }
    }

    private fun Shape.subDivideWithRndColor(): List<Shape> {
        val a = pApplet.random(0.5f, 0.5f)
        val b = pApplet.random(0.5f, 0.5f)
        gfx.colorMode(PConstants.HSB, 1.0f)
        val ca = gfx.color(rnd(1.0f), 0.75f, 0.75f)
        val cb = gfx.color(0.0f)
        return subDivideAB(a, b, ca, cb)
    }

    private fun rndMonoChrome() = pApplet.random(1.0f).let { if (it > 0.5f) gfx.color(0.0f) else gfx.color(1.0f) }

    private fun rnd(low: Float, high: Float) = pApplet.random(low, high)

    private fun rnd(fl: Float) = pApplet.random(fl)

    fun subDivideLargest() {
        val sorted = shapes.sortedBy { it.perimeter() }
        val last = sorted.last()
        shapes = sorted.dropLast(1) + last.subDivideWithRndColor()
    }

}


