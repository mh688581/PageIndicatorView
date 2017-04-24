package com.rd.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.SwapAnimationValue;

public class SwapAnimation extends BaseAnimation<ValueAnimator> {

    private static final String ANIMATION_COORDINATE = "ANIMATION_COORDINATE";
    private static final int COORDINATE_NONE = -1;

    private SwapAnimationValue value;
    private int widthStart = COORDINATE_NONE;
    private int widthEnd = COORDINATE_NONE;

    public SwapAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new SwapAnimationValue();
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(BaseAnimation.DEFAULT_ANIMATION_TIME);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @Override
    public SwapAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);

            if (animator.getValues() != null && animator.getValues().length > 0) {
                animator.setCurrentPlayTime(playTime);
            }
        }

        return this;
    }

    @NonNull
    public SwapAnimation with(int widthStart, int widthEnd) {
        if (animator != null && hasChanges(widthStart, widthEnd)) {
            this.widthStart = widthStart;
            this.widthEnd = widthEnd;

            PropertyValuesHolder holder = createColorPropertyHolder();
            animator.setValues(holder);
        }

        return this;
    }

    private PropertyValuesHolder createColorPropertyHolder() {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_COORDINATE, widthStart, widthEnd);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int coordinate = (int) animation.getAnimatedValue(ANIMATION_COORDINATE);
        value.setWidth(coordinate);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int widthStart, int widthEnd) {
        if (this.widthStart != widthStart) {
            return true;
        }

        if (this.widthEnd != widthEnd) {
            return true;
        }

        return false;
    }
}
