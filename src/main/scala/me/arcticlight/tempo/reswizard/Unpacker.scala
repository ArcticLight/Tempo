package me.arcticlight.tempo.reswizard

import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

import com.sun.nio.zipfs.ZipFileSystem

/**
 * Created by clivem on 2/7/15.
 */
object Unpacker {
  def UnpackResources(): Boolean = {
    import collection.JavaConverters._
    val myURL = this.getClass().getProtectionDomain.getCodeSource.getLocation
    val mydir = java.nio.file.Paths.get(myURL.toURI).getParent
    if(mydir.getFileSystem.isReadOnly) return false

    val ZURI = java.net.URI.create("jar:"+myURL.toURI.toString+"!/buildres")
    val zfs = FileSystems.newFileSystem(ZURI, Map("create" -> "false").asJava)
    val top = Paths.get(ZURI)
    Files.walkFileTree(top, new FileVisitor[Path] {
      import FileVisitResult._
      override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
        if(dir.toString.compareTo(top.toString) != 0)
          try {
            Files.createDirectory(mydir.resolve(top.relativize(dir).toString))
          } catch {
            case x: FileAlreadyExistsException =>
            case x: Throwable => println("Warn: " + x.getMessage())
          }
        CONTINUE
      }
      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = CONTINUE
      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        try {
          Files.copy(Files.newInputStream(file), mydir.resolve(top.relativize(file).toString))
        } catch {
          case x: FileAlreadyExistsException =>
          case x: Throwable => println("Warn: " + x.getMessage)
        }
        CONTINUE
      }
      override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = CONTINUE
    })
       //.iterator.asScala.foreach(Files.newDirectoryStream(_).iterator.asScala.foreach(println))
    false
  }
}
