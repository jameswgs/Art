package net.slenderloris.art

import processing.core.PGraphics

fun PGraphics.draw(function: PGraphics.() -> Unit) {
    beginDraw()
    function()
    endDraw()
}

fun PGraphics.shape(function: PGraphics.() -> Unit) {
    beginShape()
    function()
    endShape()
}