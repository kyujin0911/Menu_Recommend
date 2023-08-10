package kr.ac.tukorea.recommend.menu.util

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import kr.ac.tukorea.recommend.menu.databinding.ToastTinoBinding

object TinoToast {
    fun customToastView(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastTinoBinding = ToastTinoBinding.inflate(inflater)
        binding.toastText.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM, 0, 72.toPx())
            //or Gravity.CENTER, 0, 120.toPx()
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
