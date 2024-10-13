import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyList = new ArrayList<>(10);

    @Override
    public ArrayList<Task> getHistory(){
        return new ArrayList<>(historyList);
    }

    @Override
    public void addHistory(Task task){
        if(historyList.size() == 10) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }
}
