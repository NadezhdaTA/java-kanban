import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> history = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();

        Node<Task> printNode = first;

        while (printNode != null) {
            historyList.add(printNode.getTask());
            printNode = printNode.getNext();
        }
        return historyList;
    }

    @Override
    public void addHistory(Task task) {
        if (history.isEmpty()) {
            first = new Node<>(task, null, null);
            history.put(task.getId(), first);
            last = first;
        } else {
            if (history.containsKey(task.getId())) {
                remove(task.getId());
            }
            this.linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        Node<Task> node = history.get(id);
        Node<Task> prevNode = node.getPrev();
        Node<Task> nextNode = node.getNext();

        if (prevNode == null) {
            first = nextNode;
        } else {
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
            history.remove(id);
        }
    }

    public void linkLast(Task task) {
        final Node<Task> node = new Node<>(task, last, null);
        last.setNext(node);
        history.put(task.getId(), node);
        last = node;
    }
}

