package ca.cmpt213.webserver.controllers;

import ca.cmpt213.webserver.models.Tokimon;
import ca.cmpt213.webserver.models.TokimonList;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TokimonListController {
    private AtomicInteger nextId;
    private TokimonList tokimonList;

    // Returns a list of all Tokimon cards
    @GetMapping("/tokimon")
    public ResponseEntity<List<Tokimon>> getTokimons() {
        System.out.println("GET /tokimon");
        List<Tokimon> tokimons = tokimonList.getTokimons();
        return ResponseEntity.ok(tokimons);
//        return tokimonList.getTokimons();
    }

    // Returns the Tokimon card with the given ID
    @GetMapping("tokimon/{id}")
    public ResponseEntity<Tokimon> getTokimonByID(@PathVariable long id) {
        System.out.println("GET /tokimon/{id}");
        Tokimon tokimon = tokimonList.getTokimonById(id);
        if (tokimon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(tokimon);
        }
//        return tokimon;
    }

    // Adds a bew Tokimon card abd returns HTTP 201 (Created)
    @PostMapping("/tokimon")
    public ResponseEntity<Tokimon> addTokimon(@RequestBody Tokimon newTokimon, HttpServletResponse response) {
        System.out.println("POST /tokimon");
        newTokimon.setId(nextId.getAndIncrement());
        tokimonList.addTokimon(newTokimon);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTokimon);
    }

    // Deletes the Tokimon with the given ID and returns HTTP 204 (No Content)
    @DeleteMapping("/tokimon/{id}")
    public ResponseEntity<Void> deleteTokimon(@PathVariable long id, HttpServletResponse response) {
        System.out.println("DELETE /tokimon/" + id);
        Tokimon deletedTokimon = tokimonList.deleteTokimon(id);
        if (deletedTokimon != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/tokimon/edit/{id}")
    public ResponseEntity<Tokimon> updateTokimon(@PathVariable long id, @RequestBody Tokimon updatedTokimon) {
        System.out.println("PUT /tokimon/edit/{id}");
        if (id != updatedTokimon.getId()) {
            return ResponseEntity.badRequest().build();
        }
        Tokimon tokimon = tokimonList.updateTokimon(updatedTokimon);
        if (tokimon != null) {
            return ResponseEntity.ok(tokimon);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("POST CONSTRUCT CODE");
        tokimonList = new TokimonList();
        List<Tokimon> tokimons = tokimonList.getTokimons();
        Tokimon firstTokimon;
        if (tokimons.size() > 0) {
             firstTokimon = tokimons.get(0);
        } else {
            firstTokimon = null;
        }

        if (firstTokimon == null) {
            nextId = new AtomicInteger(1);
        } else {
            // calculate what the next id would be
            long maxId = firstTokimon.getId();
            for (int i=1; i<tokimons.size(); i++) {
                long id = tokimons.get(i).getId();
                if (id > maxId) {
                    maxId = id;
                }
            }
            nextId = new AtomicInteger((int) (maxId + 1));
        }

//        nextId = new AtomicInteger(1);
//        Tokimon t1 = new Tokimon("bulbasaur", "grass", 5);
//        t1.setId(nextId.getAndIncrement());
//        tokimonList.addTokimon(t1);
//        Tokimon t2 = new Tokimon("squirtle", "water", 3);
//        t2.setId(nextId.getAndIncrement());
//        tokimonList.addTokimon(t2);
//        Tokimon t3 = new Tokimon("squirtle", "water", 7);
//        t3.setId(nextId.getAndIncrement());
//        tokimonList.addTokimon(t3);
    }
}
