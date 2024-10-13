import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        FileBackedTaskManager fileManager = new FileBackedTaskManager("StateFile.csv");
        InMemoryTaskManager taskManager = new InMemoryTaskManager();


        Task task = new Task("Изучить сериализацию", TaskStatus.NEW);
        LocalDateTime time = LocalDateTime.of(2025, 1, 12, 15, 25);
        Duration duration = Duration.ofMinutes(459);
        task.setStartTime(time, duration);
        fileManager.addTask(task);



        Epic epic = new Epic("Сдать ТЗ 7 спринта", TaskStatus.NEW);
        fileManager.addEpic(epic);


        Task task2 = new Task("Купить наушники", TaskStatus.NEW);
        Duration duration1 = Duration.ofMinutes(12586);
        LocalDateTime time1 = LocalDateTime.of(2025, 2, 2, 14, 53);
        task2.setStartTime(time1, duration1);
        fileManager.addTask(task2);



        Subtask subtask = new Subtask("Выбрать цвет", TaskStatus.NEW, epic.getId());
        LocalDateTime time2 = LocalDateTime.of(2024, 11, 2, 6, 0);
        Duration duration2 = Duration.ofMinutes(76);
        subtask.setStartTime(time2, duration2);
        fileManager.addSubtask(subtask);


        Subtask subtask1 = new Subtask("Выбрать еще один цвет", TaskStatus.NEW, epic.getId());
        LocalDateTime time3 = LocalDateTime.of(2025, 1, 2, 8, 12);
        Duration duration3 = Duration.ofMinutes(186);
        subtask1.setStartTime(time3, duration3);
        fileManager.addSubtask(subtask1);


        Epic epic1 = new Epic("Обучение", TaskStatus.NEW);
        fileManager.addEpic(epic1);

        Subtask subtask2 = new Subtask("Выбрать еще один цвет", TaskStatus.NEW, epic.getId());
        LocalDateTime time4 = LocalDateTime.of(2025, 1, 2, 8, 12);
        Duration duration4 = Duration.ofMinutes(186);
        subtask1.setStartTime(time3, duration3);
        fileManager.addSubtask(subtask1);

        System.out.println("Содержание файла исходного менеджера:");

        fileManager.printFile();

        FileBackedTaskManager fileManager1 = FileBackedTaskManager.loadFromFile(fileManager.getFile());
        System.out.println("-------------------------------");

        System.out.println("Содержание файла другого экземпляра менеджера:");
        fileManager1.printFile();

        System.out.println("-------------------------------");
        System.out.println("Списки по папкам:");

        System.out.println(fileManager.getEpicList());
        System.out.println(fileManager.getTaskList());
        System.out.println(fileManager.getSubtaskList());
        System.out.println("-------------------------------");
        System.out.println("Список приоритетных задач:");
        System.out.println(fileManager.getPrioritizedTasks());


    }
}
