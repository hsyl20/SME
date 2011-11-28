package fr.hsyl20.sme.tutorial

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
 
/** Sample 11 - playing 3D audio. */
class HelloAudio extends SimpleApplication {

  private var audio_gun:AudioNode = null
  private var audio_nature:AudioNode = null
  private var player:Geometry = null

  override def simpleInitApp: Unit = {
    flyCam.setMoveSpeed(40)
 
    /** just a blue box floating in space */
    val box1 = new Box(Vector3f.ZERO, 1, 1, 1)
    player = new Geometry("Player", box1)
    val mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat1.setColor("Color", ColorRGBA.Blue)
    player.setMaterial(mat1)
    rootNode.attachChild(player)
 
    /** custom init methods, see below */
    initKeys()
    initAudio()
  }

  /** We create two audio nodes. */
  private def initAudio():Unit =  {
    /* gun shot sound is to be triggered by a mouse click. */
    audio_gun = new AudioNode(assetManager, "Sound/Effects/Gun.wav", false)
    audio_gun.setLooping(false)
    audio_gun.setVolume(2)
    rootNode.attachChild(audio_gun)
 
    /* nature sound - keeps playing in a loop. */
    audio_nature = new AudioNode(assetManager, "Sound/Environment/Nature.ogg", false)
    audio_nature.setLooping(true)  // activate continuous playing
    audio_nature.setPositional(true)
    audio_nature.setLocalTranslation(Vector3f.ZERO.clone())
    audio_nature.setVolume(3)
    rootNode.attachChild(audio_nature)
    audio_nature.play() // play continuously!
  }
 
  /** Declaring "Shoot" action, mapping it to a trigger (mouse click). */
  private def initKeys(): Unit = {
    inputManager.addMapping("Shoot", new MouseButtonTrigger(0))
    inputManager.addListener(actionListener, "Shoot")
  }
 
  /** Defining the "Shoot" action: Play a gun sound. */
  val actionListener = new ActionListener {
    override def onAction(name:String, keyPressed:Boolean, tpf:Float): Unit = {
      if (name.equals("Shoot") && !keyPressed) {
        audio_gun.playInstance() // play each instance once!
      }
    }
  }
 
  /** Move the listener with the a camera - for 3D audio. */
  override def simpleUpdate(tpf:Float):Unit =  {
    listener.setLocation(cam.getLocation())
    listener.setRotation(cam.getRotation())
  }
}

object HelloAudio {
  def main(args:Array[String]): Unit = {

    import java.util.logging.{Logger,Level}
    Logger.getLogger("").setLevel(Level.WARNING);

    val app = new HelloAudio
    app.start
  }
}
