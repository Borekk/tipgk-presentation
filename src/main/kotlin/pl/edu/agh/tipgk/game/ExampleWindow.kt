package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch


class ExampleWindow : ApplicationListener{

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        font.color = Color.RED
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(drawRectangle( 400, 200 ), 100f, 100f )
        font.draw(batch, "Hello World", 200f, 200f)
        batch.end()
    }

    fun drawRectangle( width: Int, height: Int ) : Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.BLUE)
        pixmap.fillRectangle(0, 0, width, height)
        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}
}