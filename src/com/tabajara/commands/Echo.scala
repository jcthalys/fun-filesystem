package com.tabajara.commands

import com.tabajara.files.{Directory, File}
import com.tabajara.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {
    /*
      if no args, state
      else if just one arg, print to console
      else if multiple args
      {
        operator = next to last argument
        if >
          echo to a file (may create a file if not there)
        if >>
          append to a file
        else
          just echo everything to console
      }

     */

    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val content = createContent(args, args.length - 2)

      if (">>".equals(operator)) doEcho(state, content, filename, appendMode = true)
      else if (">".equals(operator)) doEcho(state, content, filename, appendMode = false)
      else state.setMessage(createContent(args, args.length))
    }
  }

  // topIndex NON_INCLUSIVE
  def createContent(args: Array[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, appendMode: Boolean): Directory = {
    /*
      if path is empty, then fail (currentDirectory)
      else if no more things to explore = path.tail.isEmpty
        find the file to create/add content to
        if file not found, create file
        else if the entry is actually a directory, then fail
        else
          replace or append content to the file
          replace the entry with the filename with the NEW file
      else
        find the next directory to navigate
        call gRAE recursively on that

        if recursive call failed, fail
        else replace entry with the NEW directory after the recursive call
     */
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val entry = currentDirectory.findEntry(path.head)

      if (entry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (entry.isDirectory) currentDirectory
      else if (appendMode) currentDirectory.replaceEntry(path.head, entry.asFile.appendContents(contents))
      else currentDirectory.replaceEntry(path.head, entry.asFile.setContents(contents))
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, appendMode)

      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, contents: String, filename: String, appendMode: Boolean) = {
    if (filename.contains(Directory.SEPARATOR))
      state.setMessage("Echo: file name should not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ filename, contents, appendMode)
      if (newRoot == state.root)
        state.setMessage(filename + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
  }
}
