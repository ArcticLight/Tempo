package me.arcticlight.tempo.reswizard

import java.nio.file.{Files, FileStore, FileSystems}

import com.sun.nio.zipfs.ZipFileSystem

/**
 * Created by clivem on 2/7/15.
 */
object Unpacker {
  def UnpackResources(): Boolean = {
    import collection.JavaConverters._;
    val myURL = this.getClass().getProtectionDomain.getCodeSource.getLocation
    val mydir = java.nio.file.Paths.get(myURL.toURI)
    if(mydir.getFileSystem.isReadOnly) return false
    val zfs = FileSystems.newFileSystem(java.net.URI.create("jar:"+myURL.toURI.toString), Map[String,String]("create" -> "false", "encoding" -> "UTF-8").asJava)
    zfs.getRootDirectories.iterator.asScala.foreach(Files.list(_).iterator.asScala.foreach(println))
    false
  }
}
