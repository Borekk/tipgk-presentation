package pl.edu.agh.tipgk

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.edu.agh.tipgk.game.ExampleWindow

fun main(args: Array<String>) {

    val config = LwjglApplicationConfiguration()
    LwjglApplication(ExampleWindow(), config)
}