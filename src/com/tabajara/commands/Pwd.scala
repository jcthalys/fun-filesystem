package com.tabajara.commands

import com.tabajara.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
