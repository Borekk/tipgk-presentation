package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World



class BasicGravity : ApplicationAdapter() {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body


    override fun create() {
        spriteBatch = SpriteBatch()
        sprite = Sprite(drawCircle())

        sprite.setPosition(200f, 200f)

        //TODO: create world with gravity vector
        //world =

        //TODO: create BodyDef set its type to DynamicBody and Position to sprite body
        //val bodyDef =

        //body = world.createBody(bodyDef)

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        world.step(Gdx.graphics.deltaTime, 6, 2)

        //TODO: set sprite position after world step
        //sprite.setPosition( x, y)

        spriteBatch.begin()
        spriteBatch.draw(sprite, sprite.x, sprite.y)
        spriteBatch.end()
    }

    override fun dispose() {
        world.dispose()
    }

    private fun drawCircle(): Texture {
        val pixmap = Pixmap(150, 150, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.BROWN)
        pixmap.fillCircle(75, 75, 75)
        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture
    }
}