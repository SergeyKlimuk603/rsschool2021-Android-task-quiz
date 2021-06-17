package com.rsschool.quiz.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.rsschool.quiz.CURRENT_QUESTION
import com.rsschool.quiz.CURRENT_QUIZ
import com.rsschool.quiz.database.QuestionBase
import com.rsschool.quiz.databinding.FragmentQuestionBinding
import com.rsschool.quiz.interfaces.QuestionFragmentListener

private const val QUESTION = "question"
private const val ANSWER = "answer"
private const val RIGHT_ANSWER = "rightAnswer"
private const val LAST_QUESTION = "lastQuestion"
private const val USER_CHOICE = "userChoice"

class QuestionFragment : Fragment() {

    private var listener: QuestionFragmentListener? = null

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private var currentQuestion = -1
    private var lastQuestion = -1
    private var userChoice = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is QuestionFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentQuestion = it.getInt(CURRENT_QUESTION)
        }
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        if (currentQuestion < 0) {
            createQuizFile(sPref)
            currentQuestion = 0
        }
        lastQuestion = sPref?.getInt(LAST_QUESTION, -1) ?: -1
        userChoice = sPref?.getInt("$USER_CHOICE-$currentQuestion", -1) ?: -1
        println()
    }

    private fun createQuizFile(sPref: SharedPreferences?) {
        val questions = QuestionBase().getQuestions()
        val editor = sPref?.edit()
        editor?.putInt(CURRENT_QUESTION, 0)
        for ((i, question) in questions.withIndex()) {
            editor?.putString("$QUESTION-$i", question.question)
            for ((j, answer) in question.answers.withIndex()) {
                editor?.putString("$ANSWER-$i-$j", answer)
            }
            editor?.putInt("$RIGHT_ANSWER-$i", question.rightAnswer)
            editor?.putInt("$USER_CHOICE-$i", -1)
        }
        editor?.putInt(LAST_QUESTION, questions.lastIndex)
        editor?.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        val radioButtons = arrayOf(binding.optionOne, binding.optionTwo, binding.optionThree,
            binding.optionFour, binding.optionFive)
        setButtonsState(radioButtons)
        setTextView(radioButtons)
        setButtonListeners(radioButtons)
        return binding.root
    }

    private fun setButtonsState(radioButtons: Array<RadioButton>) {
        when (currentQuestion) {
            0 -> {
                binding.previousButton.isEnabled = false
                binding.toolbar.navigationIcon = null
            }
            lastQuestion -> binding.nextButton.text = "Результат"
        }
        when (userChoice) {
            -1 -> binding.nextButton.isEnabled = false
            else -> radioButtons[userChoice].isChecked = true
        }
    }

    private fun setTextView(radioButtons: Array<RadioButton>) {
        binding.toolbar.title = "Вопрос ${currentQuestion + 1}"
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        binding.question.text = sPref?.getString("$QUESTION-$currentQuestion", "")
        for (index in radioButtons.indices) {
            radioButtons[index].text = sPref?.getString("$ANSWER-$currentQuestion-$index", "")
        }
    }

    private fun setButtonListeners(radioButtons: Array<RadioButton>) {
        binding.nextButton.setOnClickListener() {
            when (currentQuestion) {
                lastQuestion -> {
                    recordState(-1)
                    listener?.setResult()
                }
                else -> changeQuestion(currentQuestion + 1)
            }
        }
        binding.previousButton.setOnClickListener(previousListener)
        binding.toolbar.setNavigationOnClickListener(previousListener)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.nextButton.isEnabled = true
            radioButtons.forEachIndexed { index, radioButton ->
                if (radioButton.id == checkedId) {
                    userChoice = index
                }
            }
        }
    }

    private fun changeQuestion(newQuestion: Int) {
        recordState(newQuestion)
        listener?.setNextQuestion()
    }

    private fun recordState(newQuestion: Int) {
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        val editor = sPref?.edit()
        editor?.putInt(CURRENT_QUESTION, newQuestion)
        editor?.putInt("$USER_CHOICE-$currentQuestion", userChoice)
        editor?.apply()
    }

    private val previousListener = View.OnClickListener{changeQuestion(currentQuestion - 1)}

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(currentQuestion: Int) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putInt(CURRENT_QUESTION, currentQuestion)
                }
            }
    }
}