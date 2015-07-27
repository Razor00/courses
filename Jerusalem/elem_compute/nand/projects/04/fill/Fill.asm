// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, the
// program clears the screen, i.e. writes "white" in every pixel.

// Put your code here.

//keyboard loop
(KEYBOARD)
@24576
D=M
@RESET
D;JEQ
@R2
M=-1
@PAINT
0;JMP
(RESET)
@R2
M=0
(PAINT)
@8192
D=A
@R0
M=D
@SCREEN
D=A
@R1
M=D
(LOOP)
@R0
D=M
D=D-1
M=D
@KEYBOARD
D;JLT
@R2
D=M
@R1
A=M
M=D
D=A
D=D+1
@R1
M=D
@LOOP
0;JMP

@END
0;JMP
