package pl.edu.agh.tipgk

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.edu.agh.tipgk.game.Collisions

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
//    LwjglApplication(ExampleWindow(), config)
//    LwjglApplication(BasicGravity(), config)
//    LwjglApplication(ExtendedPhysics(), config)
    LwjglApplication(Collisions(), config)
}