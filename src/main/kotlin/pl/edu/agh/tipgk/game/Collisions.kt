package pl.edu.agh.tipgk.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*

class Collisions : ApplicationAdapter(), InputProcessor {

    private lateinit var batch: SpriteBatch
    private lateinit var sprite: Sprite
    private lateinit var world: World
    private lateinit var body: Body
    private lateinit var bodyEdgeScreen: Body
    private lateinit var debugRenderer: Box2DDebugRenderer
    private lateinit var debugMatrix: Matrix4
    private lateinit var camera: OrthographicCamera
    private lateinit var font: BitmapFont
    private lateinit var img: Texture


    private var torque = 0.0f
    private var drawSprite = true

    private val PIXELS_TO_METERS = 100f

    private val elapsed = 0f

    override fun create() {

        batch = SpriteBatch()
        img = drawRectangle(30, 30)
        sprite = Sprite(img)

        sprite.setPosition(-sprite.width / 2, -sprite.height / 2)

        world = World(Vector2(0f, -1f), true)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set((sprite.x + sprite.width / 2) / PIXELS_TO_METERS,
                (sprite.y + sprite.height / 2) / PIXELS_TO_METERS)

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox(sprite.width / 2f / PIXELS_TO_METERS, sprite.height
                / 2f / PIXELS_TO_METERS)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.density = 0.1f
        fixtureDef.restitution = 0.8f

        body.createFixture(fixtureDef)
        shape.dispose()

        val bodyDef2 = BodyDef()
        bodyDef2.type = BodyDef.BodyType.StaticBody
        val w = Gdx.graphics.width / PIXELS_TO_METERS
        // Set the height to just 50 pixels above the bottom of the screen so we can see the edge in the
        // debug renderer
        val h = Gdx.graphics.height / PIXELS_TO_METERS - 50 / PIXELS_TO_METERS
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

        Gdx.input.inputProcessor = this

        debugRenderer = Box2DDebugRenderer()
        font = BitmapFont()
        font.color = Color.BLACK
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun render() {
        camera.update()
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f / 120, 12, 6)

        body.applyTorque(torque, true)

        sprite.setPosition(body.position.x * PIXELS_TO_METERS - sprite.width / 2,
                body.position.y * PIXELS_TO_METERS - sprite.height / 2)
        sprite.rotation = Math.toDegrees(body.angle.toDouble()).toFloat()

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.projectionMatrix = camera.combined
        debugMatrix = batch.projectionMatrix.cpy().scale(PIXELS_TO_METERS,
                PIXELS_TO_METERS, 0f)
        batch.begin()

        if (drawSprite)
            batch.draw(sprite, sprite.x, sprite.y, sprite.originX,
                    sprite.originY,
                    sprite.width, sprite.height, sprite.scaleX, sprite.scaleY, sprite.rotation)

        font.draw(batch,
                "Restitution: " + body.fixtureList.first().restitution,
                (-Gdx.graphics.width / 2).toFloat(),
                (Gdx.graphics.height / 2).toFloat())
        batch.end()

        debugRenderer.render(world, debugMatrix)
    }

    override fun dispose() {
        world.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {


        if (keycode == Input.Keys.RIGHT)
            body.setLinearVelocity(1f, 0f)
        if (keycode == Input.Keys.LEFT)
            body.setLinearVelocity(-1f, 0f)

        if (keycode == Input.Keys.UP)
            body.applyForceToCenter(0f, 5f, true)
        if (keycode == Input.Keys.DOWN)
            body.applyForceToCenter(0f, -5f, true)

        // On brackets ( [ ] ) apply torque, either clock or counterclockwise
        if (keycode == Input.Keys.RIGHT_BRACKET)
            torque += 0.1f
        if (keycode == Input.Keys.LEFT_BRACKET)
            torque -= 0.1f

        // Remove the torque using backslash /
        if (keycode == Input.Keys.BACKSLASH)
            torque = 0.0f

        // If user hits spacebar, reset everything back to normal
        if (keycode == Input.Keys.SPACE || keycode == Input.Keys.NUM_2) {
            body.setLinearVelocity(0f, 0f)
            body.angularVelocity = 0f
            torque = 0f
            sprite.setPosition(0f, 0f)
            body.setTransform(0f, 0f, 0f)
        }

        if (keycode == Input.Keys.COMMA) {
            body.fixtureList.first().restitution = body.fixtureList.first().restitution - 0.1f
        }
        if (keycode == Input.Keys.PERIOD) {
            body.fixtureList.first().restitution = body.fixtureList.first().restitution + 0.1f
        }
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.NUM_1)
            drawSprite = !drawSprite

        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }


    // On touch we apply force from the direction of the users touch.
    // This could result in the object "spinning"
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        body.applyForce(1f, 1f, screenX.toFloat(), screenY.toFloat(), true)
        //body.applyTorque(0.4f,true);
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    fun drawRectangle(width: Int, height: Int): Texture {
        val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color.BLUE)
        pixmap.fillRectangle(0, 0, width, height)
        val texture = Texture(pixmap)
        pixmap.dispose()

        return texture
    }
}