package nsu.ru.models

@FunctionalInterface
interface TaskEvaluation {
    Double evaluate(RatedTask ratedTask);
}