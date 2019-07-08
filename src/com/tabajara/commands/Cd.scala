package com.tabajara.commands

import com.tabajara.files.{DirEntry, Directory}
import com.tabajara.filesystem.State

import scala.annotation.tailrec

class Cd(dirName: String) extends Command {


  def doFindEntry(root: Directory, path: String): DirEntry = {
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if(nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    findEntryHelper(root, tokens)
  }

  override def apply(state: State): State = {
    val root = state.root
    val wd = state.wd

    val absolutePath =
      if (dirName.startsWith(Directory.SEPARATOR)) dirName
      else if (wd.isRoot) wd.path + dirName
      else wd.path + Directory.SEPARATOR + dirName

    val destinationDirectory = doFindEntry(root, absolutePath)

    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dirName + ": no such directory")
    else
      State(root, destinationDirectory.asDirectory)
  }
}
