package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyList = new ArrayList<>(10);

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyList);
    }

    @Override
    public void addHistory(Task task) {
        if(historyList.size() == 10) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }
}
