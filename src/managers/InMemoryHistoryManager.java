package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyList = new ArrayList<>(10);

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }

    @Override
    public void addHistory(Task task) {
        if (!historyList.isEmpty()) {
            for (Task task1 : historyList) {
                if (task1.equals(task)) {
                    return;
                }
            }
        }

        if (historyList.size() == 10) {
            historyList.removeFirst();

            if (historyList.size() == 10) {
                historyList.removeFirst();
            }
            historyList.add(task);

        }
}
