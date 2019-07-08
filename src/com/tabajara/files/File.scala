package com.tabajara.files

import com.tabajara.filesystem.FileSystemException


class File(override val parentPath: String, override val name: String, contents: String)
  extends DirEntry(parentPath, name) {

  def appendContents(newContents: String): DirEntry =
    setContents(contents + "\n" + newContents)

  def setContents(newContents: String): File =
    new File(parentPath, name, newContents)

  override def asDirectory: Directory =
    throw new FileSystemException("file can not be converted to a directory")

  override def getType: String = "File"

  override def asFile: File = this

  override def isDirectory: Boolean = false

  override def isFile: Boolean = true
}


object File {

  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}