public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Совершить прогулку на закате", TaskStatus.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task("Купить наушники", TaskStatus.NEW);
        task2.setDescription("Наушники должны быть с микрофоном");
        taskManager.addTask(task2);


        Epic epic1 = new Epic("Обучение", TaskStatus.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Перечитать тему про дженерики", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Разобраться в ТЗ финального проекта", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Рисунок", TaskStatus.NEW);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("Выбрать цвет", TaskStatus.NEW, epic2.getId());
        taskManager.addSubtask(subtask3);

        Subtask subtask4 = new Subtask("Рисунок", TaskStatus.NEW, epic2.getId());
        taskManager.addSubtask(subtask4);

        Subtask subtask5 = new Subtask("Купить палитру", TaskStatus.NEW, epic2.getId());
        taskManager.addSubtask(subtask5);

        Subtask subtask6 = new Subtask("Вкрутить лампочку", TaskStatus.NEW, 10);
        taskManager.addSubtask(subtask6);

        Epic epic3 = new Epic("Вкрутить лампочку", TaskStatus.NEW);
        epic3.setId(subtask6.getId());
        taskManager.addEpic(epic3);

        taskManager.getTaskById(2);

        taskManager.getEpicById(3);

        taskManager.getSubtaskById(4);

        taskManager.getSubtaskById(5);

        taskManager.getSubtaskById(4);

        taskManager.getTaskById(1);

        System.out.println("Задачи:");
        for (Task task : taskManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println();

        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpicList()) {
            System.out.println(epic);
            for (Task task : taskManager.getSubtasksOfEpic(epic.getId())) {
                System.out.println("--> " + task);
            }
        }

        System.out.println();

        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtaskList()) {
            System.out.println(subtask);
        }

        System.out.println();

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
