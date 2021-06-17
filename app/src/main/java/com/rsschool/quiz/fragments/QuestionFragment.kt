package com.rsschool.quiz.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            editor?.putInt(LAST_QUESTION, questions.size)
            editor?.apply()
            currentQuestion = 0
        }
        lastQuestion = sPref?.getInt(LAST_QUESTION, -1) ?: -1
        userChoice = sPref?.getInt(USER_CHOICE, -1) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        when (currentQuestion) {
            0 -> {
                binding.previousButton.isEnabled = false
                binding.toolbar.navigationIcon = null
            }
            lastQuestion -> binding.nextButton.text = "Результат"
        }
        when (userChoice) {
            0 -> binding.optionOne.isChecked = true
            1 -> binding.optionTwo.isChecked = true
            2 -> binding.optionThree.isChecked = true
            3 -> binding.optionFour.isChecked = true
            4 -> binding.optionFive.isChecked = true
        }
        binding.toolbar.title = "Вопрос ${currentQuestion + 1}"
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        binding.question.text = sPref?.getString("$QUESTION-$currentQuestion", "")
        binding.optionOne.text = sPref?.getString("$ANSWER-$currentQuestion-0", "")
        binding.optionTwo.text = sPref?.getString("$ANSWER-$currentQuestion-1", "")
        binding.optionThree.text = sPref?.getString("$ANSWER-$currentQuestion-2", "")
        binding.optionFour.text = sPref?.getString("$ANSWER-$currentQuestion-3", "")
        binding.optionFive.text = sPref?.getString("$ANSWER-$currentQuestion-4", "")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener() {
            when (currentQuestion) {
                lastQuestion -> {
                    // TODO Переход к следующему вопросу
                }
                else -> {
                    recordState(++currentQuestion)
                }
            }
        }
        binding.previousButton.setOnClickListener(previousListener1)
        binding.toolbar.setNavigationOnClickListener() {previousListener}
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            binding.nextButton.isEnabled = true
            userChoice = when (checkedId) {
                binding.optionOne.id -> 0
                binding.optionOne.id -> 1
                binding.optionOne.id -> 2
                binding.optionOne.id -> 3
                else -> 4
            }
        }
    }

    object PreviousListener1: View.OnClickListener {
        override fun onClick(v: View?) {
            println()
        }

    }
    private val previousListener = recordState(--currentQuestion)
    private fun recordState(newQuestion: Int) {
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        val editor = sPref?.edit()
        editor?.putInt(CURRENT_QUESTION, newQuestion)
        editor?.putInt("$USER_CHOICE-$currentQuestion", userChoice)
        editor?.apply()
        listener?.setNextQuestion()
    }

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