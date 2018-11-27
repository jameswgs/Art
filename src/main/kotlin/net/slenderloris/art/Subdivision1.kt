package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.core.PVector
import processing.event.KeyEvent
import kotlin.math.cos
import kotlin.math.sin

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
            val hex0 = makeHex(width * 0.25f, height * 0.5f, width * 0.1f, 6)
            val hex1 = makeHex(width * 0.5f, height * 0.5f, width * 0.1f, 8)
            val hex2 = makeHex(width * 0.75f, height * 0.5f, width * 0.1f, 10)
            shapes = listOf(hex0, hex1, hex2)
            colorMode(PConstants.HSB, 1.0f)
        }
    }

    private fun makeHex(x: Float, y: Float, radius: Float, sides: Int): Shape {
        val verts = (0 until sides).map {
            val rads = PConstants.TWO_PI / sides.toFloat() * it.toFloat()
            PVector(sin(rads) * radius + x, cos(rads) * radius + y)
        }
        return Shape(verts)
    }

    private fun makeRect(x: Float, y: Float, w: Float, h: Float): Shape {
        return Shape(listOf(
                PVector(x, y),
                PVector(x + w, y),
                PVector(x + w, y + h),
                PVector(x, y + h)
        ))
    }

    fun draw() {
        gfx.draw {
            background(0)
            stroke(0xFF000000.toInt())
            strokeWeight(5.0f)
            shapes.forEach { shape ->
                fill(shape.col)
                beginShape()
                shape.verts.forEach {
                    vertex(it.x, it.y)
                }
                shape.verts.firstOrNull()?.apply {
                    vertex(x, y)
                }
                endShape()
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

typealias Edge = Pair<PVector, PVector>

private fun PGraphics.draw(function: PGraphics.() -> Unit) {
    beginDraw()
    function()
    endDraw()
}

private fun Shape.subDivide(): List<Shape> {
    val edges = edges
    val numberedEdges = edges.mapIndexed { index, edge -> index to edge }
    val longest = numberedEdges.sortedBy { it.second.mag() }.last()
    val opposite = numberedEdges[(longest.first + (edges.size / 2)).rem(edges.size)]
    val longestC = longest.second.center()
    val oppositeC = opposite.second.center()

    val newFirstIndex = (longest.first + 1).rem(verts.size)
    val reOrderedVerts = verts.subList(newFirstIndex, verts.size) + verts.subList(0, newFirstIndex)
    val v1 = reOrderedVerts.subList(0, verts.size / 2) + oppositeC + longestC
    val v2 = reOrderedVerts.subList(verts.size / 2, verts.size) + longestC + oppositeC
    return listOf(Shape(v1), Shape(v2))
}

private fun Shape.subDivideAB(a: Float, b: Float, ca: Int, cb: Int): List<Shape> {
    val edges = edges
    val numberedEdges = edges.mapIndexed { index, edge -> index to edge }
    val longest = numberedEdges.sortedBy { it.second.mag() }.last()
    val opposite = numberedEdges[(longest.first + (edges.size / 2)).rem(edges.size)]
    val longestC = longest.second.between(a)
    val oppositeC = opposite.second.between(b)

    val newFirstIndex = (longest.first + 1).rem(verts.size)
    val reOrderedVerts = verts.subList(newFirstIndex, verts.size) + verts.subList(0, newFirstIndex)
    val v1 = reOrderedVerts.subList(0, verts.size / 2) + oppositeC + longestC
    val v2 = reOrderedVerts.subList(verts.size / 2, verts.size) + longestC + oppositeC
    return listOf(Shape(v1, ca), Shape(v2, cb))
}

private fun Edge.center(): PVector {
    return first.copy().add(second.copy().sub(first).mult(0.5f))
}

private fun Edge.between(f: Float): PVector {
    return first.copy().add(second.copy().sub(first).mult(f))
}

private fun Edge.mag(): Float {
    return first.copy().sub(second).mag()
}


data class Shape(val verts: List<PVector>, val col: Int = 0) {

    val edges: List<Edge> by lazy {
        verts.zip(verts.subList(1, verts.size)) + (verts.last() to verts.first())
    }

    fun perimeter(): Float {
        return edges.fold(0.0f) { acc, edge ->
            acc + edge.mag()
        }
    }

}