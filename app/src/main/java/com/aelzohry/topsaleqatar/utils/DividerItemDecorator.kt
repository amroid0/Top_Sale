package com.aelzohry.topsaleqatar.utils

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator(private var divider:Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        var pdding = 8 * (parent.context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
        var dividerLeft = parent.paddingLeft + pdding
        var dividerRight = parent.width - parent.paddingRight - pdding
        var childCount = parent.childCount

        /*
            return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        public static float convertPixelsToDp(float px, Context context){
    return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
}
        * */

        for (i in 0 .. childCount.minus(2)){

            var child = parent.getChildAt(i)
            var params =  child.layoutParams as RecyclerView.LayoutParams
            var dividerTop = child.bottom + params.bottomMargin;
            var dividerBottom = dividerTop + divider?.intrinsicHeight!!

            divider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider?.draw(c)
        }

    }
}