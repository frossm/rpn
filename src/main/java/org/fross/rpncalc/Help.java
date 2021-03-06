/******************************************************************************
 * RPNCalc
 * 
 * RPNCalc is is an easy to use console based RPN calculator
 * 
 *  Copyright (c) 2013-2021 Michael Fross
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *           
 ******************************************************************************/
package org.fross.rpncalc;

import org.fross.library.Format;
import org.fross.library.Output;
import org.fusesource.jansi.Ansi;

/**
 * Help(): Display the help page when users enters 'h' or '?' command.
 * 
 * @author michael.d.fross
 *
 */
public class Help {
	/**
	 * Display(): Show help information
	 */
	public static void Display() {
		int helpWidth = 80;

		Output.printColor(Ansi.Color.CYAN, "\n+" + "-".repeat(helpWidth) + "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, ("RPN Calculator  v" + Main.VERSION)));
		Output.printColor(Ansi.Color.CYAN, "+\n+");
		Output.printColor(Ansi.Color.WHITE, Format.CenterText(helpWidth, Main.COPYRIGHT));
		Output.printColorln(Ansi.Color.CYAN, "+\n+" + "-".repeat(helpWidth) + "+");
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "RPNCalc is a command line Reverse Polish Notation calculator"));
		Output.printColorln(Ansi.Color.CYAN, Format.CenterText(helpWidth, "https://github.com/frossm/rpncalc"));

		Output.printColorln(Ansi.Color.YELLOW, "\nCommand Line Options:");
		Output.printColorln(Ansi.Color.WHITE, " -l       Load a saved named stack. Create the stack if it does not exist");
		Output.printColorln(Ansi.Color.WHITE, " -D       Start in debug mode.  Same as using the 'debug' command");
		Output.printColorln(Ansi.Color.WHITE, " -a [lrd] Alignment of numbers. (l)eft, (r)ight, or (d)ecmimal. Default: left");
		Output.printColorln(Ansi.Color.WHITE, " -m num   Set the number of memory slots.  Default value is 10");
		Output.printColorln(Ansi.Color.WHITE, " -w num   Set Width of header / status line.  Default is 70 characters");
		Output.printColorln(Ansi.Color.WHITE, " -v       Display version information as well as latest GitHub release");
		Output.printColorln(Ansi.Color.WHITE, " -z       Disable colorized output");
		Output.printColorln(Ansi.Color.WHITE, " -h | ?   Show this help information.  Either key will work.");

		Output.printColorln(Ansi.Color.YELLOW, "\nOperands:");
		Output.printColorln(Ansi.Color.WHITE, " +    Addition:  Add last two stack elements");
		Output.printColorln(Ansi.Color.WHITE, " -    Subtraction: Subtract row 1 from row 2");
		Output.printColorln(Ansi.Color.WHITE, " *    Multiplication: Muliply last two stack items");
		Output.printColorln(Ansi.Color.WHITE, " /    Division: Divide line2 by line1");
		Output.printColorln(Ansi.Color.WHITE, " ^    Power:  Calculate line2 to the power of line1");

		Output.printColorln(Ansi.Color.YELLOW, "\nCalculator Commands:");
		Output.printColorln(Ansi.Color.WHITE, " u            Undo last action");
		Output.printColorln(Ansi.Color.WHITE, " f            Flip the sign of the element at line1");
		Output.printColorln(Ansi.Color.WHITE, " c            Clear the screen and empty current stack");
		Output.printColorln(Ansi.Color.WHITE, " clean        Clear screen but keep the stack values");
		Output.printColorln(Ansi.Color.WHITE, " d [#]        Delete the line1 value or the line number provided");
		Output.printColorln(Ansi.Color.WHITE, " s [#] [#]    Swap the last two elments in the stack or the lines provided");
		Output.printColorln(Ansi.Color.WHITE, " %            Convert line1 into a percentage by multipling it by 0.01");
		Output.printColorln(Ansi.Color.WHITE, " sqrt         Perform a square root on line1");
		Output.printColorln(Ansi.Color.WHITE, " round [n]    Round to n decimal places.  Default is 0 decimals");
		Output.printColorln(Ansi.Color.WHITE, " aa [keep]    Add all stack items. Adding 'keep' will keep existing elements");
		Output.printColorln(Ansi.Color.WHITE, " mod          Modulus. Perform a division and return the remainder");
		Output.printColorln(Ansi.Color.WHITE, " avg [keep]   Replace stack with average of values.  'keep' will retain stack");
		Output.printColorln(Ansi.Color.WHITE, " sd [keep]    Standard deviation of stack items.  'keep' will retain stack");
		Output.printColorln(Ansi.Color.WHITE, " copy         Copy line1 and add it to the stack");
		Output.printColorln(Ansi.Color.WHITE, " log | log10  Calculate the natural (base e) or base10 logarithm");
		Output.printColorln(Ansi.Color.WHITE, " int          Convert line1 to an integer by discarding after the decimal");
		Output.printColorln(Ansi.Color.WHITE, " abs          Take the absolute value of line1");
		Output.printColorln(Ansi.Color.WHITE, " rand [L] [H] Random integer between X and Y inclusive.  Default is 1-100");
		Output.printColorln(Ansi.Color.WHITE, " dice XdY     Roll a Y sided die X times.  Default is 1d6");

		Output.printColorln(Ansi.Color.YELLOW, "\nConversions:");
		Output.printColorln(Ansi.Color.WHITE, " frac [base]  Display as a fraction with min provided base. Default base is 64th");
		Output.printColorln(Ansi.Color.WHITE, " in2mm        Convert line1 from inches into millimeters");
		Output.printColorln(Ansi.Color.WHITE, " mm2in        Convert line1 from millimeters to inches");
		Output.printColorln(Ansi.Color.WHITE, " rad2deg      Convert line1 from raidans to degrees");
		Output.printColorln(Ansi.Color.WHITE, " deg2rad      Convert line1 from degrees to radians");

		Output.printColorln(Ansi.Color.YELLOW, "\nTrigonometry Functions:");
		Output.printColorln(Ansi.Color.WHITE, " sin|cos|tan [rad]    Trig Functions: Angle in degrees unless rad is provided");
		Output.printColorln(Ansi.Color.WHITE, " asin|acos|atan [rad] Trig Functions: Result in degrees unless rad is provided");
		Output.printColorln(Ansi.Color.WHITE, " hypot                Returns the hypotenuse using line1 and line2 as the legs");

		Output.printColorln(Ansi.Color.YELLOW, "\nMemory Commands:");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] add   Add line1 to memory slot X. Default slot is 0");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] copy  Copy number from memory slot X. Default slot is 0");
		Output.printColorln(Ansi.Color.WHITE, " mem [X] clr   Clear memory from slot X. Default slot0");
		Output.printColorln(Ansi.Color.WHITE, " mem clearall  Clear all memory slots");
		Output.printColorln(Ansi.Color.WHITE, " mem copyall   Copy all memory items onto the stack");

		Output.printColorln(Ansi.Color.YELLOW, "\nConstants:");
		Output.printColorln(Ansi.Color.WHITE, " pi            Add PI to the stack");
		Output.printColorln(Ansi.Color.WHITE, " phi           Add the Golden Ratio (phi) to the stack");
		Output.printColorln(Ansi.Color.WHITE, " euler         Add Euler's number (e) to the stack");

		Output.printColorln(Ansi.Color.YELLOW, "\nOperational Commands:");
		Output.printColorln(Ansi.Color.WHITE, " list stacks  Show the list of saved stacks");
		Output.printColorln(Ansi.Color.WHITE, " list mem     Display contents of the memory slots");
		Output.printColorln(Ansi.Color.WHITE, " list undo    Show the current undo stack");
		Output.printColorln(Ansi.Color.WHITE, " ss           Swap primary and secondary stack");
		Output.printColorln(Ansi.Color.WHITE, " load         Load (or create if needed) a named stack");
		Output.printColorln(Ansi.Color.WHITE, " a [lrd]      Set display alignment to be (l)eft, (r)ight, or (d)ecmial");
		Output.printColorln(Ansi.Color.WHITE, " debug        Toggle DEBUG mode on/off");
		Output.printColorln(Ansi.Color.WHITE, " ver          Display the current version");
		Output.printColorln(Ansi.Color.WHITE, " h|?          Show this help information.  Either key will work.");
		Output.printColorln(Ansi.Color.WHITE, " cx|x|exit    Exit Calculator.  'cx' will clear before exiting");

		Output.printColorln(Ansi.Color.YELLOW, "\nNotes:");
		Output.printColorln(Ansi.Color.WHITE, "  - You can place an operand at the end of a number & execute in one step.");
		Output.printColorln(Ansi.Color.WHITE, "    Example adding two numbers:   2 <enter> 3+ <enter>   will produce 5.");
		Output.printColorln(Ansi.Color.CYAN, "  - See GitHub homepage (listed above) for more detailed usage instructions\n");
	}
}
