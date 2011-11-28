package fr.hsyl20.sme.tutorial

import com.jme3.app.SimpleApplication
import com.jme3.collision.CollisionResult
import com.jme3.collision.CollisionResults
import com.jme3.font.BitmapText
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Ray
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Node
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Sphere

import scala.collection.JavaConversions._
 
/** Sample 8 - how to let the user pick (select) objects in the scene 
 * using the mouse or key presses. Can be used for shooting, opening doors, etc. */
class HelloPicking extends SimpleApplication {

  var shootables:Node = null
  var mark:Geometry = null

  override def simpleInitApp: Unit = {
    initCrossHairs() // a "+" in the middle of the screen to help aiming
    initKeys()       // load custom key mappings
    initMark()       // a red sphere to mark the hit
 
    /** create four colored boxes and a floor to shoot at: */
    shootables = new Node("Shootables")
    rootNode.attachChild(shootables)
    shootables.attachChild(makeCube("a Dragon", -2f, 0f, 1f))
    shootables.attachChild(makeCube("a tin can", 1f, -2f, 0f))
    shootables.attachChild(makeCube("the Sheriff", 0f, 1f, -2f))
    shootables.attachChild(makeCube("the Deputy", 1f, 0f, -4f))
    shootables.attachChild(makeFloor())
    shootables.attachChild(makeCharacter())
  }

  /** Declaring the "Shoot" action and mapping to its triggers. */
  private def initKeys() {
    inputManager.addMapping("Shoot",
      new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
      new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
    inputManager.addListener(actionListener, "Shoot");
  }

  val actionListener = new ActionListener {
    def onAction(name:String, keyPressed:Boolean, tpf:Float) {
      if (name == "Shoot" && !keyPressed) {
        // 1. Reset results list.
        val results = new CollisionResults()
        // 2. Aim the ray from cam loc to cam direction.
        val ray = new Ray(cam.getLocation(), cam.getDirection())
        // 3. Collect intersections between Ray and Shootables in results list.
        shootables.collideWith(ray, results)
        // 4. Print the results
        println("----- Collisions? " + results.size() + "-----")
        results.zipWithIndex.foreach { case (r,i) =>
          // For each hit, we know distance, impact point, name of geometry.
          val dist = r.getDistance()
          val pt = r.getContactPoint()
          val hit = r.getGeometry().getName()
          println("* Collision #" + i)
          println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.")
        }
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          val closest = results.getClosestCollision()
          // Let's interact - we mark the hit with a red dot.
          mark.setLocalTranslation(closest.getContactPoint())
          rootNode.attachChild(mark)
        } else {
          // No hits? Then remove the red mark.
          rootNode.detachChild(mark)
        }
      }
    }
  }

  /** A cube object for target practice */
  protected def makeCube(name:String, x:Float, y:Float, z:Float): Geometry =  {
    val box = new Box(new Vector3f(x, y, z), 1, 1, 1)
    val cube = new Geometry(name, box)
    val mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.randomColor())
    cube.setMaterial(mat1)
    cube
  }
 
  /** A floor to show that the "shot" can go through several objects. */
  protected def makeFloor(): Geometry =  {
    val box = new Box(new Vector3f(0, -4, -5), 15, .2f, 15)
    val floor = new Geometry("the Floor", box)
    val mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.Gray)
    floor.setMaterial(mat1)
    floor
  }
 
  /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected def initMark(): Unit = {
    val sphere = new Sphere(30, 30, 0.2f)
    mark = new Geometry("BOOM!", sphere)
    val mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mark_mat.setColor("Color", ColorRGBA.Red)
    mark.setMaterial(mark_mat)
  }
 
  /** A centred plus sign to help the player aim. */
  protected def initCrossHairs(): Unit = {
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    val ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
      settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
    guiNode.attachChild(ch);
  }
 
  protected def makeCharacter(): Spatial =  {
    // load a character from jme3test-test-data
    val golem = assetManager.loadModel("Models/Oto/Oto.mesh.xml")
    golem.scale(0.5f)
    golem.setLocalTranslation(-1.0f, -1.5f, -0.6f)
 
    // We must add a light to make the model visible
    val sun = new DirectionalLight()
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f))
    golem.addLight(sun)
    golem
  }
}

object HelloPicking {
  def main(args:Array[String]): Unit = {

    import java.util.logging.{Logger,Level}
    Logger.getLogger("").setLevel(Level.WARNING);

    val app = new HelloPicking
    app.start
  }
}
