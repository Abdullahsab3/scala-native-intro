import scalanative.unsafe._

def updateAges(ages: Ptr[CInt], numberOfAges: CInt): Unit =
  for(i <- 0 to numberOfAges)
  do
    !(ages + i) = !(ages + i) + 1

def updateAge(ages: Ptr[CInt], index: CInt): Unit =
  !(ages + index) = !(ages + index) + 1

def printAges(ages: Ptr[CInt], numberOfAges: CInt): Unit =
  print("Ages are: ")
  for(i <- 0 until numberOfAges)
  do
    print(s"${!(ages + i)} ")
  println()

def generateAges(numberOfAges: CInt): Unit =
  /* Allocate memory in the zone
   * All the allocated memory is zeored out. */
   Zone { implicit z =>
    val ages : Ptr[CInt] = alloc[CInt](numberOfAges)
    printAges(ages, numberOfAges)
    updateAges(ages, numberOfAges)
    printAges(ages, numberOfAges)
    updateAge(ages, 4)
    printAges(ages, numberOfAges)
    }
    /* When the execution of the Zone is finished
     * the zone allocator will free all the manually
     * allocated memory */

object Main:
  def main(args: Array[String]): Unit =
    val numberOfAges = 10
    generateAges(numberOfAges)