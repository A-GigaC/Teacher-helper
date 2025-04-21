package nsu.ru.models

@FunctionalInterface
interface TaskEvaluation {
    void evaluate(RatedTask ratedTask);
}