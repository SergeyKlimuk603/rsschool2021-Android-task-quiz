package com.rsschool.quiz.database

import com.rsschool.quiz.interfaces.QuestionDataBase
import com.rsschool.quiz.models.Question

class QuestionBase : QuestionDataBase {
    override fun getQuestions(): Array<Question> {
        return arrayOf(
            Question(
                "Сколько ног у человека?",
                arrayOf(
                    "1",
                    "2",
                    "3",
                    "4",
                    "У человека нет ног"
                ),
                1
            ),
            Question(
                "Раз в год и ... стреляет!",
                arrayOf(
                    "ружье",
                    "охотник",
                    "сломанный танк",
                    "палка",
                    "гороховый суп"
                ),
                3
            ),
            Question(
                "Единица измерения длины?",
                arrayOf(
                    "Грамм",
                    "Ньютон",
                    "Литр",
                    "Штука",
                    "Метр"
                ),
                4
            ),
            Question(
                "Стандартная крепость водки?",
                arrayOf(
                    "38%",
                    "39%",
                    "40%",
                    "41%",
                    "42%"
                ),
                2
            ),
            Question(
                "Какой цвет волос у блондинки?",
                arrayOf(
                    "Белый",
                    "Каштановый",
                    "Черный",
                    "Красный",
                    "У блондинок нет волос"
                ),
                0
            ),
//            Question(
//                "Новый вопрос 1",
//                arrayOf(
//                    "Ответ 0",
//                    "Ответ 1",
//                    "Ответ 2",
//                    "Ответ 3",
//                    "Ответ 4"
//                ),
//                0
//            ),
//            Question(
//                "Новый вопрос 2",
//                arrayOf(
//                    "Ответ 0",
//                    "Ответ 1",
//                    "Ответ 2",
//                    "Ответ 3",
//                    "Ответ 4"
//                ),
//                0
//            )
        )
    }
}