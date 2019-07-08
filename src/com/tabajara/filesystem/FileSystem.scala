package com.tabajara.filesystem

import java.util.Scanner

import com.tabajara.commands.Command
import com.tabajara.files.Directory


object FileSystem extends App {

  val firstRoot = Directory.ROOT
  var state = State(firstRoot, firstRoot)
  val scanner = new Scanner(System.in)

  while(true) {
    state.show
    state = Command.from(scanner.nextLine()).apply(state)
  }
}
