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

symbols = [
("R0", 0),
("R1", 1),
("R2", 2),
("R3", 3),
("R4", 4),
("R5", 5),
("R6", 6),
("R7", 7),
("R8", 8),
("R9", 9),
("R10", 10),
("R11", 11),
("R12", 12),
("R13", 13),
("R14", 14),
("R15", 15),
("SP", 0),
("LCL", 1),
("ARG", 2),
("THIS", 3),
("THAT", 4),
("SCREEN", 16384), 
("KBD", 24576),
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
def process_stage2(instr, mcode):
    for l,c in enumerate(instr):
        if c[0] == "@":
            process_A_instruction(c, l, mcode)
        else:
            process_C_instruction(c, l, mcode)
        print mcode[l]

def search_symbol(sym):
    for e in symbols:
        if e[0] == sym:
            return e[1]
    return -1

def add_symbol(sym, v):
    for e in symbols:
        if e[0] == sym:
            e[1] = v
            return
    symbols.append([sym, v])

def process_stage1(instr, finstr):
    l = 0 
    for i, e in enumerate(instr):
        minstr=instr[i]
        if e[0] == "@" and not(e[1].isdigit()):
            r = search_symbol(e[1:])
            if r == -1:
                add_symbol(e[1:], -1)
            else:
                minstr = "@" + str(r)

        elif e[0] == '(': #labels can be stacked at same place, handle that
            sname = e[1:].split(')')
            add_symbol(sname[0], l)
            minstr = ""

        if minstr != "":
            finstr.append(minstr)

        if e[0] != '(':
            l += 1    #increment line numbers when not processing labels

def assign_symbol_values():
    v = 16
    for s in symbols:
        if s[1] == -1:
            s[1] = v
            v += 1
pattern = re.compile(r'\s+')
instr = list()
for line in fileinput.input():
    if line[0:2] == "//" or line[0:2] == "--":
        continue
    mline = re.sub(pattern, '', line)
    if mline != "": 
        mline = mline.split("//")
#        print mline[0]
        instr.append(mline[0])
#    else:
#        print "------"

mcode = [0] * len(instr)
f1instr = list()
f2instr = list()
process_stage1(instr, f1instr)
assign_symbol_values()
process_stage1(f1instr, f2instr)
process_stage2(f2instr, mcode)
