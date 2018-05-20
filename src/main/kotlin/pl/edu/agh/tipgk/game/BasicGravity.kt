package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*





/**
 * Created by Karol on 2018-05-12.
 */
class BasicGravity : ApplicationListener{

    private lateinit var batch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var smallSprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body
    private lateinit var bodyEdgeScreen: Body
    private lateinit var debugRenderer: Box2DDebugRenderer
    private lateinit var debugMatrix: Matrix4
    private lateinit var camera: OrthographicCamera

    override fun create() {
        batch = SpriteBatch()
        //Since we need to move object it needs to be placed in Sprite
        sprite = Sprite(drawCircle(20))
        smallSprite = Sprite(drawCircle(10))

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
        fixtureDef.density = 0.1f
        fixtureDef.restitution = 0.5f

        val fixture = body.createFixture(fixtureDef)

        val bodyDef2 = BodyDef()
        bodyDef2.type = BodyDef.BodyType.StaticBody
        val w = Gdx.graphics.width.toFloat()
        // Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        val h = Gdx.graphics.height.toFloat() - 150
        //bodyDef2.position.set(0,
//                h-10/PIXELS_TO_METERS);
        bodyDef2.position.set(0f, 0f)
        val fixtureDef2 = FixtureDef()

        val edgeShape = EdgeShape()
        edgeShape.set(-w / 2, -h / 2, w / 2, -h / 2)
        fixtureDef2.shape = edgeShape

        bodyEdgeScreen = world.createBody(bodyDef2)
        bodyEdgeScreen.createFixture(fixtureDef2)
        edgeShape.dispose()

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose()
        debugRenderer = Box2DDebugRenderer()
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun render() {
        camera.update()
        world.step(Gdx.graphics.deltaTime, 6, 2)
        sprite.setPosition((body.position.x ) - sprite.width / 2,
                (body.position.y ) - sprite.height / 2)

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(sprite, sprite.x, sprite.y)
        batch.end()
    }

    override fun dispose() {
        world.dispose()
    }

    fun drawCircle(radius: Int ) : Texture {
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