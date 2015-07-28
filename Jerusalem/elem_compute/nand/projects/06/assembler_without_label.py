#!/usr/bin/python


import fileinput
import re
jmptable = [    
("", "000"),
("JGT" , "001"), 
("JEQ" , "010"), 
("JGE" , "011"), 
("JLT" , "100"), 
("JNE" , "101"), 
("JLE" , "110"), 
("JMP" , "111") 
]

dsttable = [
("", "000"),
("M"   , "001"), 
("D"   , "010"), 
("MD"  , "011"), 
("A"   , "100"), 
("AM"  , "101"), 
("AD"  , "110"), 
("AMD" , "111") 
]
comptable = [ 
("0"  , ""   , "101010"),
("1"  , ""   , "111111"),
("-1" , ""   , "111010"),
("D"  , ""   , "001100"),
("A"  , "M"  , "110000"),
("!D" , ""   , "001101"),
("!A" , "!M" , "110001"),
("-D" , ""   , "001111"),
("-A" , "-M" , "110011"),
("D+1", ""   , "011111"),
("A+1", "M+1", "110111"),
("D-1", ""   , "001110"),
("A-1", "M-1", "110010"),
("D+A", "D+M", "000010"),
("D-A", "D-M", "010011"),
("A-D", "M-D", "000111"),
("D&A", "D&M", "000000"),
("D|A", "D|M", "010101")
]


def process_A_instruction(c, l, mcode):
    value = c[1:]
    mcode[l] = "{0:016b}".format(int(value))

def process_C_instruction(c, l, mcode):
    mcode[l] = "111"
    p = c.split(';')
    q = p[0].split('=')
   

    jump = ""
    if len(p) == 2:
        jump = p[1]
    #dst = comp; jmp
    #comp
    dst=""
    if len(q) == 2:
        dst  = q[0]
        comp = q[1]
    else:
        comp = q[0]

    for e in comptable:
        if e[0] == comp:
            mcode[l] += "0" + e[2]
            break
        if e[1] == comp:
            mcode[l] += "1" + e[2]
            break

    # dst valid
    if dst != "":
        for e in dsttable:
            if e[0] == dst:
                mcode[l] += e[1]
                break
    else:
        mcode[l] += "000" #no dst


    if jump != "":         #jmp valid
        for e in jmptable:
            if e[0] == jump:
                mcode[l] += e[1]
    else:
        mcode[l] += "000"   #no jmp

#indentify the instruction and process the instruction
def process_stage1(instr, mcode):
    for c,l in instr:
        if c[0] == "@":
            process_A_instruction(c, l, mcode)
        else:
            process_C_instruction(c, l, mcode)
        print mcode[l]





pattern = re.compile(r'\s+')
instr = list()
lno = 0
for line in fileinput.input():
    if line[0:2] == "//" or line[0:2] == "--":
        continue
    mline = re.sub(pattern, '', line)
    if mline != "": 
        print mline
        instr.append([mline, lno])
        lno += 1
    else:
        print "------"

mcode = [0] * lno
process_stage1(instr, mcode)
