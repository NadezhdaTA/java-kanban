public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager("StateFile.csv");

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

        System.out.println("Содержание файла исходного менеджера:");

        fileManager.printFile();

        System.out.println("___");

        FileBackedTaskManager fileManager1 = FileBackedTaskManager.loadFromFile(fileManager.getFile());

        System.out.println("Содержание файла другого экземпляра менеджера:");

        fileManager1.printFile();

        System.out.println("___");

        System.out.println("Список подзадач эпика:");

        System.out.println(fileManager1.getSubtasksOfEpic(epic.getId()));

    }
}
