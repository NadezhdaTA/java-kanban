import java.util.Objects;

public class Node<Task> {
    private Task task;
    private Node<Task> prev;
    private Node<Task> next;

    public Node(Task task, Node<Task> prev, Node<Task> next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) { this.task = task; }

    public Node<Task> getPrev() { return prev; }

    public void setPrev(Node<Task> prev) { this.prev = prev; }

    public Node<Task> getNext() { return next; }

    public void setNext(Node<Task> next) { this.next = next; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<Task> node = (Node<Task>) o;
        return node.task == this.task;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public String toString() {
        return "Node{" +
                " task=" + task +
                ", prev='" + prev + '\'' +
                ", next='" + next + '\'' +
                "}";
    }
}
