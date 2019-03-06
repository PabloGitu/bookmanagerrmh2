package com.tfg.bookmanagerrmh2.web.rest;
import com.tfg.bookmanagerrmh2.domain.Book;
import com.tfg.bookmanagerrmh2.service.BookService;
import com.tfg.bookmanagerrmh2.web.rest.errors.BadRequestAlertException;
import com.tfg.bookmanagerrmh2.web.rest.util.HeaderUtil;
import com.tfg.bookmanagerrmh2.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Book.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * POST  /books : Create a new book.
     *
     * @param book the book to create
     * @return the ResponseEntity with status 201 (Created) and with body the new book, or with status 400 (Bad Request) if the book has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);
        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Book result = bookService.save(book);
        return ResponseEntity.created(new URI("/api/books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /books : Updates an existing book.
     *
     * @param book the book to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated book,
     * or with status 400 (Bad Request) if the book is not valid,
     * or with status 500 (Internal Server Error) if the book couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to update Book : {}", book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Book result = bookService.save(book);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, book.getId().toString()))
            .body(result);
    }

    /**
     * GET  /books : get all the books.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of books in body
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(Pageable pageable) {
        log.debug("REST request to get a page of Books");
        Page<Book> page = bookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /books/author/:id : get the books of an author.
     *
     * @param id the id of the author
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @GetMapping("/books/author/{id}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get Books of an author : {}", id);
        Page<Book> page = bookService.getBooksByAuthor(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books/author/" + id);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /books/publisher/:id : get the books of a publisher.
     *
     * @param id the id of the publisher
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @GetMapping("/books/publisher/{id}")
    public ResponseEntity<List<Book>> getBooksByPublisher(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get Books of an author : {}", id);
        Page<Book> page = bookService.getBooksByPublisher(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/books/publisher/" + id);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /books/:id : get the "id" book.
     *
     * @param id the id of the book to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the book, or with status 404 (Not Found)
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<Book> book = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(book);
    }

    /**
     * DELETE  /books/:id : delete the "id" book.
     *
     * @param id the id of the book to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
