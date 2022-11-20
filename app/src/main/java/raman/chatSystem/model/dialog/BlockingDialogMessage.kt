package raman.chatSystem.model.dialog

import android.app.ProgressDialog
import android.content.Context

class BlockingDialogMessage(context: Context): ProgressDialog(context)
{
    init {
        setCanceledOnTouchOutside(false)
    }
}