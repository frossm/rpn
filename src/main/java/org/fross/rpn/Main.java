/******************************************************************************
 * rpn.java
 * 
 * A simple console based RPN calculator with an optional persistent stack.
 * 
 *  Written by Michael Fross.  Copyright 2011-2019.  All rights reserved.
 *  
 *  License: 
 *  MIT License / https://opensource.org/licenses/MIT
 *  Please see included LICENSE.txt file for additional details
 *           
 ******************************************************************************/
package org.fross.rpn;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Stack;
import org.fusesource.jansi.Ansi;
import gnu.getopt.Getopt;

/**
 * Main - Main program execution class
 * 
 * @author michael.d.fross
 *
 */
public class Main {

	// Class Constants
	public static String VERSION;
	public static final String PROPERTIES_FILE = "rpn.properties";

	/**
	 * Main(): Start of program and holds main command loop
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Console con = null;
		Stack<Double> calcStack = new Stack<Double>();
		Stack<Double> calcStack2 = new Stack<Double>();
		Stack<Double> undoStack = new Stack<Double>();
		boolean ProcessCommandLoop = true;
		int optionEntry;

		// Process application level properties file
		// Update properties from Maven at build time:
		// https://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
		try {
			InputStream iStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties prop = new Properties();
			prop.load(iStream);
			VERSION = prop.getProperty("Application.version");
		} catch (IOException ex) {
			Output.fatalerror("Unable to read property file '" + PROPERTIES_FILE + "'", 3);
		}

		// Initialize the console used for command input
		con = System.console();
		if (con == null) {
			Output.printError("FATAL :  Could not initialize OS Console for data input");
			System.exit(1);
		}

		// Process Command Line Options and set flags where needed
		Getopt optG = new Getopt("rpn", args, "Dl:h?");
		while ((optionEntry = optG.getopt()) != -1) {
			switch (optionEntry) {
			case 'D': // Debug Mode
				Debug.Enable();
				break;
			case 'l':
				Prefs.SetLoadedStack(String.valueOf(optG.getOptarg()));
				break;
			case '?': // Help
			case 'h':
				Help.Display();
				System.exit(0);
				break;
			default:
				Output.printError("Unknown Command Line Option: '" + (char) optionEntry + "'");
				Help.Display();
				System.exit(0);
				break;
			}
		}

		// Display some useful information about the environment if in Debug Mode
		Debug.Print("System Information:");
		Debug.Print(" - class.path:     " + System.getProperty("java.class.path"));
		Debug.Print("  - java.home:      " + System.getProperty("java.home"));
		Debug.Print("  - java.vendor:    " + System.getProperty("java.vendor"));
		Debug.Print("  - java.version:   " + System.getProperty("java.version"));
		Debug.Print("  - os.name:        " + System.getProperty("os.name"));
		Debug.Print("  - os.version:     " + System.getProperty("os.version"));
		Debug.Print("  - os.arch:        " + System.getProperty("os.arch"));
		Debug.Print("  - user.name:      " + System.getProperty("user.name"));
		Debug.Print("  - user.home:      " + System.getProperty("user.home"));
		Debug.Print("  - user.dir:       " + System.getProperty("user.dir"));
		Debug.Print("  - file.separator: " + System.getProperty("file.separator"));
		Debug.Print("  - library.path:   " + System.getProperty("java.library.path"));
		Debug.Print("\nCommand Line Options");
		Debug.Print("  -D:  " + Debug.Query());
		Debug.Print("  -l:  " + Prefs.QueryLoadedStack());

		// Pull the existing stacks from the preferences if they exist
		calcStack = Prefs.RestoreStack("1");
		calcStack2 = Prefs.RestoreStack("2");

		// Populate the undoStack with the contents of the current stack
		undoStack = (Stack<Double>) calcStack.clone();

		// Display output header information
		Output.printColorln(Ansi.Color.CYAN, "+----------------------------------------------------------------------+");
		Output.printColorln(Ansi.Color.CYAN, "|                           RPN Calculator                 v" + VERSION + " |");
		Output.printColorln(Ansi.Color.CYAN, "|           Written by Michael Fross.  All rights reserved             |");
		Output.printColorln(Ansi.Color.CYAN, "|                 Enter command 'h' for help details                   |");
		Output.displayDashedNameLine();

		// Start Main Command Loop
		while (ProcessCommandLoop == true) {
			String cmdInput = null;

			// Display the current stack
			for (int i = 0; i <= calcStack.size() - 1; i++) {
				// Display Stack Number
				String sn = String.format("%02d:   ", calcStack.size() - i);
				Output.printColor(Ansi.Color.CYAN, sn);

				// Display Stack Value
				Output.printColorln(Ansi.Color.WHITE, Math.Comma(calcStack.get(i)));
			}

			// Input command/number from user
			Output.printColor(Ansi.Color.YELLOW, "\n>>  ");
			cmdInput = con.readLine();

			// Toggle Debug Mode
			if (cmdInput.matches("[Dd][Ee][Bb][Uu][Gg]")) {
				if (Debug.Query()) {
					Debug.Disable();
					Output.printColorln(Ansi.Color.RED, "Debug Disabled");
				} else {
					Debug.Enable();
					Debug.Print("Debug Enabled");
				}

				//////////////////////////////////////////////////////////////////
				// Load a new stack into the calculator
				// Command: load
			} else if (cmdInput.matches("^load .+")) {
				// Save current Stack
				Prefs.SaveStack(calcStack, "1");
				Prefs.SaveStack(calcStack2, "2");

				// Set new stack
				String newStack = cmdInput.toString().substring(5);
				Debug.Print("Loading new stack: '" + newStack + "'");
				Prefs.SetLoadedStack(newStack);

				// Load new stack
				calcStack = Prefs.RestoreStack("1");
				calcStack2 = Prefs.RestoreStack("2");

				//////////////////////////////////////////////////////////////////
				// Display program version
				// Command: ver
			} else if (cmdInput.matches("^ver.*")) {
				Output.printColorln(Ansi.Color.RED, "Version: v" + VERSION);

				//////////////////////////////////////////////////////////////////
				// Display help information
				// Command: h or ?
			} else if (cmdInput.matches("^[Hh?]")) {
				Debug.Print("Displaying Help");
				Help.Display();

				//////////////////////////////////////////////////////////////////
				// Process Exit
				// Command: x
			} else if (cmdInput.matches("^[Xx]")) {
				Debug.Print("Exiting Command Loop");
				ProcessCommandLoop = false;

				//////////////////////////////////////////////////////////////////
				// Show Undo Stack
				// Command: queryundo
			} else if (cmdInput.matches("^queryundo")) {
				Output.printColorln(Ansi.Color.CYAN, "Current undoStack:");
				for (int j = 0; j <= undoStack.size() - 1; j++) {
					String sn = String.format("%02d:   ", undoStack.size() - j);
					Output.printColor(Ansi.Color.CYAN, sn);
					Output.printColorln(Ansi.Color.CYAN, Math.Comma(undoStack.get(j)));
				}

				//////////////////////////////////////////////////////////////////
				// Process Undo
				// Command: u
			} else if (cmdInput.matches("^[Uu]")) {
				Debug.Print("Undoing last command");

				// Backup current stack so we can undo the undo
				Stack<Double> calcStackTemp = (Stack<Double>) calcStack.clone();

				// Replace current stack with the undo stack
				calcStack = (Stack<Double>) undoStack.clone();

				// Add original stack to the undo stack
				undoStack = (Stack<Double>) calcStackTemp.clone();

				//////////////////////////////////////////////////////////////////
				// Clear stack and screen
				// Command: c
			} else if (cmdInput.matches("^[Cc]")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Clearing Stack");
				calcStack.clear();
				for (int clearcounter = 0; clearcounter <= 200; clearcounter++)
					Output.println("");

				//////////////////////////////////////////////////////////////////
				// Delete a stack item
				// Command: d
			} else if (cmdInput.matches("^[Dd].*")) {
				// Default to deleting the top of the stack
				int lineToDelete = 0;

				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				// Determine the line number to delete
				try {
					if (cmdInput.substring(1).trim().length() == 0) {
						lineToDelete = 1;
					} else {
						lineToDelete = Integer.parseInt(cmdInput.substring(1).trim());
					}

					// Ensure the number entered is is valid
					if (lineToDelete < 1 || lineToDelete > calcStack.size()) {
						Output.printError("Invalid line number entered: " + lineToDelete);
					} else {
						Debug.Print("Deleting line number: " + lineToDelete);
						calcStack = StackOps.StackDeleteItem(calcStack, (lineToDelete - 1));
					}

				} catch (Exception e) {
					Output.printError("Error parsing line number for element delete: '" + cmdInput.substring(1).trim() + "'");
					Debug.Print(e.getMessage());
				}

				//////////////////////////////////////////////////////////////////
				// Perform a square root of the last item on the stack
				// Command: sqrt
			} else if (cmdInput.matches("^sqrt")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Taking the square root of the last stack item");
				Math.SquareRoot(calcStack);

				//////////////////////////////////////////////////////////////////
				// Swap primary and secondary stack
				// Command: ss
			} else if (cmdInput.matches("^[Ss][Ss]")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Swapping primary and secondary stack");
				Stack<Double> calcStackTemp = (Stack<Double>) calcStack.clone();
				calcStack = (Stack<Double>) calcStack2.clone();
				calcStack2 = (Stack<Double>) calcStackTemp.clone();
				Prefs.ToggleCurrentStackNum();

				//////////////////////////////////////////////////////////////////
				// Swap two elements on the stack
				// Command: s
			} else if (cmdInput.matches("^[Ss].*")) {
				int item1 = 1;
				int item2 = 2;

				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				// Determine the source and destination elements
				try {
					if (cmdInput.substring(1).trim().length() != 0) {
						item1 = Integer.parseInt(cmdInput.substring(1).trim().split("\\s")[0]);
						item2 = Integer.parseInt(cmdInput.substring(1).trim().split("\\s")[1]);
					}
				} catch (Exception e) {
					Output.printError("Error parsing line number for stack swap: '" + cmdInput.substring(1).trim() + "'");
				}

				// Make sure the numbers are valid
				if (item1 < 1 || item1 > calcStack.size() || item2 < 1 || item2 > calcStack.size()) {
					Output.printError("Invalid element entered.  Must be between 1 and " + calcStack.size());
				} else {
					Debug.Print("Swapping #" + item1 + " and #" + item2 + " stack items");

					calcStack = StackOps.StackSwapItems(calcStack, (item1 - 1), (item2) - 1);
				}

				//////////////////////////////////////////////////////////////////
				// Flip the sign of last stack element
				// Command: f
			} else if (cmdInput.matches("^[Ff]")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Changing sign of last stack element");
				if (!calcStack.isEmpty())
					calcStack.push(calcStack.pop() * -1);

				//////////////////////////////////////////////////////////////////
				// Copy the item at the top of the stack
				// Command: copy
			} else if (cmdInput.matches("^copy")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Copying the item at the top of the stack");
				if (calcStack.size() >= 1) {
					calcStack.add(calcStack.lastElement());
				} else {
					Output.printError("ERROR: Must be an item in the stack to copy it");
				}

				//////////////////////////////////////////////////////////////////
				// Add the value of PI onto the stack
				// Command: pi
			} else if (cmdInput.matches("^pi")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Adding PI to the end of the stack");
				calcStack.add(java.lang.Math.PI);

				//////////////////////////////////////////////////////////////////
				// Operand entered
				//
			} else if (cmdInput.matches("[\\*\\+\\-\\/\\^\\%]")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("CalcStack has " + calcStack.size() + " elements");
				Debug.Print("Operand entered: '" + cmdInput.charAt(0) + "'");
				// Verify stack contains at least two elements
				if (calcStack.size() >= 2) {
					calcStack = Math.Parse(cmdInput.charAt(0), calcStack);
				} else {
					Output.printError("Two numbers are required for this operation");
				}

				//////////////////////////////////////////////////////////////////
				// Number entered, add to stack. Blank line will trigger so skip if !blank
				//
			} else if (!cmdInput.isEmpty() && cmdInput.matches("^-?\\d*\\.?\\d*")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("Adding number onto the stack");
				calcStack.push(Double.valueOf(cmdInput));

				//////////////////////////////////////////////////////////////////
				// Handle numbers with a single operand at the end (a NumOp)
				//
			} else if (cmdInput.matches("^-?\\d*(\\.)?\\d* ?[\\*\\+\\-\\/\\^]")) {
				Debug.Print("Saving current stack to undo stack");
				undoStack = (Stack<Double>) calcStack.clone();

				Debug.Print("CalcStack has " + calcStack.size() + " elements");
				// Verify stack contains at least one element
				if (calcStack.size() >= 1) {
					char TempOp = cmdInput.charAt(cmdInput.length() - 1);
					String TempNum = cmdInput.substring(0, cmdInput.length() - 1);
					Debug.Print("NumOp Found: Num= '" + TempNum + "'");
					Debug.Print("NumOp Found: Op = '" + TempOp + "'");
					calcStack.push(Double.valueOf(TempNum));
					calcStack = Math.Parse(TempOp, calcStack);
				} else {
					Output.printError("One number is required for this NumOp function");
				}

				//////////////////////////////////////////////////////////////////
				// A blank line is OK, just do nothing
				//
			} else if (cmdInput.matches("")) {
				Debug.Print("Blank line entered");

				//////////////////////////////////////////////////////////////////
				// Display an error if the entry matched none of the above
			} else {
				Output.printColorln(Ansi.Color.RED, "Input : '" + cmdInput + "'");
			}

			// Display DashLine
			Output.displayDashedNameLine();

		}

		// Save the primary and secondary stacks
		Prefs.SaveStack(calcStack, "1");
		Prefs.SaveStack(calcStack2, "2");
	}

} // End Program
