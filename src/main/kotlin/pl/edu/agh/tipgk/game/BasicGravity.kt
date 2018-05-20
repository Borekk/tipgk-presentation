package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*



class BasicGravity : ApplicationAdapter() {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body


    override fun create() {
        spriteBatch = SpriteBatch()
        sprite = Sprite(drawCircle())

        sprite.setPosition(200f, 200f)

        //create World
        world = World(Vector2(0f, -98f), true)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(sprite.x, sprite.y)
        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(sprite.width/2f, sprite.height/2f)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f

        val fixture = body.createFixture(fixtureDef)

        shape.dispose()
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        world.step(Gdx.graphics.deltaTime, 6, 2)

        sprite.setPosition(body.position.x, body.position.y)

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