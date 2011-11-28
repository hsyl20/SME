package fr.hsyl20.sme.tutorial

import com.jme3.app.SimpleApplication
import com.jme3.light.DirectionalLight
import com.jme3.material.Material
import com.jme3.material.RenderState.BlendMode
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Sphere
import com.jme3.texture.Texture
import com.jme3.util.TangentBinormalGenerator
import com.jme3.renderer.queue.RenderQueue.Bucket
 
/** Sample 6 - how to give an object's surface a material and texture.
 * How to make objects transparent, or let colors "leak" through partially
 * transparent textures. How to make bumpy and shiny surfaces.  */
class HelloMaterial extends SimpleApplication {
 
  override def simpleInitApp: Unit = {
    /** A simple textured cube -- in good MIP map quality. */
    val boxshape1 = new Box(new Vector3f(-3f,1.1f,0f), 1f,1f,1f)
    val cube = new Geometry("My Textured Box", boxshape1)
    val mat_stl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    val tex_ml = assetManager.loadTexture("Interface/Logo/Monkey.jpg")
    mat_stl.setTexture("ColorMap", tex_ml)
    cube.setMaterial(mat_stl)
    rootNode.attachChild(cube)
 
    /** A translucent/transparent texture, similar to a window frame. */
    val boxshape3 = new Box(new Vector3f(0f,0f,0f), 1f,1f,0.01f)
    val window_frame = new Geometry("window frame", boxshape3)
    val mat_tt = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat_tt.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"))
    mat_tt.getAdditionalRenderState().setBlendMode(BlendMode.Alpha)
    window_frame.setMaterial(mat_tt)
 
    /** Objects with transparency need to be in the render bucket for transparent objects: */
    window_frame.setQueueBucket(Bucket.Transparent)
    rootNode.attachChild(window_frame)
 
    /** A cube with base color "leaking" through a partially transparent texture */
    val boxshape4 = new Box(new Vector3f(3f,-1f,0f), 1f,1f,1f)
    val cube_leak = new Geometry("Leak-through color cube", boxshape4)
    val mat_tl = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md")
    mat_tl.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"))
    mat_tl.setColor("Color", new ColorRGBA(1f,0f,1f, 1f)) // purple
    cube_leak.setMaterial(mat_tl)
    rootNode.attachChild(cube_leak)
 
    /** A bumpy rock with a shiny light effect */
    val rock = new Sphere(32,32, 2f)
    val shiny_rock = new Geometry("Shiny rock", rock)
    rock.setTextureMode(Sphere.TextureMode.Projected) // better quality on spheres
    TangentBinormalGenerator.generate(rock)           // for lighting effect
    val mat_lit = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md")
    mat_lit.setTexture("DiffuseMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"))
    mat_lit.setTexture("NormalMap", assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"))
    mat_lit.setFloat("Shininess", 5f) // [1,128]
    shiny_rock.setMaterial(mat_lit)
    shiny_rock.setLocalTranslation(0,2,-2) // Move it a bit
    shiny_rock.rotate(1.6f, 0, 0)          // Rotate it a bit
    rootNode.attachChild(shiny_rock)
 
    /** Must add a light to make the lit object visible! */
    val sun = new DirectionalLight()
    sun.setDirection(new Vector3f(1,0,-2).normalizeLocal())
    sun.setColor(ColorRGBA.White)
    rootNode.addLight(sun)
  }
}

object HelloMaterial {
  def main(args:Array[String]): Unit = {

    import java.util.logging.{Logger,Level}
    Logger.getLogger("").setLevel(Level.WARNING);

    val app = new HelloMaterial
    app.start
  }
}
