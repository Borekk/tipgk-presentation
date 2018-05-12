package pl.edu.agh.tipgk

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.edu.agh.tipgk.game.BasicGravity
import pl.edu.agh.tipgk.game.Collisions
import pl.edu.agh.tipgk.game.ExampleWindow
import pl.edu.agh.tipgk.game.ExtendedPhysics

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
//    LwjglApplication(ExampleWindow(), config)
//    LwjglApplication(BasicGravity(), config)
//    LwjglApplication(ExtendedPhysics(), config)
    LwjglApplication(Collisions(), config)
}