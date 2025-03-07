package com.rsschool.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rsschool.quiz.fragments.QuestionFragment
import com.rsschool.quiz.fragments.ResultFragment
import com.rsschool.quiz.interfaces.QuestionFragmentListener
import com.rsschool.quiz.interfaces.ResultFragmentListener

const val CURRENT_QUIZ = "currentQuiz"
const val CURRENT_QUESTION = "currentQuestion"


class MainActivity : AppCompatActivity(),
    QuestionFragmentListener, ResultFragmentListener {

    private val themes = arrayOf(
        R.style.Theme_Quiz_First,
        R.style.Theme_Quiz_Second,
        R.style.Theme_Quiz_Third,
        R.style.Theme_Quiz_Forth,
        R.style.Theme_Quiz_Fifth
    )

    private var currentQuestion = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getCurrentTheme())
        setContentView(R.layout.activity_main)
        setQuestionFragment()
    }

    private fun getCurrentTheme(): Int {
        currentQuestion = getCurrentQuestion()
        var themeId = 0
        if (currentQuestion > 0) {
            themeId = currentQuestion % themes.size
        }
        return themes[themeId]
    }

    private fun getCurrentQuestion(): Int {
        val sPref = getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        return sPref.getInt(CURRENT_QUESTION, -1)
    }

    private fun setQuestionFragment() {
        replaceFragment(QuestionFragment.newInstance(currentQuestion))
    }

    private fun setResultFragment() {
        replaceFragment(ResultFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun setNextQuestion() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0,0)
        finish()
    }

    override fun setResult() {
        setResultFragment()
    }

    override fun startNewQuiz() {
        setNextQuestion()
    }
}