package practicum.test;

import org.junit.jupiter.api.BeforeEach;
import practicum.manager.InMemoryTaskManager;
import practicum.manager.util.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>  {

    @BeforeEach
    void createTaskManager() {
        setTaskManager(new InMemoryTaskManager(Managers.getDefaultHistory()));
    }
}