package com.tabajara.commands

import com.tabajara.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
