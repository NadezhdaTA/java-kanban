public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager();

        Task task = new Task("Изучить сериализацию", TaskStatus.NEW);
        fileManager.addTask(task);

        Epic epic = new Epic("Сдать ТЗ 7 спринта", TaskStatus.NEW);
        fileManager.addEpic(epic);

        Task task2 = new Task("Купить наушники", TaskStatus.NEW);
        fileManager.addTask(task2);

        Subtask subtask = new Subtask("Выбрать цвет", TaskStatus.NEW, epic.getId());
        fileManager.addSubtask(subtask);

        Epic epic1 = new Epic("Обучение", TaskStatus.NEW);
        fileManager.addEpic(epic1);

        fileManager.printFile();

        System.out.println("___");

        FileBackedTaskManager fileManager1 = FileBackedTaskManager.loadFromFile(FileBackedTaskManager.file);

        fileManager1.printFile();

    }
}
