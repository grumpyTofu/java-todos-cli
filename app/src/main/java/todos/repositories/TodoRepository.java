package todos.repositories;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import todos.entities.Todo;

public class TodoRepository {
    private File storage;
    private ObjectMapper objectMapper;

    public TodoRepository() {
        String currentDirectory = System.getProperty("user.dir");
        Path parentDirectory = Paths.get(currentDirectory);
        Path dataDirectory = parentDirectory.resolve("data");
        String storagePath = dataDirectory.resolve("todos.json").toString();

        try {
            Files.createDirectory(dataDirectory);
        } catch (Exception e) {
            System.out.println("Error creating directory");
        }

        this.storage = new File(storagePath);
        this.objectMapper = new ObjectMapper();
    }

    public List<Todo> findAll() {
        List<Todo> todos = new ArrayList<Todo>();
        try {
            ObjectReader reader = this.objectMapper.readerFor(Todo.class);
            MappingIterator<Todo> existingTodos = reader.readValues(this.storage);
            todos.addAll(existingTodos.readAll());
        } catch (IOException e) {
            System.out.println("Error reading todos");
            e.printStackTrace();
        }
        return todos;
    }

    public void create(Todo todo) {
        List<Todo> todos = this.findAll();

        todo.id = UUID.randomUUID().toString();
        todos.add(todo);

        this.persist(todos);
    }

    public void save(List<Todo> todos) {
        this.persist(todos);
    }

    public void remove(String id) {
        List<Todo> todos = this.findAll();

        Boolean removed = todos.removeIf(todo -> todo.id.equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Todo not found");
        }

        this.persist(todos);
    }

    private void persist(List<Todo> todos) {
        try (FileWriter fileWriter = new FileWriter(this.storage)) {
            ObjectWriter writer = this.objectMapper.writer();
            String output = writer.writeValueAsString(todos);
            fileWriter.write(output);
        } catch (IOException e) {
            System.out.println("Error persisting todos");
            e.printStackTrace();
        }
    }
}
