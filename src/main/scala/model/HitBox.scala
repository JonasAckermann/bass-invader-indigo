package model

import indigo._

trait HitBox {
    val width: Int
    val height: Int
    val location: Point 
    def isHit(otherBox: HitBox): Boolean = {
        val left = this.location.x - (width/2)
        val otherLeft = otherBox.location.x - (otherBox.width/2)
        val right = this.location.x + (width/2)
        val otherRight = otherBox.location.x + (otherBox.width/2)
        val top = this.location.y - (height/2)
        val otherTop = otherBox.location.y - (otherBox.height/2)
        val bottom = this.location.y + (height/2)
        val otherBottom = otherBox.location.y + (otherBox.height/2)
        (right > otherLeft && left < otherLeft && top < otherTop && bottom > otherTop) || 
        (right > otherLeft && left < otherLeft && top < otherBottom && bottom > otherBottom) ||
        (left < otherRight && right > otherRight && top < otherTop && bottom > otherTop) ||
        (left < otherRight && right > otherRight && top < otherBottom && bottom > otherBottom) || 
        (left > otherLeft && right < otherRight && top < otherTop && bottom > otherTop) ||
        (left > otherLeft && right < otherRight && top < otherBottom && bottom > otherBottom) || 
        (left > otherLeft && right < otherRight && top > otherTop && bottom < otherBottom) 
    }
}

case class GrandmaHitBox(val width: Int, val height: Int, val location: Point) extends HitBox
case class ShotHitBox(val width: Int, val height: Int, val location: Point) extends HitBox