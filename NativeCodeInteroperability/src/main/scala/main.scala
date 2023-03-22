import scalanative.unsafe._
import scala.scalanative.libc.stdlib.{malloc, free}
import scala.scalanative.libc.string.{strlen, strcpy}
import scalanative.unsigned.{ULong, UnsignedRichInt}

type struct_article = CStruct2[CString, CInt]

@extern
object myapi:
  def read_articles(
    article_titles: Ptr[CString], 
    release_year: Ptr[CInt], 
    number_of_articles: CInt): Ptr[Ptr[struct_article]] = extern
  def free_articles(articles: Ptr[Ptr[struct_article]], number_of_articles: CInt): Unit = extern
  def print_articles(artciles: Ptr[Ptr[struct_article]], number_of_articles: CInt): Unit = extern

object Main:
  def main(args: Array[String]): Unit = 
    import myapi._
    val number_of_articles: Int = 3
    
    // Allocate space on the heap for the titles and years.
    val article_titles_heap: Ptr[CString] = 
        malloc(sizeof[CString]).asInstanceOf[Ptr[CString]]
    val release_years_heap: Ptr[CInt]= 
        malloc(sizeof[CInt]*number_of_articles.toULong).asInstanceOf[Ptr[CInt]]
        
    val article_titles: Array[CString] = 
      Array[CString](
        c"Result Invalidation for Incremental Modular Analyses",
        c"A Delta-Debugging Approach to Assessing the Resilience of Actor Programs through Run-time Test Perturbations", 
        c"Towards Tracking Thread Interferences during Abstract Interpretation of Concurrent Programs")
    val release_years: Array[CInt] = Array[CInt](2022, 2020, 2015)

    // copy the titles and years to the heap.
    for(i <- 0 until number_of_articles)
    do
      val stringLength: ULong = strlen(article_titles(i)) + 1.toULong
      article_titles_heap(i) = malloc(stringLength).asInstanceOf[CString]
      strcpy(article_titles_heap(i), article_titles(i))
      release_years_heap(i) = release_years(i)

    // pass the pointers of the arrays to the C functions
    val articles: Ptr[Ptr[struct_article]] = 
        read_articles(article_titles_heap, release_years_heap, number_of_articles)
    print_articles(articles, number_of_articles)
    
    // Free all the heap allocated memory.
    free_articles(articles, number_of_articles)
    for(i <- 0 until number_of_articles)
    do
        free(article_titles_heap(i).asInstanceOf[Ptr[Byte]])
        free(article_titles_heap.asInstanceOf[Ptr[Byte]])
        free(release_years_heap.asInstanceOf[Ptr[Byte]])
