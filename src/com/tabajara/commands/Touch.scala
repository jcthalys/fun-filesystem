package com.tabajara.commands

import com.tabajara.files.{DirEntry, File}
import com.tabajara.filesystem.State

class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
