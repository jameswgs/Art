package net.slenderloris.art

fun Shape.subDivide(): List<Shape> {
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

fun Shape.subDivideAB(a: Float, b: Float, ca: Int, cb: Int): List<Shape> {
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