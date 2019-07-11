package com.tabajara.filesystem

import com.tabajara.commands.Command
import com.tabajara.files.Directory


object FileSystem extends App {

  val root = Directory.ROOT
  io.Source.stdin.getLines().foldLeft(State(root, root))((concurrentState, newLine) => {
    concurrentState.show
    Command.from(newLine).apply(concurrentState)
  })
}
