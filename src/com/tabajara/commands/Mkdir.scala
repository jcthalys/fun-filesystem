package com.tabajara.commands

import com.tabajara.files.{DirEntry, Directory}
import com.tabajara.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
