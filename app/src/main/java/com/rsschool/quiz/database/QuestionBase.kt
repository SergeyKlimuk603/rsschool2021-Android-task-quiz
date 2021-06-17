package com.rsschool.quiz.database

import com.rsschool.quiz.interfaces.QuestionDataBase
import com.rsschool.quiz.models.Question

class QuestionBase : QuestionDataBase {
    override fun getQuestions(): Array<Question> {
        return arrayOf(
            Question(
                "Вопрос 1",
                arrayOf(
                    "Ответ 1",
                    "Ответ 2",
                    "Ответ 3",
                    "Ответ 4",
                    "Ответ 5"
                ),
                0
            ),
            Question(
                "Вопрос 2",
                arrayOf(
                    "Ответ 1",
                    "Ответ 2",
                    "Ответ 3",
                    "Ответ 4",
                    "Ответ 5"
                ),
                1
            ),
            Question(
                "Вопрос 3",
                arrayOf(
                    "Ответ 1",
                    "Ответ 2",
                    "Ответ 3",
                    "Ответ 4",
                    "Ответ 5"
                ),
                2
            ),
            Question(
                "Вопрос 4",
                arrayOf(
                    "Ответ 1",
                    "Ответ 2",
                    "Ответ 3",
                    "Ответ 4",
                    "Ответ 5"
                ),
                3
            ),
            Question(
                "Вопрос 5",
                arrayOf(
                    "Ответ 1",
                    "Ответ 2",
                    "Ответ 3",
                    "Ответ 4",
                    "Ответ 5"
                ),
                4
            )
        )
    }
}