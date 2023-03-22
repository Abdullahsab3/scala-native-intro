import scala.scalanative.unsafe._
// Scala Native has bindings for a core subset of the C Standard Library
import scala.scalanative.libc.stdlib.{malloc, free}
import scala.scalanative.libc.string.{strlen, strcpy}
import scalanative.unsigned.UnsignedRichInt
import java.nio.charset.Charset

type struct_article = CStruct2[CInt, CString]

object Struct_factory:
  def make_article_pointer(year: CInt, title: CString): Ptr[struct_article] =
    /* Dynamically allocate heap memory for the struct and title.
      * The pointer returned by malloc needs to be cast to the specific pointer needed,
      * in this case a pointer to a struct_article. */
    val new_article: Ptr[struct_article] = 
      malloc(sizeof[struct_article]).asInstanceOf[Ptr[struct_article]]
      
    // Allocate heap memory for the string, and copy its contents to the allocated memory.
    val stringLength = strlen(title) + 1.toULong
    new_article._2 = malloc(stringLength).asInstanceOf[CString]
    strcpy(new_article._2, title)

    new_article._1 = year
    new_article

class Article(year: CInt, title: CString):
  val struct_pointer = 
    Struct_factory.make_article_pointer(year, title)

  /* A method to free the allocated memory.
    * Pointers need to be cast to a byte pointer.  */
  def free_pointer() =
    free(struct_pointer._2.asInstanceOf[Ptr[Byte]])
    free(struct_pointer.asInstanceOf[Ptr[Byte]])

  def article_year: CInt = struct_pointer._1  
  def article_title: CString = struct_pointer._2

  def update_article(year: CInt, title: CString): Unit =
    struct_pointer._1 = year
    free(struct_pointer._2.asInstanceOf[Ptr[Byte]])
    val stringLength = strlen(title) + 1.toULong
    strcpy(struct_pointer._2, title)

  override def toString(): String = 
  // fromCString will convert the string saved inside the given pointer to a Scala String.
    s"Article \"${fromCString(article_title, Charset.forName("utf-8"))}\" is written in $article_year"

object Main:
  def main(args: Array[String]): Unit =
    val article: Article = Article(2022, c"LIFUSO: A Tool for Library Feature Unveiling based on Stack Overflow Posts")
    println(article.toString())
    article.update_article(2023, c"Result Invalidation for Incremental Modular Analyses")
    println(article.toString())
    article.free_pointer()