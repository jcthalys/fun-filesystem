package com.tabajara.filesystem

import java.util.Scanner

import com.tabajara.commands.Command
import com.tabajara.files.Directory


object FileSystem extends App {

  val firstRoot = Directory.ROOT
  val scanner = new Scanner(System.in)
  var state = State(firstRoot, firstRoot)

  while (true) {
    state.show
    state = Command.from(scanner.nextLine()).apply(state)
  }
}
