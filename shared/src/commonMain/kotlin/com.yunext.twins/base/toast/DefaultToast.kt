//package com.yunext.farm.common.toast
//
//import android.content.Context
//import android.widget.Toast
//
//private class DefaultToast : IToast {
//    private var mToast: Toast? = null
//
//    override fun toast(context: Context, msg: String) {
//        show(context, msg)
//    }
//
//    override fun toastLong(context: Context, msg: String) {
//        show(context, msg, Toast.LENGTH_LONG)
//    }
//
//    private fun show(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT) {
//        val toast = mToast
//        if (toast == null) {
//            mToast = Toast.makeText(context, msg, duration).also {
//                it.show()
//            }
//        } else {
//            toast.setText(msg)
//            toast.show()
//        }
//    }
//
//    companion object{
//         internal val DEFAULT: IToast = DefaultToast()
//    }
//}
//
//fun Context.extToast(msg: String) {
//    DefaultToast.DEFAULT.toast(this,msg)
//}
//
//fun Context.extToastLong(msg: String) {
//    DefaultToast.DEFAULT.toastLong(this,msg)
//}