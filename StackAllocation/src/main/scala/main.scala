import scalanative.unsafe._

// Loop over the allocated memory and increment with 1
def updateAges(ages: Ptr[CInt], numberOfAges: CInt): Unit =
  for(i <- 0 to numberOfAges)
  do
    !(ages + i) = !(ages + i) + 1

/* Increment one element in the allocated memory
 * given its index in the allocated array. */
def updateAge(ages: Ptr[CInt], index: CInt): Unit =
  !(ages + index) = !(ages + index) + 1

def printAges(ages: Ptr[CInt], numberOfAges: CInt): Unit =
  print("Ages are: ")
  for(i <- 0 until numberOfAges)
  do
    print(s"${!(ages + i)} ")
  println()

def generateAges(numberOfAges: CInt): Unit =
  /* Allocate memory on the stack. 
   * All the allocated memory is zeored out. */
  val ages : Ptr[CInt] = stackalloc[CInt](numberOfAges)
  printAges(ages, numberOfAges)
  updateAges(ages, numberOfAges)
  printAges(ages, numberOfAges)
  // increment the 4th element of the array
  updateAge(ages, 4)
  printAges(ages, numberOfAges)
  /* Once the method is finished executing, 
   * all the allocated stack memory will be freed. */

object Main:
  def main(args: Array[String]): Unit =
    val numberOfAges = 10
    generateAges(numberOfAges)
