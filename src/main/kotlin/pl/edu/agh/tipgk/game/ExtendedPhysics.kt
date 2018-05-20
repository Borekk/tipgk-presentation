package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*


class ExtendedPhysics : ApplicationAdapter(), InputProcessor {

    private lateinit var spriteBatch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body
    private lateinit var debugMatrix: Matrix4
    private lateinit var camera: OrthographicCamera

    private var torque = 0.0f

    companion object {
        var PIXELS_TO_METERS = 100f
    }

    override fun create() {
        spriteBatch = SpriteBatch()
        sprite = Sprite(drawRectangle(100, 100))

        sprite.setPosition(Gdx.graphics.width.toFloat() / 2, Gdx.graphics.height.toFloat() / 2)

        world = World(Vector2(0f, 0f), true)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((sprite.x + sprite.width / 2) /
                PIXELS_TO_METERS,
                (sprite.y + sprite.height / 2) / PIXELS_TO_METERS)

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(sprite.width / 2 / PIXELS_TO_METERS, sprite.height
                / 2 / PIXELS_TO_METERS)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 0.1f

        body.createFixture(fixtureDef)
        shape.dispose()

        //init transform
        body.setTransform(0f, 0f, 0f)

        Gdx.input.inputProcessor = this
        camera = OrthographicCamera(Gdx.graphics.width.toFloat()
                , Gdx.graphics.height.toFloat())
    }

    override fun render() {
        camera.update()
        world.step(1f / 60f, 6, 2)

        body.applyTorque(torque, true)

        sprite.setPosition((body.position.x * PIXELS_TO_METERS) - sprite.width / 2,
                (body.position.y * PIXELS_TO_METERS) - sprite.height / 2)

        sprite.rotation = Math.toDegrees(body.angle.toDouble()).toFloat()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        spriteBatch.projectionMatrix = camera.combined

        debugMatrix = spriteBatch.projectionMatrix.cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0f)

        spriteBatch.begin()

        spriteBatch.draw(sprite, sprite.x, sprite.y, sprite.originX,
                sprite.originY,
                sprite.width,
                sprite.height, sprite.scaleX,
                sprite.scaleY, sprite.rotation)

        spriteBatch.end()
    }

    override fun dispose() {
        world.dispose()
    }

    fun drawRectangle(width: Int, height: Int): Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.BLUE)
        pixmap.fillRectangle(0, 0, width, height)
        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        /*
            Add linear velocity in each direction (Input.Keys.Right|LEFT)
            Apply force for UP and DOWN (body.applyForceToCenter)
            Increment/decrement torque
            Reset velocity, force and torque on SPACE up
         */

        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }
}