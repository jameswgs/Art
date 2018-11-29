package net.slenderloris.art

import processing.core.PVector

data class Edge(val first: PVector, val second: PVector)

fun Edge.center(): PVector {
    return first.copy().add(second.copy().sub(first).mult(0.5f))
}

fun Edge.between(f: Float): PVector {
    return first.copy().add(second.copy().sub(first).mult(f))
}

fun Edge.mag(): Float {
    return first.copy().sub(second).mag()
}