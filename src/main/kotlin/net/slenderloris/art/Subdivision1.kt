package net.slenderloris.art

import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.core.PVector
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

        subD1.draw()
        blendMode(PConstants.REPLACE)
        image(gfx, 0.0f, 0.0f)

        frameNumber++
    }

    override fun keyPressed(event: KeyEvent) {
        super.keyPressed(event)
        if (event.key == 's') {
            saveFrame("frame-#######.png")
        }
        if (event.key == 'p') {
            subD1.subDivide()
        }
    }

}

class SubD1(private val gfx: PGraphics, private val pApplet: PApplet) {

    private var shapes = emptyList<Shape>()

    init {
        with(gfx) {
            shapes = listOf(makeRect(50.0f, 50.0f, width.toFloat()-100.0f, height.toFloat()-100.0f))
            colorMode(PConstants.HSB, 1.0f)
        }
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
            stroke(0xFFFF0000.toInt())
            strokeWeight(4.0f)
            shapes.forEach { shape ->
                fill(shape.col)
                beginShape()
                shape.verts.forEach {
                    vertex(it.x, it.y)
                }
                endShape()
            }
        }
    }

    fun subDivide() {
        shapes = shapes.flatMap {
            val a = pApplet.random(0.45f, 0.55f)
            val b = pApplet.random(0.45f, 0.55f)
            gfx.colorMode(PConstants.HSB, 1.0f)
            val ca = gfx.color(pApplet.random(1.0f), 1.0f, 1.0f)
            val cb = gfx.color(pApplet.random(1.0f), 1.0f, 1.0f)
            it.subDivideAB(a, b, ca, cb)
        }
    }

}

typealias Edge = Pair<PVector, PVector>

private fun PGraphics.draw(function: PGraphics.() -> Unit) {
    beginDraw()
    function()
    endDraw()
}

private fun Shape.subDivide(): List<Shape> {
    val edges = edges()
    val numberedEdges = edges.mapIndexed { index, edge -> index to edge }
    val longest = numberedEdges.sortedBy { it.second.mag() }.last()
    val opposite = numberedEdges[(longest.first + (edges.size / 2)).rem(edges.size)]
    val longestC = longest.second.center()
    val oppositeC = opposite.second.center()

    val newFirstIndex = (longest.first+1).rem(verts.size)
    val reOrderedVerts = verts.subList(newFirstIndex, verts.size) + verts.subList(0, newFirstIndex)
    val v1 = reOrderedVerts.subList(0, verts.size/2) + oppositeC + longestC
    val v2 = reOrderedVerts.subList(verts.size/2, verts.size) + longestC + oppositeC
    return listOf(Shape(v1), Shape(v2))
}

private fun Shape.subDivideAB(a: Float, b: Float, ca: Int, cb: Int): List<Shape> {
    val edges = edges()
    val numberedEdges = edges.mapIndexed { index, edge -> index to edge }
    val longest = numberedEdges.sortedBy { it.second.mag() }.last()
    val opposite = numberedEdges[(longest.first + (edges.size / 2)).rem(edges.size)]
    val longestC = longest.second.between(a)
    val oppositeC = opposite.second.between(b)

    val newFirstIndex = (longest.first+1).rem(verts.size)
    val reOrderedVerts = verts.subList(newFirstIndex, verts.size) + verts.subList(0, newFirstIndex)
    val v1 = reOrderedVerts.subList(0, verts.size/2) + oppositeC + longestC
    val v2 = reOrderedVerts.subList(verts.size/2, verts.size) + longestC + oppositeC
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

    fun edges(): List<Edge> {
        return verts.zip(verts.subList(1, verts.size)) + (verts.last() to verts.first())
    }

}