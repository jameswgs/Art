package net.slenderloris.art

import processing.core.PApplet

fun main(args: Array<String>) {
    PApplet.main("net.slenderloris.art.Art")
}

class Art: PApplet() {

    override fun settings() {
        size(300, 300)
    }

    override fun setup() {
        fill(120f, 50f, 240f)
    }

    override fun draw() {
        ellipse((width / 2).toFloat(), (height / 2).toFloat(), PApplet.second().toFloat(), PApplet.second().toFloat())
    }

}