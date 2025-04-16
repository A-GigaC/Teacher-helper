package nsu.ru;

import nsu.ru.pipeline.Downloader;
import nsu.ru.exceptions.DSLException;
import nsu.ru.models.Config;
import nsu.ru.pipeline.Pipeline;
import nsu.ru.pipeline.PlagiarismChecker;
import nsu.ru.setup.ScriptLoader;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, DSLException, GitAPIException {
        if (args.length < 3) {
            throw new DSLException("Not enough arguments. Give path to configuration and additional.");
        }

        Config config = ScriptLoader.loadScript(args[1], args[2]);
        //System.out.println(config.getStudents().get(0).getFullName());
//        println config.plagiarismCheck.get("Task_1_1_1")
//        println config.plagiarismCheck.get("Task_1_1_2")
//        System.out.println(config.getExtraScore().get("A-GigaC"));
//        System.out.println(config.getResolvedTasks().get("A-GigaC"));
//        System.out.println(config.getGroups().get(0).getStudents());
//        System.out.println(config.getGroups().get(1).getStudents());
//        PlagiarismChecker checkRunner = new PlagiarismChecker(config);
//        List<String> repos = new ArrayList<>();
        //repos.add("https://FortrainMas/OOP");
        //checkRunner.downloadRepositories(repos);
        //Downloader.downloadAdditional(config.getAdditionalRepositories());
        //Downloader.setupStudentsRepositories(config.getStudents().stream().map(it -> it.getRepoUrl()).toList());
        //checkRunner.setupJPlag();

        Pipeline pipeline = new Pipeline(config);
        pipeline.startPipeline();
    }
}

/* TODO: тесты для dsl, выкачка с GH и репами, любые url'ы */

/*
Формальные требования к работе с загрузкой репозиториев:
a. Указанные как ссылки дополнительные источники для JPlag считаются успешно скачанными,
        если не были вырошены никакие исключения.
b. Репозиторий студента должен быть скачан, если код прошёл CI/CD.
        (иначе проверка не имеет смысла,
            т.к. задание не может быть зачтено по формальным требованиям курса => проверка бессмыслена)
        Это означает, что набор файлов в репозитории полноценен (все gradle файлы, тесты и main-код соотв. на месте).
        Если в процессе скачивания не будет никаких исключений - репозиторий считатется скачаным.

Возможны сценарии, когда будут получены исключения:
1. Некорректный URL: для дополнительных источников кода сообщаем о недоступности ссылки и продолжаем исполнение,
    а в случае некорректной ссылки репозитория студента - сообщаем о неправильной ссылке и ломаем программу
2. Соединение сброшено: для репозиториев студентов - повторяем попытку скачать репозиторий, пока не скачаем.
    Для доп источников поведение аналогично.

 */
