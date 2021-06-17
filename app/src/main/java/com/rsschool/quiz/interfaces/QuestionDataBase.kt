package com.rsschool.quiz.interfaces

import com.rsschool.quiz.models.Question

interface QuestionDataBase {
    fun getQuestions(): Array<Question>
}