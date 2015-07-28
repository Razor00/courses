number = 12
def func1():
    global number 
    number = number + 1


def func2():
    print number


func1()
func2()
func1()
func2()
