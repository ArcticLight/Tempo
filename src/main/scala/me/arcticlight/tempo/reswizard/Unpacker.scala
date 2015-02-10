package me.arcticlight.tempo.reswizard

import java.io.{File, IOException}
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes

/**
 * Unpacker is a utility class capable of unpacking
 * resources included in a Jar file into the local filesystem
 * near the Jar when it is run. This is useful for, e.g., unpacking
 * Native dependencies such as LWJGL's natives before running code
 * that requires it.
 */
object Unpacker {
  def UnpackResources(): Unit = {

    import collection.JavaConverters._
    val myURL = this.getClass.getProtectionDomain.getCodeSource.getLocation
    val myDir = java.nio.file.Paths.get(myURL.toURI).getParent

    me.arcticlight.tempo.reswizard.UnpackerJavaCallouts.mangleClassloader(myDir.resolve("native").toString)

    if (myDir.getFileSystem.isReadOnly) return

    val ZURI = java.net.URI.create("jar:" + myURL.toURI.toString + "!/buildres")
    try {
      FileSystems.newFileSystem(ZURI, Map("create" -> "false").asJava)
    } catch {
      case x: FileSystemAlreadyExistsException =>
      case x: Throwable => System.err.println(s"Warning: ${x.getMessage}")
    }
    val top = Paths.get(ZURI)
    Files.walkFileTree(top, new FileVisitor[Path] {

      import FileVisitResult._

      override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
        if (dir.toString.compareTo(top.toString) != 0)
          try {
            Files.createDirectory(myDir.resolve(top.relativize(dir).toString))
          } catch {
            case x: FileAlreadyExistsException =>
            case x: Throwable => println("Warn: " + x.getMessage)
          }
        CONTINUE
      }

      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = CONTINUE

      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        try {
          Files.copy(Files.newInputStream(file), myDir.resolve(top.relativize(file).toString))
        } catch {
          case x: FileAlreadyExistsException =>
          case x: Throwable => println("Warn: " + x.getMessage)
        }
        CONTINUE
      }

      override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = CONTINUE
    })
  }
}