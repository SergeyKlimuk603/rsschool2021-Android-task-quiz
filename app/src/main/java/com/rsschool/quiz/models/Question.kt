package com.rsschool.quiz.models

data class Question(
    val question: String,
    val answers: Array<String>,
    val rightAnswer: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Question

        if (question != other.question) return false
        if (!answers.contentEquals(other.answers)) return false
        if (rightAnswer != other.rightAnswer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = question.hashCode()
        result = 31 * result + answers.contentHashCode()
        result = 31 * result + rightAnswer
        return result
    }
}
