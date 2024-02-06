package com.comfortablesporttools.yourfitnesstracker.onBoarding

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.comfortablesporttools.yourfitnesstracker.MainTitleActivity
import com.comfortablesporttools.yourfitnesstracker.R

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: FragmentPagerAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        pagerAdapter = OnboardingPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        if (firstOpenCheck(this)) {
            Toast.makeText(this@OnboardingActivity, "Добро пожаловать!", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(Intent(this@OnboardingActivity, MainTitleActivity::class.java))
            finish()
        }

        val nextButton = findViewById<Button>(R.id.nextButton1)
        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < pagerAdapter.count - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                startActivity(Intent(this@OnboardingActivity, MainTitleActivity::class.java))
                finish()
            }
        }
    }

    private fun firstOpenCheck(context: Context): Boolean {
        val newText = PreferenceManager.getDefaultSharedPreferences(context)
        val startText = newText.getBoolean("isFirstTime", true)
        if (startText) { newText.edit().putBoolean("isFirstTime", false).apply() }
        return startText
    }

    val listImage = listOf(
        R.drawable.meal_fit_arrow,
        R.drawable.meal_fit_arrow,
        R.drawable.meal_fit_arrow
    )

    private inner class OnboardingPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val onboardingDataList = listOf(
            OnboardingData("Создавайте тренировки", "создайте свою тренировку со своими уникальными упражнениями",  listImage[0]),
            OnboardingData("Гибкая настройка", "описывайте свои тренировки, вносите изменения в любой момент", listImage[1]),
            OnboardingData("Удобное использование", "храните свои тренировки в удобной системе и меняйте свои задачи", listImage[2])
        )

        override fun getCount(): Int {
            return onboardingDataList.size
        }

        override fun getItem(position: Int): Fragment {
            val data = onboardingDataList[position]
            return OnboardingFragment.newInstance(data.title, data.description, data.imageResId)
        }
    }

    private data class OnboardingData(val title: String, val description: String, val imageResId: Int)

}