/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package todos;

import java.util.Scanner;
import java.util.List;

import todos.entities.Todo;
import todos.repositories.TodoRepository;

enum Action {
    LIST,
    ADD,
    EDIT,
    DELETE,
    QUIT
}

public class App {

    public static void main(String[] args) {
        TodoRepository todoRepository = new TodoRepository();
        Scanner scanner = new Scanner(System.in);
        Boolean running = true;

        while (running) {
            System.out.print("""
                    Available actions:
                        - list
                        - add
                        - edit
                        - delete
                        - quit
                    Enter an action:\s""");

            String actionInput = scanner.nextLine();
            Action action = null;

            try {
                action = Action.valueOf(actionInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Handle the case where the input doesn't match any enum constant
                System.out.println("Invalid action entered.");
            }

            try {
                switch (action) {
                    case Action.LIST: {
                        List<Todo> todos = todoRepository.findAll();
                        System.out.println("Listing todos");
                        for (Todo todo : todos) {
                            todo.print();
                        }
                        break;
                    }
                    case Action.ADD: {
                        System.out.println("Enter a title: ");
                        String title = scanner.nextLine();
                        System.out.println("Enter a description: ");
                        String description = scanner.nextLine();

                        Todo todo = new Todo();
                        todo.title = title;
                        todo.description = description;

                        todoRepository.create(todo);
                        break;
                    }
                    case Action.EDIT: {
                        List<Todo> todos = todoRepository.findAll();
                        System.out.println("Listing todos");
                        for (Todo todo : todos) {
                            todo.print();
                        }

                        System.out.println("Enter the id of the todo to update: ");
                        String id = scanner.nextLine();

                        Boolean updated = false;
                        for (Todo todo : todos) {
                            if (todo.id.equals(id)) {
                                System.out.println("Would you like to update the title? (y/N): ");
                                String editTitleLine = scanner.nextLine();
                                String editTitle = editTitleLine.isEmpty() ? "n" : editTitleLine;
                                if (editTitle.equals("y")) {
                                    System.out.println("Enter the new title: ");
                                    String title = scanner.nextLine();
                                    todo.title = title;
                                }

                                System.out.println("Would you like to update the description? (y/N): ");
                                String editDescriptionLine = scanner.nextLine();
                                String editDescription = editDescriptionLine.isEmpty() ? "n" : editDescriptionLine;
                                if (editDescription.equals("y")) {
                                    System.out.println("Enter the new description: ");
                                    String description = scanner.nextLine();
                                    todo.description = description;
                                }

                                updated = editTitle.equals("y") || editDescription.equals("y");
                            }
                        }

                        if (!updated) {
                            throw new IllegalArgumentException("Todo not found");
                        }

                        todoRepository.save(todos);
                        break;
                    }
                    case Action.DELETE: {
                        List<Todo> todos = todoRepository.findAll();
                        System.out.println("Listing todos");
                        for (Todo todo : todos) {
                            todo.print();
                        }

                        System.out.println("Enter the id of the todo to delete: ");
                        String id = scanner.nextLine();

                        todoRepository.remove(id);
                        break;
                    }
                    case Action.QUIT:
                    default:
                        System.out.println("Quitting the application...");
                        running = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred");
                e.printStackTrace();
                running = false;
            }
        }

        scanner.close();
    }
}
