package fr.hsyl20.sme.tutorial

import com.jme3.app.SimpleApplication
import com.jme3.material.Material
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.math.ColorRGBA
import com.jme3.input.KeyInput
import com.jme3.input.MouseInput
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.AnalogListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.input.controls.MouseButtonTrigger
 
/** Sample 5 - how to map keys and mousebuttons to actions */
class HelloInput extends SimpleApplication {
 
  protected var player:Geometry = null
  var isRunning: Boolean = true

  override def simpleInitApp: Unit = {
    val b = new Box(Vector3f.ZERO, 1, 1, 1)
    player = new Geometry("Player", b)
    val mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat.setColor("Color", ColorRGBA.Blue)
    player.setMaterial(mat)
    rootNode.attachChild(player)
    initKeys() // load my custom keybinding
  }

  /** Custom Keybinding: Map named actions to inputs. */
  def initKeys(): Unit = {
    // You can map one or several inputs to one named action
    inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P))
    inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J))
    inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_K))
    inputManager.addMapping("Rotate", new KeyTrigger(KeyInput.KEY_SPACE),
                                      new MouseButtonTrigger(MouseInput.BUTTON_LEFT))
    // Add the names to the action listener.
    inputManager.addListener(actionListener, "Pause")
    inputManager.addListener(analogListener, "Left", "Right", "Rotate")
  }

  private val actionListener = new ActionListener {
    def onAction(name:String, keyPressed:Boolean, tpf:Float) {
      if (name == "Pause" && !keyPressed) {
        isRunning = !isRunning
      }
    }
  }
 
  private val analogListener = new AnalogListener {
    def onAnalog(name:String, value:Float, tpf:Float) {
      if (isRunning) {
        name match {
          case "Rotate" => player.rotate(0, value*speed, 0)
          case "Right" => { 
            val v = player.getLocalTranslation()
            player.setLocalTranslation(v.x + value*speed, v.y, v.z)
          }
          case "Left" => {
            val v = player.getLocalTranslation()
            player.setLocalTranslation(v.x - value*speed, v.y, v.z)
          }
          case _ => ()
        }
      }
      else {
        println("Press P to unpause.")
      }
    }
  }
}

object HelloInput {
  def main(args:Array[String]): Unit = {

    import java.util.logging.{Logger,Level}
    Logger.getLogger("").setLevel(Level.WARNING);

    val app = new HelloInput
    app.start
  }
}
