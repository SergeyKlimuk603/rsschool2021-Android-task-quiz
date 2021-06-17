package com.rsschool.quiz.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsschool.quiz.CURRENT_QUIZ
import com.rsschool.quiz.R
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.interfaces.ResultFragmentListener
import com.rsschool.quiz.models.Question
import java.lang.StringBuilder


class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private var listener: ResultFragmentListener? = null

    private lateinit var userAnswers: Array<Int>
    private lateinit var questions: Array<Question?>
    private val stringResult = StringBuilder("")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ResultFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sPref = context?.getSharedPreferences(CURRENT_QUIZ, MODE_PRIVATE)
        val lastQuestion = sPref?.getInt(LAST_QUESTION, -1) ?: -1
        userAnswers = Array(lastQuestion + 1) { -1 }
        questions = Array(lastQuestion + 1) { null }
        for (i in userAnswers.indices) {
            userAnswers[i] = sPref?.getInt("$USER_CHOICE-$i", -1) ?: -1
            val answers = Array<String>(5) { "" }
            for (j in answers.indices) {
                sPref?.getString("$ANSWER-$i-$j", "") ?: ""
            }
            questions[i] = Question(
                sPref?.getString("$QUESTION-$i", "") ?: "",
                answers,
                sPref?.getInt("$RIGHT_ANSWER-$i", -1) ?: -1
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        var result = 0
        for ((i, e) in userAnswers.withIndex()) {
            stringResult.append("$i) ${questions[i]?.question}\n" +
                    "Ваш ответ: ${questions[i]?.answers?.get(e)}\n\n")
            if (e == questions[i]?.rightAnswer) {
                result++
            }
        }
        stringResult.insert(0, "Ваш результат: $result/${userAnswers.size}\n\n")
        binding.tvResult.text = "Ваш результат: $result/${userAnswers.size}"
        setButtonListeners()
        return binding.root
    }

    private fun setButtonListeners() {
        binding.btnBack.setOnClickListener() {
            listener?.startNewQuiz()
        }
        binding.btnClose.setOnClickListener() {
            activity?.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ResultFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}