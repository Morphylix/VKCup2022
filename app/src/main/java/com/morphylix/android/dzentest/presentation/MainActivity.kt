package com.morphylix.android.dzentest.presentation

import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.morphylix.android.dzentest.data.cache.InMemoryCategoryList
import com.morphylix.android.dzentest.databinding.ActivityMainBinding
import com.morphylix.android.dzentest.utils.THRESHOLD
import com.morphylix.android.dzentest.utils.swipe
import com.morphylix.android.dzentest.widget.CustomButton
import com.yuyakaido.android.cardstackview.*
import com.morphylix.android.dzentest.R.anim.shake_anim

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), CardStackListener {

    private lateinit var layoutManager: CardStackLayoutManager
    private val adapter = CategoryAdapter(InMemoryCategoryList.categoryList)
    private lateinit var binding: ActivityMainBinding
    private var isChosen = false
    private lateinit var shakeAnim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = CardStackLayoutManager(this, this).apply {
            setSwipeableMethod(SwipeableMethod.Manual)
            setOverlayInterpolator(LinearInterpolator())
            setSwipeThreshold(THRESHOLD)
        }
        layoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        shakeAnim = AnimationUtils.loadAnimation(this, shake_anim)

        with(binding) {
            categoryStackView.layoutManager = layoutManager
            categoryStackView.adapter = adapter
            categoryStackView.itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
            likeButton.setOnClickListener {
                provideButtonListener(it as CustomButton)
                setChosen(true)
                categoryStackView.swipe(Direction.Right, layoutManager)
                Log.i(TAG, "${layoutManager.cardStackState.targetPosition}")
                if (layoutManager.cardStackState.targetPosition != -1)
                    likeGradientView.visibility = View.VISIBLE
            }
            dislikeButton.setOnClickListener {
                provideButtonListener(it as CustomButton)
                setChosen(false)
                categoryStackView.swipe(Direction.Left, layoutManager)
                if (layoutManager.cardStackState.targetPosition != -1)
                    dislikeGradientView.visibility = View.VISIBLE
            }
            backButton.setOnClickListener {
                provideButtonListener(it as CustomButton)
                categoryStackView.rewind()
            }
        }
    }

    private fun provideButtonListener(button: CustomButton) {
        button.setUIState(CustomButton.UIState.Pressed, isAnim = true)
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        InMemoryCategoryList.categoryList[position].isChosen = isChosen
        Log.i(TAG, "${InMemoryCategoryList.categoryList}")
        displayDoneAndContinue(position)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        if (ratio >= THRESHOLD && direction == Direction.Right) {
            binding.likeGradientView.visibility = View.VISIBLE
            setChosen(true)
        } else if (ratio >= THRESHOLD && direction == Direction.Left) {
            binding.dislikeGradientView.visibility = View.VISIBLE
            setChosen(false)
        } else {
            hideGradients()
        }
    }

    override fun onCardSwiped(direction: Direction?) {
        hideGradients()
    }

    override fun onCardCanceled() {
        layoutManager.topView.startAnimation(shakeAnim)
        hideGradients()
    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {
        hideDoneAndContinue()
    }

    private fun hideGradients() {
        with(binding) {
            likeGradientView.visibility = View.GONE
            dislikeGradientView.visibility = View.GONE
        }
    }

    private fun displayDoneAndContinue(position: Int) {
        if (position == InMemoryCategoryList.categoryList.size - 1) { // if last card was swiped
            with(binding) {
                continueButton.visibility = View.VISIBLE
                doneAnimImageView.visibility = View.VISIBLE
                val doneAnimDrawable = doneAnimImageView.drawable
                if (doneAnimDrawable is AnimatedVectorDrawable) {
                    doneAnimDrawable.start()
                }
            }
        }
    }

    private fun hideDoneAndContinue() {
        with(binding) {
            continueButton.visibility = View.GONE
            doneAnimImageView.visibility = View.GONE
        }
    }

    private fun setChosen(userChoice: Boolean) {
        isChosen = userChoice
    }
}