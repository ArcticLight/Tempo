package me.arcticlight.tempo

/**
 * Created by clivem on 2/7/15.
 */
object Tempo {
  def main (args: Array[String]) {
    import org.lwjgl.Sys
    me.arcticlight.tempo.reswizard.Unpacker.UnpackResources()
    println(Sys.getVersion())
  }
}
