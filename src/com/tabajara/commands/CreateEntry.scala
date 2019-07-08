package com.tabajara.commands

import com.tabajara.files.{DirEntry, Directory}
import com.tabajara.filesystem.State

abstract class CreateEntry(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
    if(path.isEmpty) currentDirectory.addEntry(newEntry)
    else {
      val oldEntry = currentDirectory.findEntry(path.head)
      currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry.asDirectory, path.tail, newEntry))
    }
  }

  def doCreateEntry(state: State, name: String): State = {
    val wd = state.wd

    val allDirsInPath = wd.getAllFoldersInPath()

    val newEntry: DirEntry = createSpecificEntry(state)

    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  override def apply(state: State): State = {
    val wd = state.wd

    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + "already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contains separators!")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    } else {
      doCreateEntry(state, name)
    }
  }

  def createSpecificEntry(state: State): DirEntry
}
