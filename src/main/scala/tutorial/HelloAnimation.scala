package fr.hsyl20.sme.tutorial

import com.jme3.animation.AnimChannel
import com.jme3.animation.AnimControl
import com.jme3.animation.AnimEventListener
import com.jme3.animation.LoopMode
import com.jme3.app.SimpleApplication
import com.jme3.input.KeyInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.light.DirectionalLight
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Node
import com.jme3.scene.debug.SkeletonDebugger
import com.jme3.material.Material
 
/** Sample 7 - how to load an OgreXML model and play an animation,
 * using channels, a controller, and an AnimEventListener. */
class HelloAnimation extends SimpleApplication with AnimEventListener {

  private var channel:AnimChannel = null
  private var control:AnimControl = null
  var player:Node = null
 
  override def simpleInitApp: Unit = {
    viewPort.setBackgroundColor(ColorRGBA.LightGray)
    initKeys()
    val dl = new DirectionalLight()
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal())
    rootNode.addLight(dl)
    player = assetManager.loadModel("Models/Oto/Oto.mesh.xml").asInstanceOf[Node]
    player.setLocalScale(0.5f)
    rootNode.attachChild(player)
    control = player.getControl(classOf[AnimControl])
    control.addListener(this)
    channel = control.createChannel()
    channel.setAnim("stand")

    /* Skeleton debugger */
    val skeletonDebug = new SkeletonDebugger("skeleton", control.getSkeleton())
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Green)
    mat.getAdditionalRenderState().setDepthTest(false)
    skeletonDebug.setMaterial(mat)
    player.attachChild(skeletonDebug)

    /* List of animation names*/
    import scala.collection.JavaConversions._
    println("Available animations:")
    control.getAnimationNames.foreach(println)
  }

  def onAnimCycleDone(control:AnimControl, channel:AnimChannel, animName:String) {
    if (animName == "Walk") {
      channel.setAnim("stand", 0.50f)
      channel.setLoopMode(LoopMode.DontLoop)
      channel.setSpeed(1f)
    }
  }
 
  def onAnimChange(control:AnimControl, channel:AnimChannel, animName:String) {
    // unused
  }
 
  /** Custom Keybinding: Map named actions to inputs. */
  private def initKeys() {
    inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE))
    inputManager.addListener(actionListener, "Walk")
  }

  val actionListener = new ActionListener {
    def onAction(name:String, keyPressed:Boolean, tpf:Float) {
      if (name == "Walk" && !keyPressed) {
        if (channel.getAnimationName() != "Walk") {
          channel.setAnim("Walk", 0.50f);
          channel.setLoopMode(LoopMode.Loop);
        }
      }
    }
  }
}

object HelloAnimation {
  def main(args:Array[String]): Unit = {
    val app = new HelloAnimation
    app.start
  }
}
