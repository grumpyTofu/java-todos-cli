package todos.entities;

public class Todo {
    public String id;
    public String title;
    public String description;

    public void print() {
        System.out.println("\nTodo:");
        System.out.println(String.format("Id: %s", this.id));
        System.out.println(String.format("Title: %s", this.title));
        System.out.println(String.format("Description: %s", this.description));
    }
}
