#include <string.h>
#include <stdlib.h>
#include <stdio.h>

typedef struct article {
    char *title;
    int year;
} Article;

Article *make_article(char *title, int year) {
    Article *new = (Article *)malloc(sizeof(Article));
    new->year = year;
    new->title = (char *)malloc(sizeof(char) * (strlen(title) + 1));
    strcpy(new->title, title);
    return new;
};

void free_article(Article *t) {
    free(t->title);
    free(t);
}

void free_articles(Article **articles, int number_of_articles) {
    for(int i = 0; i < number_of_articles; i++) {
        free_article(articles[i]);
    }
}

Article **read_articles(char *article_titles[], int release_years[], int number_of_articles) {
    Article **output = malloc(sizeof(Article *) * number_of_articles);
    for(int i = 0; i < number_of_articles; i++) {
        output[i] = make_article(article_titles[i], release_years[i]);
    }

    return output;
}

void print_articles(Article **articles, int number_of_articles) {
    for(int i = 0; i < number_of_articles; i++) {
        Article *article = articles[i];
        printf("Article \"%s\" is published on %d\n", article->title, article->year);
    }
}