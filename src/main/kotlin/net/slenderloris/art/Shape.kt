package net.slenderloris.art

import processing.core.PGraphics
import processing.core.PVector

data class Shape(val verts: List<PVector>, val col: Int = 0) {

    val edges: List<Edge> by lazy {
        val pairs = verts.zip(verts.subList(1, verts.size)) + (verts.last() to verts.first())
        pairs.map { Edge(it.first, it.second) }
    }
}

fun Shape.perimeter(): Float {
    return edges.fold(0.0f) { acc, edge ->
        acc + edge.mag()
    }
}

fun Shape.draw(pGraphics: PGraphics) {
    pGraphics.fill(col)
    pGraphics.shape {
        verts.forEach {
            vertex(it.x, it.y)
        }
        verts.firstOrNull()?.let {
            vertex(it.x, it.y)
        }
    }
}
