package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape





/**
 * Created by Karol on 2018-05-12.
 */
class BasicGravity : ApplicationListener{

    private lateinit var batch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var smallSprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body

    override fun create() {
        batch = SpriteBatch()
        //Since we need to move object it needs to be placed in Sprite
        sprite = Sprite(prepareCircleTexture(20))
        smallSprite = Sprite(prepareCircleTexture(10))

        sprite.setPosition(100f, Gdx.graphics.height.toFloat() - sprite.height)
        smallSprite.setPosition(200f, Gdx.graphics.height.toFloat() - smallSprite.height)

        //World is the core of simulation, input vector is gravity
        world = World( Vector2(0f, -98f), true)

        val bodyDef : BodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody

        //defined body should have the same position as sprite
        bodyDef.position.set(sprite.x, sprite.y)

        body = world.createBody(bodyDef)


        val shape = PolygonShape()

        shape.setAsBox(sprite.width/2, sprite.height/2)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 1f

        val fixture = body.createFixture(fixtureDef)

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose()
    }

    override fun render() {
        world.step(Gdx.graphics.deltaTime, 6, 2)
        sprite.setPosition(body.position.x, body.position.y)

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(sprite, sprite.x, sprite.y)
        batch.draw(smallSprite, smallSprite.x, smallSprite.y)
        batch.end()
    }

    override fun dispose() {
        world.dispose()
    }

    fun prepareCircleTexture(radius: Int ) : Texture {
        val pixmap = Pixmap(radius *2, radius *2, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.GREEN)
        pixmap.fillCircle(radius, radius, radius)
        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }
}